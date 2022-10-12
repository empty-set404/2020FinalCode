package com.coder.websocket.message;

public class MessageType {

    public static final int LOGIN = 1; // 登录

    public static final int USER_EXIT = 2; // 用户存在

    public static final int NORMAL_MSG = 4; // 普通消息
    public static final int PUBLIC_MSG = 5; // 公共消息
    public static final int GROUP_MSG = 6; // 公共消息
    public static final int ONLINE_SUCCESS = 7; // 公共消息
    public static final int IMAGES_MSG = 8; // 公共消息

    public static final int LOGIN_SUCCEED = 11; // 登录成功
    public static final int LOGIN_FAIL = 12; // 登录失败
    public static final int REG_SUCCEED = 13; // 注册成功
    public static final int REG_FAIL = 14; // 注册失败
    public static final int LOG_EXIST = 15; // 已经被登录
    public static final int ERROR = 20; // 出错

    public static final int USER_LIST = 21; // 用户在线列表
    public static final int ADD_USER = 22; // 用户在线列表
    public static final int LEAVE_USER = 23; // 用户离开

    public static final int SDP = 24; // SDP
    public static final int ICE = 25; // ICE

    public static final String USER_FILE = "user.properties"; // 用户文件路径

    public static final String USERNAME = "username"; // 用户名参数
    public static final String STATE = "state"; // 验证的状态参数

}
