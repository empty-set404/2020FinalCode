package com.coder.tmp;//package com.coder;
//
//import com.coder.core.session.ClientSession;
//import com.coder.core.session.WebSocketSession;
//import com.coder.utils.ByteUtils;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.*;
//import java.util.Iterator;
//
//public class WebSocketServer {
//
//    private Selector selector;
//    private WebSocketSession webSocketSession;
//    private boolean isRunning = true;
//
//    public WebSocketServer(int port, WebSocketSession webSocketSession) throws IOException {
//        this.webSocketSession = webSocketSession;
//
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        serverSocketChannel.bind(new InetSocketAddress(port));
//        serverSocketChannel.configureBlocking(false);
//        serverSocketChannel.socket().setSoTimeout(5000);
//
//        selector = Selector.open();
//
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//    }
//
//    public void run() throws IOException {
//        while (isRunning) {
//            int selectCount = selector.select();
//            if (selectCount == 0) continue;
//
//            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//            while (iterator.hasNext()) {
//                SelectionKey next = iterator.next();
//                if (next.isAcceptable()) {
//                    System.out.println("连接事件 -------");
//                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
//                    SocketChannel socketChannel = channel.accept();
//                    socketChannel.configureBlocking(false);
//                    socketChannel.register(selector, SelectionKey.OP_READ);
//                }else if (next.isReadable()) {
//                    System.out.println("读取事件 -------");
//                    try {
//                        SocketChannel channel = (SocketChannel) next.channel();
//                        ClientSession session = (ClientSession) next.attachment();
//
//                        if (session == null) {
//                            // 从通道读取数据
//                            String content = ByteUtils.getBytes(channel);
//                            System.out.println(content);
//
//                            // 封装协议
//                            WSProtocol.Header header = WSProtocol.Header.decodeFromString(content);
//                            String key = header.getHeaders("Sec-WebSocket-Key");
//                            String response = WSProtocol.getHandShakeResponse(key);
//
//                            System.out.println("response------------");
//                            System.out.println(response);
//
//                            // 写入通道
//                            channel.write(ByteBuffer.wrap(response.getBytes()));
//
//                            ClientSession clientSession = new ClientSession(channel);
//                            next.attach(clientSession);
//                            webSocketSession.onOpen(clientSession);
//                        }else {
//                            webSocketSession.onMessage(session);
//                        }
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                        next.channel().close();
//                        next.cancel();
//
//                        ClientSession attSession = (ClientSession) next.attachment();
//                        webSocketSession.onException(attSession, e);
//                        webSocketSession.onClose(attSession);
//                    }
//                }
//                iterator.remove();
//            }
//
//        }
//
//
//    }
//
//}
