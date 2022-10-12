package com.coder.thread;

import com.coder.content.Content;

import java.nio.channels.SocketChannel;

public interface NioThread {

    public void register(SocketChannel socketChannel);

    public void close();

    public void write(Object msg, Content ctx);

    public void write(SocketChannel channel, Object msg);

}
