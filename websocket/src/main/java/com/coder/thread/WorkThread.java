package com.coder.thread;

import com.coder.chain.HandlerChain;
import com.coder.codec.CodecHandler;
import com.coder.content.Content;
import com.coder.content.WsContent;
import com.coder.content.WsContentImpl;
import com.coder.websocket.handler.Handle;
import com.coder.websocket.handler.InboundHandler;
import com.coder.websocket.handler.OutboundHandler;
import com.coder.websocket.handler.WebsocketHandler;
import com.coder.utils.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkThread extends Thread implements NioThread {

    private HandlerChain handlerChain;

    private Selector selector;

    private boolean flag = false;

    private boolean running = true;

    private static AtomicInteger index = new AtomicInteger();

    private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();

    public WorkThread(HandlerChain handlerChain) {
        this.handlerChain = handlerChain;
        try {
            super.setName("worker-thread-" + index.getAndIncrement());
            this.selector = Selector.open();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(SocketChannel socketChannel) {
        this.queue.add(() -> {
            try {
                System.out.println("============");
                System.out.println(Thread.currentThread().getName());

                SelectionKey selectionKey = socketChannel.register(this.selector, SelectionKey.OP_READ);

                WsContent wsContent = new WsContentImpl(this, socketChannel);

                selectionKey.attach(wsContent);

                // 触发open
                Set<String> strings = handlerChain.keySet();
                for (String key : strings) {
                    Handle handle = handlerChain.get(key);
                    if (handle instanceof WebsocketHandler) {
                        WebsocketHandler websocketHandler = (WebsocketHandler) handle;
                        websocketHandler.onOpen(wsContent);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
//                try {
//                    socketChannel.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
            }
        });

        // 唤醒select
        this.selector.wakeup();

    }

    @Override
    public void close() {
        this.running = false;
        try {
            Set<String> strings = this.handlerChain.keySet();
            for (String key : strings) {
                Handle handle = handlerChain.get(key);
                if (handle instanceof WebsocketHandler) {
                    // TODO
                    ((WebsocketHandler) handle).onClose(null);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (this.running) {
            this.flag = true;
            SocketChannel socketChannel = null;
            Content wsContent = null;
            try {
                this.selector.select();

                // 执行任务
                Runnable poll = queue.poll();
                if (poll != null) {
                    poll.run();
                }

                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    socketChannel = (SocketChannel) selectionKey.channel();
                    wsContent = (Content) selectionKey.attachment();
                    Set<String> strings = handlerChain.keySet();

                    ByteBuffer buffer = BufferUtils.channleToBuffer(socketChannel);
                    String data = "";

                    if (!selectionKey.isValid()) {
                        continue;
                    }

                    // 处理读事件
                    if (selectionKey.isReadable()) {
                        System.out.println("处理读事件.....");

                        while (buffer.hasRemaining()) {
                            // 处理入栈处理器
                            for (String key : strings) {
                                Handle handle = handlerChain.get(key);

                                // 判断是否有编解码器
                                if (handle instanceof CodecHandler) {
                                    data = (String) ((CodecHandler) handle).decode(wsContent, buffer);
                                }

                                if (handle instanceof InboundHandler) {
                                    ((InboundHandler) handle).channelRead(wsContent, data);
                                }

                                if (handle instanceof WebsocketHandler) {
                                    // 当客户端关闭时 buffer解码后返回null
                                    ((WebsocketHandler) handle).onMessage((WsContent) wsContent, data);
                                }
                            }
                        }

                        // 绑定写事件
//                        selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
                    }

                    // 处理写事件
                    if (selectionKey.isWritable()) {
                        System.out.println("处理写事件....");

                        // 发送完了就取消写事件，否则下次还会进入写事件分支（因为只要还可写，就会进入）
                        selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);

                    }

                }
            }catch (Exception e) {
                try {
                    Set<String> strings = this.handlerChain.keySet();
                    for (String key : strings) {
                        Handle handle = handlerChain.get(key);
                        if (handle instanceof WebsocketHandler) {
                            // TODO
                            ((WebsocketHandler) handle).onClose((WsContent) wsContent);
                        }
                    }
                    socketChannel.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        }
    }

    @Override
    public void write(Object msg, Content ctx) {
        System.out.println("write: " + Thread.currentThread().getName());
        Set<String> strings = handlerChain.keySet();
        if (!ctx.channel().isConnected()) return;

        // 处理出栈处理器
        for (String key : strings) {
            Handle handle = handlerChain.get(key);

            // 判断是否有编解码器
            if (handle instanceof CodecHandler) {
                msg = ((CodecHandler) handle).encode(ctx, msg);
            }

            if (handle instanceof OutboundHandler) {
                ((OutboundHandler) handle).channelWrite(ctx, msg);
            }
        }

        try {
            if (msg instanceof ByteBuffer) {
                ByteBuffer buffer = (ByteBuffer) msg;
                buffer.flip();

                BufferUtils.writeToChannel(buffer, ctx.channel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(SocketChannel channel, Object msg) {

    }


    public boolean isRunning() {
        return running;
    }

    public boolean isFlag() {
        return flag;
    }

}
