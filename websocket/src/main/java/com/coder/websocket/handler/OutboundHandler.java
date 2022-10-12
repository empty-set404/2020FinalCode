package com.coder.websocket.handler;

import com.coder.content.Content;

public interface OutboundHandler extends Handle{

    public void channelWrite(Content ctx, Object msg);

}
