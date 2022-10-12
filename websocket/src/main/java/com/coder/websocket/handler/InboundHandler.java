package com.coder.websocket.handler;

import com.coder.content.Content;

public interface InboundHandler extends Handle {

    public void channelRead(Content ctx, Object msg);

}
