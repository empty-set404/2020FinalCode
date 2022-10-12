package com.coder.codec;

import com.coder.content.Content;
import com.coder.content.WsContentImpl;

import java.nio.ByteBuffer;

import static com.coder.utils.BinaryUtils.bytes2hexStr;
import static com.coder.utils.BinaryUtils.hexStr2bytes;
import static com.coder.utils.StringUtils.paddingTo16;
import static com.coder.utils.StringUtils.paddingTo4;

public class WebSocketCodecHandler extends CodeHandlerAdapter {

    private static final Integer BLOCK_COUNT = 1024 * 10;

    /**
     * 编码
     * @param content
     * @param msg
     */
    @Override
    public Object encode(Content content, Object msg) {
        ByteBuffer allocate = ByteBuffer.allocate(1024 * 1024);

        if (msg instanceof String) {

            String message = (String) msg;
            int length = message.getBytes().length;

            try {
                if (length <= 126) {
                    byte[] boardCastData = new byte[2];
                    boardCastData[0] = (byte) 0x81;
                    boardCastData[1] = (byte) length;

                    allocate.put(boardCastData);
                }else if (length <= 65536) {
                    byte[] boardCastData = new byte[4];
                    boardCastData[0] = (byte) 0x81;
                    boardCastData[1] = (byte) 0x7e;

                    String hexLength = Integer.toHexString(length);
                    hexLength = paddingTo4(hexLength);
                    byte[] bytes = hexStr2bytes(hexLength);
                    boardCastData[2] = bytes[0];
                    boardCastData[3] = bytes[1];

                    allocate.put(boardCastData);
                }else {
                    byte[] boardCastData = new byte[10];
                    boardCastData[0] = (byte) 0x81;
                    boardCastData[1] = (byte) 0x7f;

                    String hexLength = Integer.toHexString(length);
                    hexLength = paddingTo16(hexLength);
                    byte[] bytes = hexStr2bytes(hexLength);
                    for (int i = 0; i < bytes.length; i++) {
                        boardCastData[i + 2] = bytes[i];
                    }

                    allocate.put(boardCastData);
                }

                allocate.put(message.getBytes());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allocate;
    }

    /**
     * 解码
     * @param content
     * @param msg
     */
    @Override
    public Object decode(Content content, Object msg)  {
        try {
            ByteBuffer byteBuffer = (ByteBuffer) msg;
            if (byteBuffer.hasRemaining()) {
                int opcode = byteBuffer.get() & 0x0f;

                // 8表示客户端关闭了连接
                if (opcode == 8) {
                    System.out.println(String.format("[server] -- client %s connection close.", ((WsContentImpl) content).channel().getRemoteAddress()));
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

                byte[] payload = new byte[BLOCK_COUNT];
                StringBuffer stringBuffer = new StringBuffer(len);
                for (int i = 0; i < len; i++) {
                    if (i != 0 && (i % BLOCK_COUNT) == 0) {
                        stringBuffer.append(new String(payload));
                    }
                    payload[i % BLOCK_COUNT] = (byte) (byteBuffer.get() ^ mask[i % 4]);
                }
                stringBuffer.append(new String(payload, 0, len % BLOCK_COUNT));

//                if (byteBuffer.hasRemaining()) {
//                    decode(content, byteBuffer);
//                }

                System.out.println(String.format("[server] -- client: [%s], send: [%s]", "client.toString()", new String(stringBuffer)));
                System.out.println(new String(stringBuffer).length());
                return new String(stringBuffer);
//                System.out.println(String.format("[server] -- client: [%s], send: [%s]", "client.toString()", new String(payload)));
//                return new String(payload);
            } else  {
                System.out.println(String.format("[server] -- client %s connection close.", ((WsContentImpl) content).channel().getRemoteAddress()));
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
