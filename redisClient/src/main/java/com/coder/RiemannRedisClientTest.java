package com.coder;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试类
 */
public class RiemannRedisClientTest {
    public static void main(String[] args) {
        RiemannRedisClient client = new RiemannRedisClient("localhost", 6379);
        String ping = client.ping();
        System.out.println(ping);

        System.out.println(client.set("username", "coder"));
        System.out.println(client.get("username"));
        System.out.println(client.lpush("list", "admin"));

    }
}
