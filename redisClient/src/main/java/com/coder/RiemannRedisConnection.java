package com.coder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 传输层
 */
public class RiemannRedisConnection {

    private Socket socket;

    private String host;

    private Integer port;

    private InputStream inputStream;

    private OutputStream outputStream;

    public RiemannRedisConnection(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public RiemannRedisConnection connection() {
        try {
            this.socket = new Socket(this.host, this.port);
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public RiemannRedisConnection sendCommand(RiemannCommand command, byte[]... args) {
        connection();
        RiemannProtocal.sendCommand(this.outputStream, command, args);
        return this;
    }

    public String response() {
        return RiemannProtocal.response(this.inputStream);
    }

//    public String response() {
//        byte[] bytes = new byte[1024];
//        try {
//            this.inputStream.available();
//            this.inputStream.read(bytes);
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new String(bytes, Charset.defaultCharset());
//    }

}
