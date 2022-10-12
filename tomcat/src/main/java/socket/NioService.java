package socket;

import config.TomcatConfig;
import filterChain.FilterChain;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class NioService implements TomcatService {

    private static Logger logger = Logger.getLogger(FilterChain.class.getSimpleName());

    private Selector selector;

    private ServerSocketChannel server;

    @Override
    public void openPort(Integer port, Integer timeOut) throws IOException {
        selector = Selector.open();
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        server.socket().setSoTimeout(timeOut);

        logger.info(new Date() + " 启动成！！ " + Thread.currentThread().getName());
        logger.info(new Date() + " " + server.getLocalAddress() + ": " + port + " " + Thread.currentThread().getName());

        SelectionKey selectionKey = server.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    @Override
    public void doService() throws IOException {
        Work[] works = new Work[TomcatConfig.HTTP_THREAD_NUM];
        // 初始化
        for (int i = 0; i < works.length; i++) {
            works[i] = new Work("work-" + i);
        }

        AtomicInteger index = new AtomicInteger();
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();

                if (next.isAcceptable()) {
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    // 实现轮询
                    works[index.getAndIncrement() % works.length].register(channel);
                    logger.info(new Date() + " 请求连接 " + channel.getLocalAddress() + ": " + channel.getRemoteAddress() + " " + Thread.currentThread().getName());
                }
            }
        }
    }
}
