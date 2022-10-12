package com.coder.websocket;

import cn.coder.spring.bean.factory.annotation.Autowired;
import cn.coder.spring.bean.factory.annotation.Qualifier;
import cn.coder.spring.context.annotation.Scope;
import cn.coder.spring.stereotype.Component;
import com.coder.codec.WebSocketCodecHandler;
import com.coder.server.ServerBootstrap;
import com.coder.thread.WorkThreadGroup;
import com.coder.websocket.handler.WebSocketAcceptHandler;
import com.coder.websocket.handler.WebSocketHandlerImpl;
import com.coder.websocket.handler.WebsocketHandler;

// 单例模式
public class WebsocketStart implements Runnable {

    private static volatile WebsocketStart instance;

    private WebsocketStart() {
    }

    public static synchronized WebsocketStart getInstance() {
        if (instance == null) {
            synchronized (WebsocketStart.class) {
                if (instance == null) {
                    instance = new WebsocketStart();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        new ServerBootstrap()
                .group(new WorkThreadGroup())
                .addLast(new WebSocketAcceptHandler())
                .addLast(new WebSocketCodecHandler())
                .addLast(new WebSocketHandlerImpl())
                .run();
    }
}
