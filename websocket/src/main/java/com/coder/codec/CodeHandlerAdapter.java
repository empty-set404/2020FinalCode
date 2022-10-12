package com.coder.codec;

import com.coder.content.Content;

import java.io.IOException;

/**
 * 编解码器 适配器
 */
public abstract class CodeHandlerAdapter implements CodecHandler {

    @Override
    public Object encode(Content content, Object msg) {
        return msg;
    }

    @Override
    public Object decode(Content content, Object msg) throws IOException {
        return msg;
    }

    @Override
    public void channelRead(Content ctx, Object msg) {
//        ctx.writeAndFlush(msg);
    }
}
