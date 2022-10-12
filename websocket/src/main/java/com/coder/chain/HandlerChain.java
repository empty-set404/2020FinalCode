package com.coder.chain;

import com.coder.websocket.handler.Handle;

import java.util.Set;

public interface HandlerChain {

    HandlerChain addFirst(String name, Handle handle);

    HandlerChain addLast(String name, Handle handle);

    Handle remove(String name);

    Handle get(String name);

    Set<String> keySet();

}
