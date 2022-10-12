package com.coder.content;

import java.nio.channels.SocketChannel;

public interface Content {

    public SocketChannel channel();

    public void writeAndFlush(Object msg);

    public void writeAndFlush(SocketChannel channel, Object msg);

}
