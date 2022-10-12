package com.coder.content;

import com.coder.thread.NioThread;

import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WsContentImpl implements WsContent {

    private SocketChannel channel;

    private String sessionID;

    private NioThread nioThread;

    public WsContentImpl(NioThread nioThread, SocketChannel channel) {
        this.channel = channel;
        this.nioThread = nioThread;

        try {
            MessageDigest sha1 = MessageDigest.getInstance("sha1");
            byte[] bytes = String.valueOf(System.currentTimeMillis()).getBytes();
            sha1.update(bytes);
            BigInteger bi = new BigInteger(sha1.digest());
            sessionID = bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SocketChannel channel() {
        return channel;
    }

    @Override
    public void writeAndFlush(Object msg) {
        this.nioThread.write(msg, this);
    }

    @Override
    public void writeAndFlush(SocketChannel channel, Object msg) {
        this.nioThread.write(channel, msg);
    }

    public String getSessionID() {
        return sessionID;
    }

    public NioThread getNioThread() {
        return nioThread;
    }

}
