package com.coder.websocket.handler;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

public interface AcceptHandler extends Handle{

    public boolean handlerHandshake(SocketChannel socketChannel) throws IOException, NoSuchAlgorithmException, InterruptedException;

}
