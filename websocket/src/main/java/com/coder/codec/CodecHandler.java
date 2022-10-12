package com.coder.codec;

import com.coder.content.Content;
import com.coder.websocket.handler.InboundHandler;

import java.io.IOException;

/**
 * 编解码 handler
 */
public interface CodecHandler extends InboundHandler {

    // 编码
    public Object encode(Content content, Object msg);

    // 解码
    public Object decode(Content content, Object msg) throws IOException;

}
