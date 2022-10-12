package com.coder.websocket.handler;

import com.coder.content.WsContent;

import java.io.IOException;

public interface WebsocketHandler extends Handle {

    void onOpen(WsContent ctx) throws IOException;

    void onMessage(WsContent ctx, Object frame) throws IOException;

    void onException(WsContent ctx, Exception ex);

    void onClose(WsContent ctx) throws IOException;

}
