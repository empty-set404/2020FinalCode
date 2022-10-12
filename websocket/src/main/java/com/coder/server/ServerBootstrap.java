package com.coder.server;

import com.coder.chain.DefaultHandlerChain;
import com.coder.chain.HandlerChain;
import com.coder.websocket.handler.Handle;
import com.coder.thread.NioThreadGroup;

import java.util.Set;

public class ServerBootstrap {

    public static HandlerChain handlerChain = new DefaultHandlerChain();

    private NioThreadGroup nioThreadGroup;

    public ServerBootstrap() {
    }

    public void run() {
        nioThreadGroup.start();
    }

    public ServerBootstrap group(NioThreadGroup threadGroup) {
        nioThreadGroup = threadGroup;
        return this;
    }

    public ServerBootstrap addFirst(String name, Handle handle) {
        handlerChain.addFirst(name, handle);
        return this;
    }

    public ServerBootstrap addLast(String name, Handle handle) {
        handlerChain.addLast(name, handle);
        return this;
    }

    public ServerBootstrap addLast(Handle handle) {
        String name = handle.getClass().getSimpleName() + System.currentTimeMillis();
        handlerChain.addLast(name, handle);
        return this;
    }

    public Handle remove(String name) {
        return handlerChain.remove(name);
    }

    public Handle get(String name) {
        return handlerChain.get(name);
    }

    public Set<String> keySet() {
        return handlerChain.keySet();
    }


}
