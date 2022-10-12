package com.coder.websocket.handler;

import com.alibaba.fastjson.JSONObject;
import com.coder.content.WsContent;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketService implements WebsocketHandler {

    private static Map<String, WsContent> users = new ConcurrentHashMap<>();

    // 建立连接时发送系统广播
    @Override
    public void onOpen(WsContent ctx) throws IOException {
        String id = ctx.channel().getRemoteAddress().toString();
        for (String name : users.keySet()) {
            if (name.equals(id)) {
                // 用户已经存在
                System.out.println("用户已经存在-----");
            }
        }

        // 添加用户
        users.put(id, ctx);

        System.out.println(users.keySet());

        JSONObject message = new JSONObject();
        message.put("type", "user login");
        message.put("data", id);

        // 通知其他人新用户加入
        forwardMessage(users.keySet(), message.toString());

        System.out.println("用户链接: " + id);
    }

    // 转发消息给指定用户
    private void forwardMessage(String address, String message) {
        System.out.println(address + " ss");
        WsContent wsContent = users.get(address);
        wsContent.writeAndFlush(message);
    }

    // 转发消息给所有人
    private void forwardMessage(Collection<String> addresses, String message) {
        for (String address : addresses) {
            forwardMessage(address, message);
        }
    }

    // 发送消息给除自己之外的所有人
    private void forwardMessageExceptMe(String address, String message) {
        for (String key : users.keySet()) {
            if (!key.equals(address)) {
                forwardMessage(key, message);
            }
        }
    }

    // 收到消息
    @Override
    public void onMessage(WsContent ctx, Object message) throws IOException {
        JSONObject json = JSONObject.parseObject((String) message);
        String type = (String) json.get("type");

        System.out.println(json);

        if ("connect".equals(type)) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "connect");
            jsonObject.put("data", ctx.channel().getRemoteAddress().toString());
            jsonObject.put("list", users.keySet());
            forwardMessage(ctx.channel().getRemoteAddress().toString(), jsonObject.toString());
        }

        // type data
        if ("disconnect".equals(type)) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "user disconnected");
            jsonObject.put("data", ctx.channel().getRemoteAddress().toString());
            forwardMessageExceptMe(ctx.channel().getRemoteAddress().toString(), jsonObject.toString());
        }

        // type data
        if("chat message".equals(type)) {
            forwardMessageExceptMe(ctx.channel().getRemoteAddress().toString(), (String) message);
        }

        // type sender msg
        if ("new user greet".equals(type)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "need connect");
            jsonObject.put("sender", ctx.channel().getRemoteAddress().toString());
            jsonObject.put("msg", message);
            forwardMessageExceptMe(ctx.channel().getRemoteAddress().toString(), jsonObject.toString());
        }

        // type sender
        if ("ok we connect".equals(type)) {
            String sendTo = (String) json.get("receiver");
            String sender = (String) json.get("sender");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "ok we connect");
            jsonObject.put("sender", sender);
            forwardMessage(sendTo, jsonObject.toString());
        }

        // type
        if ("sdp".equals(type)) {
            String sendTo = (String) json.get("to");
            String sender = (String) json.get("sender");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "sdp");
            jsonObject.put("description", json.get("description"));
            jsonObject.put("sender", sender);
            forwardMessage(sendTo, jsonObject.toString());
        }

        if ("ice".equals(type)) {
            String sendTo = (String) json.get("to");
            String sender = (String) json.get("sender");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "ice");
            jsonObject.put("candidate", json.get("candidate"));
            jsonObject.put("sender", sender);
//            jsonObject.put("tmp", tmp);
            forwardMessage(sendTo, jsonObject.toString());
        }
    }

    //用户断开连接的断后操作
    @Override
    public void onClose(WsContent ctx) throws IOException {
        // 用户离开
        String id = ctx.channel().getRemoteAddress().toString();
        users.remove(id);

        // 通知其他人
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "userClose");
        jsonObject.put("data", id);
        forwardMessageExceptMe(id, jsonObject.toString());
        System.out.println("用户断开连接: " + id);
    }

    @Override
    public void onException(WsContent ctx, Exception ex) {

    }


}
