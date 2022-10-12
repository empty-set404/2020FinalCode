package com.coder.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * 字节处理工具类
 */
public class ByteUtils {

    /**
     * 读取通道length个字节 并返回
     * @param channel
     * @return
     */
    public static String getBytes(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String content = "";
        try {
            int read = 0;
            while ((read = channel.read(buffer)) > 0) {
                buffer.flip();
                byte[] bytes = new byte[read];
                buffer.get(bytes);
                content += new String(bytes);
                // content += StandardCharsets.UTF_8.decode(buffer).toString();
            }
            return content;
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            buffer.clear();
        }
    }

    /**
     * 从输入流中读取length个字节 并返回
     * @param inputStream
     * @param length
     * @return
     */
    public static byte[] getBytes(InputStream inputStream, Integer length) {
        if (length < 1) {
            return null;
        }
        if (inputStream == null) {
            return null;
        }

        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[length];
            int totalReadLength = 0;
            while (totalReadLength < length) {
                int rcLength = inputStream.read(buff);
                if (rcLength == 0) {
                    TimeUnit.MICROSECONDS.sleep(1);
                    break;
                }
                totalReadLength += rcLength;
                swapStream.write(buff, 0, rcLength);
                if (rcLength < buff.length) {
                    break;
                }
            }
            return swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                swapStream.close();
            } catch (IOException e) {
            }
        }
    }

}
