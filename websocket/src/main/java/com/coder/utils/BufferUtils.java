package com.coder.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.coder.utils.BinaryUtils.bytes2hexStr;

/**
 * buffer 工具类
 */
public class BufferUtils {

    // 从通道读取数据 最大 1M
    public static ByteBuffer channleToBuffer(SocketChannel socketChannel) throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 100);
        while (true) {
            int read = socketChannel.read(buffer);
            System.out.println("读取到数据： " + read);
            if (read <= 0) {
                break;
            }
        }
        buffer.flip();
        return buffer;
    }

    public ByteBuffer decode(Object msg)  {
        try {
            ByteBuffer byteBuffer = (ByteBuffer) msg;
            if (byteBuffer.hasRemaining()) {
                int opcode = byteBuffer.get() & 0x0f;

                // 8表示客户端关闭了连接
                if (opcode == 8) {
                    return null;
                }
                int len = byteBuffer.get();
                len &= 0x7f;

                // 126
                if (len == 126) {
                    byte[] tmp = new byte[2];
                    byteBuffer.get(tmp);

                    String s = bytes2hexStr(tmp);
                    len = Integer.parseInt(s, 16);
                }

                if (len == 127) {
                    byte[] tmp = new byte[8];
                    byteBuffer.get(tmp);

                    String s = bytes2hexStr(tmp);
                    len = Integer.parseInt(s, 16);
                }

                byte[] mask = new byte[4];
                byteBuffer.get(mask);

                // 加密
//                byte[] payload = new byte[len];
//                for (int i = 0; i < payload.length; i++) {
//                    payload[i] = (byte) (byteBuffer.get() ^ mask[i % 4]);
//                }
//                if (len >= 65535) {
//                    ByteBuffer allocate = ByteBuffer.allocate(1024 * 1024);
//                    for (int i = 0; i < len; i++) {
//                        allocate.put((byte) (byteBuffer.get() ^ mask[i % 4]));
//                    }
//                    return allocate;
//                }

                ByteBuffer allocate = ByteBuffer.allocate(1024 * 1024);
                for (int i = 0; i < len; i++) {
                    allocate.put((byte) (byteBuffer.get() ^ mask[i % 4]));
                }
                return allocate;
            } else  {
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 写数据到通道
    public static void writeToChannel(ByteBuffer buffer, SocketChannel channel) throws IOException {
        while (buffer.hasRemaining()) {
            int write = channel.write(buffer);
            System.out.println("写入数据： " + write);
        }
    }

    // buffer -> byte[]
    public static byte[] bufferToByte(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

}
