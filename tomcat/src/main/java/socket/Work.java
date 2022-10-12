package socket;

import builder.HttpBuilder;
import builder.NioHttpBuilder;
import filterChain.FilterChain;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

/**
 * 管理线程
 */
public class Work implements Runnable {

    private static Logger logger = Logger.getLogger(FilterChain.class.getSimpleName());

    private Thread thread;
    private String name;
    private Selector selector;
    private boolean flag;

    private ConcurrentLinkedDeque<Runnable> take = new ConcurrentLinkedDeque<>();

    public Work(String name) {
        this.name = name;
    }

    public void register(SocketChannel channel) throws IOException {
        // 初始化一次
        if (!flag) {
            this.selector = Selector.open();
            this.thread = new Thread(this, name);
            this.thread.start();
            flag = true;
        }
        // 向队列添加任务
        this.take.add(() -> {
            try {
                channel.register(this.selector, SelectionKey.OP_READ, null);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });
        this.selector.wakeup();  // 唤醒selector
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.selector.select();

                // 执行任务
                Runnable poll = this.take.poll();
                if (poll != null) {
                    poll.run();
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();

                    // 判断类型
                    if (next.isReadable()) {
                        SocketChannel channel = (SocketChannel) next.channel();

                        logger.info(new Date() + " 触发读事件 " + channel.getLocalAddress() + ": " + channel.getRemoteAddress() + " " + Thread.currentThread().getName());

                        HttpBuilder builder = new NioHttpBuilder(channel);
                        builder.builder();
                        SelectionKey sKey = channel.register(selector, SelectionKey.OP_WRITE);
                        sKey.attach(builder);

                    }

                    if (next.isWritable()) {
                        SocketChannel channel = (SocketChannel) next.channel();

                        logger.info(new Date() + " 触发写事件 " + channel.getLocalAddress() + ": " + channel.getRemoteAddress() + " " + Thread.currentThread().getName());

                        HttpBuilder builder = (HttpBuilder) next.attachment();
                        try {
                            builder.flushAndClose();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            channel.close();
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
