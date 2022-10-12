package com.coder.thread;

import com.coder.config.WebSocketConfig;
import com.coder.websocket.handler.AcceptHandler;
import com.coder.websocket.handler.Handle;
import com.coder.server.ServerBootstrap;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工作线程组
 */
public class WorkThreadGroup extends AbstractNioThreadGroup {

    private static final Integer DEFAULT_MAX_THREAD = Runtime.getRuntime().availableProcessors();

    private NioThread[] nioThreads;

    private AtomicInteger maxThread = new AtomicInteger();

    private AtomicInteger count = new AtomicInteger();

    private Selector selector;

    private boolean running = true;

    private Integer port = WebSocketConfig.DEFAULT_PORT;

    private ServerSocketChannel serverSocketChannel;

    public WorkThreadGroup() {
        this(DEFAULT_MAX_THREAD);
    }

    public WorkThreadGroup(int maxThread) {
        handlerChain = ServerBootstrap.handlerChain;
        this.maxThread.set(maxThread);
        this.init();
    }

    public void init() {
        try {
            this.nioThreads = new WorkThread[maxThread.get()];
            for (int i = 0; i < maxThread.get(); i++) {
                this.nioThreads[i] = new WorkThread(handlerChain);
            }

            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.bind(new InetSocketAddress(this.port));

            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerSocketChannel(SocketChannel socketChannel) {
        NioThread nioThread = this.nioThreads[count.getAndIncrement() % maxThread.get()];
        nioThread.register(socketChannel);

        // TODO
        WorkThread workThread = (WorkThread) nioThread;
        if ((!workThread.isRunning() || !workThread.isFlag())) {
            workThread.start();
        }
    }

    @Override
    public void close() {
        for (int i = 0; i < maxThread.get(); i++) {
            this.nioThreads[i].close();
        }
    }

    @Override
    public void start() {
        try {
            while (this.running) {
                this.selector.select();
                Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isAcceptable()) {
                        System.out.println("请求连接.....");

                        ServerSocketChannel sc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = sc.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE);
                        for (String key : handlerChain.keySet()) {
                            Handle handle = handlerChain.get(key);
                            if (handle instanceof AcceptHandler) {
                                boolean accept = ((AcceptHandler) handle).handlerHandshake(socketChannel);
                                if (accept) {
                                    registerSocketChannel(socketChannel);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
