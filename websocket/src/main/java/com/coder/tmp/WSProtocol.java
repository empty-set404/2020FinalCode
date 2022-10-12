package com.coder.tmp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class WSProtocol {

    public static class Header {
        private HashMap<String, String> headers;

        public String getHeaders(String key) {
            return headers.get(key);
        }

        /**
         * GET /webfin/websocket/ HTTP/1.1
         * Host: localhost
         * Upgrade: websocket
         * Connection: Upgrade
         * Sec-WebSocket-Key: xqBt3ImNzJbYqRINxEFlkg==
         * Origin: http://localhost:8080
         * Sec-WebSocket-Version: 13
         */
        public static Header decodeFromString(String headers) {
            final Header header = new Header();

            final HashMap<String, String> headerMap = new HashMap<>();
            final String[] headerLines = headers.split("\r\n");
            for (String headerLine : headerLines) {
                if (headerLine.contains(":")) {
                    String[] split = headerLine.split(":");
                    String key = split[0].trim();
                    String value = split[1].trim();
                    headerMap.put(key, value);
                }
            }
            header.headers = headerMap;
            return header;
        }
    }

    /**
     *
     * HTTP/1.1 101 Switching Protocols
     * Upgrade: websocket
     * Connection: Upgrade
     * Sec-WebSocket-Accept: K7DJLdLooIwIG/MOpvWFB3y3FE8=
     *
     * Sec-WebSocket-Accept字段是对请求报文中Sec-WebSocket-Key字段进行摘要运算的结果。其运算过程如下
     * 1、将Sec-WebSocket-Key字段的值与字符串258EAFA5-E914-47DA-95CA-C5AB0DC85B11拼接。
     * 2、对拼接后的字符串进行sha1运算，得到160位摘要（二进制）。
     * 3、以base64的形式表示得到的摘要。
     */
    public static String getHandShakeResponse(String receiveKey) throws NoSuchAlgorithmException {
        String keyOrigin = receiveKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        MessageDigest sha1;
        String accept = null;

        sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update(keyOrigin.getBytes());
        accept = new String(Base64.getEncoder().encode(sha1.digest()));

        String echoHeader = "";
        echoHeader += "HTTP/1.1 101 Switching Protocols\r\n";
        echoHeader += "Upgrade: websocket\r\n";
        echoHeader += "Connection: Upgrade\r\n";
        echoHeader += "Sec-WebSocket-Accept: " + accept + "\r\n";
//        echoHeader += "Content-Type: text/html;charset=utf-8\r\n";
        echoHeader += "\r\n";
        return echoHeader;
    }


}

