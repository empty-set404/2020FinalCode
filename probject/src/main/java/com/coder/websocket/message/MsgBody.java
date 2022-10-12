package com.coder.websocket.message;

public class MsgBody {

    public int type = MessageType.NORMAL_MSG; // 消息类型，默认为普通消息

    public String from; // 发送用户

    public String to; // 接收用户

    public String content; // 消息内容

//    public String date = String.valueOf(System.currentTimeMillis());; // 发送时间

    public MsgBody() {
    }

    public MsgBody(String to, String content) {
        this.to = to;
        this.content = content;
    }

    public MsgBody(String from, String to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "MsgBody{" +
                "type=" + type +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
