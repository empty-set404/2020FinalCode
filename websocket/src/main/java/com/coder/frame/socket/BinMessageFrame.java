package com.coder.frame.socket;

/**
 * 二进制消息
 */
public class BinMessageFrame extends WebsocketDataFrame {

    private byte[] content;//内容

    public BinMessageFrame(byte[] content){
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

}
