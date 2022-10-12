package com.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 协议层
 */
public class RiemannProtocal {

    private static String DOLLER = "$";
    private static String ALLERSTIC  = "*";
    private static String CRLF  = "\r\n";

    private static String STATUS  = "+";
    private static String ERROR  = "-";
    private static String INTEGER  = ":";
    private static String BULK  = "$";
    private static String MULTIBULK  = "*";

    /**
     * 如SET请求      set riemann 0328
     *
     * *3\r\n        长度为3的数组
     * $3\r\n        第一个字符串长度为3
     * SET\r\n       第一个字符串为SET
     * $7\r\n        第二个字符串长度为7
     * riemann\r\n   第二个字符串为riemann
     * $4\r\n        第三个字符串长度为4
     * 0328\r\n      第三个字符串为0328
     * @param outputStream
     * @param command
     * @param args
     */
    public static void sendCommand(OutputStream outputStream, RiemannCommand command, byte[][] args) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(ALLERSTIC).append(args.length + 1).append(CRLF);
        buffer.append(DOLLER).append(command.name().length()).append(CRLF);
        buffer.append(command.name()).append(CRLF);
        for (byte[] arg : args) {
            buffer.append(DOLLER).append(arg.length).append(CRLF);
            buffer.append(new String(arg)).append(CRLF);
        }
        try {
            outputStream.write(buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回协议
     * 通过检查服务器发回数据的第一个字节， 可以确定这个回复是什么类型：
     * 状态回复（status reply）的第一个字节是 “+”
     * 错误回复（error reply）的第一个字节是 “-”
     * 整数回复（integer reply）的第一个字节是 “:”
     * 批量回复（bulk reply）的第一个字节是 “$”
     * 多条批量回复（multi bulk reply）的第一个字节是 “*”
     * @param inputStream
     * @return
     */
    public static String response(InputStream inputStream) {
        byte[] bytes = new byte[1024];
        try {
            inputStream.available();
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = new String(bytes, Charset.defaultCharset());

        // 状态回复 || 错误回复 || 整数回复
        if (content.startsWith("+") || content.startsWith("-") || content.startsWith(":")) {
            return content.split(CRLF)[0].substring(1);
        }

        // 批量回复
        if (content.startsWith("$")) {
            return content.split(CRLF)[1];
        }

        return content;
    }

}
