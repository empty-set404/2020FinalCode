package com.coder.thread;

import com.coder.chain.HandlerChain;

public abstract class AbstractNioThreadGroup implements NioThreadGroup {

    protected HandlerChain handlerChain;

//    public AbstractNioThreadGroup(HandlerChain handlerChain) {
//        this.handlerChain = handlerChain;
//    }
}
