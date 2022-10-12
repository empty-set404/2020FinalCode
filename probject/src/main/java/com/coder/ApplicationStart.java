package com.coder;

import cn.coder.spring.bean.factory.annotation.Autowired;
import cn.coder.spring.stereotype.Component;
import com.coder.codec.WebSocketCodecHandler;
import com.coder.server.ServerBootstrap;
import com.coder.thread.WorkThreadGroup;
import com.coder.utils.MD5Utils;
import com.coder.websocket.WebsocketStart;
import com.coder.websocket.handler.WebSocketAcceptHandler;
import com.coder.websocket.handler.WebSocketHandlerImpl;
import com.coder.websocket.handler.WebsocketHandler;
import web.CoreApp;
import java.io.IOException;

public class ApplicationStart {
    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        // 启动tomcat服务器
        CoreApp.init(ApplicationStart.class);

        // 启动websocket服务器
        new Thread(WebsocketStart.getInstance()).start();
    }
}
