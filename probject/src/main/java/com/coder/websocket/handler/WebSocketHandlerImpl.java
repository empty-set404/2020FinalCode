package com.coder.websocket.handler;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.annotation.Autowired;
import cn.coder.spring.context.ApplicationContext;
import cn.coder.spring.context.ApplicationContextAware;
import cn.coder.spring.context.ApplicationListener;
import cn.coder.spring.context.annotation.Scope;
import cn.coder.spring.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.coder.content.WsContent;
import com.coder.entity.User;
import com.coder.mapper.UserMapper;
import com.coder.service.Impl.UserServiceImpl;
import com.coder.service.UserService;
import com.coder.springmvc.annotation.Service;
import com.coder.websocket.message.MessageType;
import com.coder.websocket.message.MsgBody;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebSocketHandlerImpl implements WebsocketHandler {

    public static Map<User, WsContent> userMap = new HashMap<>();

    private static final int USER_NOTFOUND = -1;

    @Override
    public void onOpen(WsContent ctx) {
        System.out.println("连接成功....");
    }

    @Override
    public void onMessage(WsContent ctx, Object frame) throws IOException {
//        if (((String) frame).contains("data:image") || !((String) frame).contains("type")) {
//            Collection<WsContent> values = userMap.values();
//            for (WsContent value : values) {
//                if (value.channel() != ctx.channel()) {
//                    value.writeAndFlush(frame);
//                }
//            }
//            return;
//        }
        MsgBody msgBody = null;
        try {
            msgBody = JSONObject.parseObject((String) frame, MsgBody.class);
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // login
        if (msgBody.type == MessageType.LOGIN) {
            login(ctx, msgBody);
        }

        // sendMessage
        if (msgBody.type == MessageType.PUBLIC_MSG) {
            sendMessage(ctx, msgBody);
        }

        // 私聊
        if (msgBody.type == MessageType.NORMAL_MSG) {
            WsContent wsContent = userMap.get(JSONObject.parseObject(msgBody.getTo(), User.class));
            System.out.println("私聊信息....");
            System.out.println(JSONObject.parseObject(msgBody.getTo(), User.class).getUsername());
            send(wsContent, msgBody);
            send(ctx, msgBody);
        }

        // sendImage
        if (msgBody.type == MessageType.IMAGES_MSG) {
            sendImage(ctx, msgBody);
        }

        // sdp
        if (msgBody.type == MessageType.SDP) {
            User to = JSONObject.parseObject(msgBody.getTo(), User.class);
            System.out.println("转发sdp " + to.getUsername());
            send(userMap.get(to), msgBody);
        }

        // ice
        if (msgBody.type == MessageType.ICE) {
            User to = JSONObject.parseObject(msgBody.getTo(), User.class);
            System.out.println("转发ice " + to.getUsername());
            send(userMap.get(to), msgBody);
        }
    }

    // { type: type, content: user }
    private void loginSuccess(WsContent ctx, User user) {
        userMap.put(user, ctx);

        MsgBody msgBody = new MsgBody();
        msgBody.setContent(toJson(user));
        msgBody.setType(MessageType.LOGIN_SUCCEED);
        send(ctx, msgBody);

        // 公共在线消息
        MsgBody msgBody2 = new MsgBody();
        msgBody2.setContent(toJson(user));
        msgBody2.setType(MessageType.ADD_USER);
        sendAllExpectedOwn(ctx, msgBody2);

        // 上线用户列表更新
        MsgBody msgBody3 = new MsgBody();
        msgBody3.setContent(toJson(getUserList()));
        msgBody3.setType(MessageType.USER_LIST);
        sendAll(msgBody3);
    }

    // { type: type, content: string }
    private void userExit(WsContent ctx) {
        MsgBody msgBody = new MsgBody();
        msgBody.setContent("请先登录");
        msgBody.setType(MessageType.LOGIN_FAIL);
        send(ctx, toJson(msgBody));
    }

    private void addUser() {

    }

    private void userList() {

    }

    private void leaverRoom(User user) {
        // 上线用户列表更新
        MsgBody msgBody3 = new MsgBody();
        msgBody3.setContent(toJson(user));
        msgBody3.setType(MessageType.LEAVE_USER);
        sendAll(msgBody3);
    }

    // { type: type, content: string }
    private void sendMessage(WsContent ctx, MsgBody msgBody) {
        sendAll(msgBody);
    }

    // { type: type, content: string }
    private void sendImage(WsContent ctx, MsgBody msgBody) {
        sendAll(msgBody);
    }

    // 处理登录 { type: type, content: user }
    private void login(WsContent ctx, MsgBody msgBody) {
        User user = JSONObject.parseObject(msgBody.getContent(), User.class);
        User tmpUser = null;
        for (User user1 : userMap.keySet()) {
            if (user1.getUsername().equals(user.getUsername())) {
                tmpUser = user1;
            }
        }
        if (tmpUser != null) {
            userMap.put(tmpUser, ctx);
            loginSuccess(ctx, tmpUser);
        }else {
            userExit(ctx);
        }
    }

    @Override
    public void onException(WsContent ctx, Exception ex) {

    }

    @Override
    public void onClose(WsContent ctx) throws IOException {
        System.out.println("用户离开");
        User user = null;
        Set<Map.Entry<User, WsContent>> entries = userMap.entrySet();
        for (Map.Entry<User, WsContent> entry : entries) {
            WsContent value = entry.getValue();
            if (value == ctx) {
                user = entry.getKey();
                break;
            }
        }
        userMap.remove(user);
        this.leaverRoom(user);
    }

    private void sendAllExpectedOwn(WsContent ctx, Object data) {
        Collection<WsContent> values = userMap.values();
        for (WsContent value : values) {
            if (value.channel() != ctx.channel()) {
                value.writeAndFlush(toJson(data));
            }
        }
    }

    private void sendAll(Object data) {
        for (WsContent value : userMap.values()) {
            value.writeAndFlush(toJson(data));
        }
    }

    private void send(WsContent ctx, Object data) {
        ctx.writeAndFlush(toJson(data));
    }

    private String toJson(Object data) {
        return JSONObject.toJSONString(data);
    }

    private Set<User> getUserList() {
        return userMap.keySet();
    }

}
