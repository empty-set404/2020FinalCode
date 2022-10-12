package com.coder.websocket.handler;

import com.coder.tmp.WSProtocol;
import com.coder.utils.BufferUtils;
import com.coder.utils.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

public class WebSocketAcceptHandler implements AcceptHandler {
    @Override
    public boolean handlerHandshake(SocketChannel socketChannel) throws IOException, NoSuchAlgorithmException, InterruptedException {
        ByteBuffer buffer = BufferUtils.channleToBuffer(socketChannel);
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        String content = new String(bytes);

        // 封装协议
        WSProtocol.Header header = WSProtocol.Header.decodeFromString(content);
        String key = header.getHeaders("Sec-WebSocket-Key");
        String response = WSProtocol.getHandShakeResponse(key);

        // 写入通道
        BufferUtils.writeToChannel(ByteBuffer.wrap(response.getBytes()), socketChannel);
        return StringUtils.isNotEmpty(response);
    }
}
