package com.coder.chain;

import com.coder.websocket.handler.Handle;

import java.util.LinkedHashMap;
import java.util.Set;

public class DefaultHandlerChain implements HandlerChain {

    private static LinkedHashMap<String, Handle> handlers = new LinkedHashMap<>();

    @Override
    public HandlerChain addFirst(String name, Handle handle) {
        handlers.put(name, handle);
        return this;
    }

    @Override
    public HandlerChain addLast(String name, Handle handle) {
        handlers.put(name, handle);
        return this;
    }

    @Override
    public Handle remove(String name) {
        return handlers.remove(name);
    }

    @Override
    public Handle get(String name) {
        return handlers.get(name);
    }

    @Override
    public Set<String> keySet() {
        return handlers.keySet();
    }

}
