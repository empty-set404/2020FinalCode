package com.coder.frame.socket;

/**
 * 文本消息
 */
public class TextMessageFrame extends WebsocketDataFrame {

    private String content = "";//内容

    public TextMessageFrame(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
