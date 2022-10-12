package utils;

import config.TomcatConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class ByteUtils {

    /**
     * 读取通道length个字节 并返回
     * @param channel
     * @param length
     * @return
     */
    public static byte[] getBytes(SocketChannel channel, Integer length) {
        if (length < 1) {
            return null;
        }
        int company = 1024;
        if (length < company) {
            company = length;
        }
        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            ByteBuffer buff = ByteBuffer.allocate(company);
            int totalReadLength = 0;
            while (totalReadLength < length) {
                try {
                    int rcLength = channel.read(buff);
                    if (rcLength == 0) {
                        TimeUnit.MICROSECONDS.sleep(1);
                    }
                    totalReadLength += rcLength;
                    buff.flip();
                    byte[] data = new byte[buff.remaining()];
                    buff.get(data);
                    swapStream.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    buff.flip();
                    buff.clear();
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
        int company = TomcatConfig.MAX_HEADER_LENGTH;
        if (length < company) {
            company = length;
        }
        ByteArrayOutputStream swapStream = null;
        try {
            swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[company];
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
