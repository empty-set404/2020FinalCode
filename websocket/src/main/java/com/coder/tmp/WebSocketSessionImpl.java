package com.coder.tmp;//package com.coder;
//
//import com.coder.core.session.ClientSession;
//import com.coder.core.session.WebSocketSession;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import static com.coder.utils.BinaryUtils.bytes2hexStr;
//import static com.coder.utils.BinaryUtils.hexStr2bytes;
//import static com.coder.utils.BufferUtils.channleToBuffer;
//import static com.coder.utils.BufferUtils.writeToChannel;
//import static com.coder.utils.StringUtils.paddingTo16;
//
//public class WebSocketSessionImpl implements WebSocketSession {
//
//    private Map<String, ClientSession> connSessionMap = new HashMap<>();
//
//    @Override
//    public void onOpen(ClientSession session) throws IOException {
//        connSessionMap.put(session.getSessionID(), session);
//        sendBoardCast(session.getChannel().socket().getInetAddress().getHostName() + ":" +
//                session.getChannel().socket().getPort() + " Join", session);
//        System.out.println(session.getChannel().socket().getInetAddress() + ":" +
//                session.getChannel().socket().getPort() + " Join");
//    }
//
//    @Override
//    public void onMessage(ClientSession session) throws IOException {
//        SocketChannel sc = session.getChannel();
//        ByteBuffer byteBuffer = channleToBuffer(sc);
//        try {
//            if (byteBuffer.hasRemaining()) {
//                int opcode = byteBuffer.get() & 0x0f;
//
//                // 8表示客户端关闭了连接
//                if (opcode == 8) {
//                    System.out.println(String.format("[server] -- client %s connection close.", sc.getRemoteAddress()));
//                    return;
//                }
//                int len = byteBuffer.get();
//                len &= 0x7f;
//
//                // 126
//                if (len == 126) {
//                    byte[] tmp = new byte[2];
//                    byteBuffer.get(tmp);
//
//                    String s = bytes2hexStr(tmp);
//                    len = Integer.parseInt(s, 16);
//                }
//
//                if (len == 127) {
//                    byte[] tmp = new byte[8];
//                    byteBuffer.get(tmp);
//
//                    String s = bytes2hexStr(tmp);
//                    len = Integer.parseInt(s, 16);
//                }
//
//                byte[] mask = new byte[4];
//                byteBuffer.get(mask);
//
//                byte[] payload = new byte[len];
//                for (int i = 0; i < payload.length; i++) {
//                    payload[i] = (byte) (byteBuffer.get() ^ mask[i % 4]);
//                }
//
//                System.out.println(String.format("[server] -- client: [%s], send: [%s].", "client.toString()", new String(payload)));
//                sendBoardCast(new String(payload), session);
//            } else  {
//                System.out.println(String.format("[server] -- client %s connection close.", sc.getRemoteAddress()));
//            }
//        } catch (IOException e) {
//            System.out.println(String.format("[server] -- error occur when read: %s.", e.getMessage()));
//        }
//
//    }
//
//    @Override
//    public void onException(ClientSession session, Exception ex) {
//        System.out.println("exception catch: " + ex.getMessage());
//    }
//
//    @Override
//    public void onClose(ClientSession session) throws IOException {
//        connSessionMap.remove(session.getSessionID());
//
//        sendBoardCast(session.getChannel().socket().getInetAddress().getHostName() + ":" +
//                session.getChannel().socket().getPort() + " Leave", session);
//
//        System.out.println("closed sessionId = " + session.getSessionID());
//    }
//
//    private void sendBoardCast(String message, ClientSession ownSession) throws IOException {
//        Iterator<ClientSession> iterator = connSessionMap.values().iterator();
//
//        while (iterator.hasNext()) {
//            ClientSession nextSession = iterator.next();
//            if (nextSession == ownSession) {
//                continue;
//            }
//
//            SocketChannel channel = nextSession.getChannel();
//
//            if (message.length() < 126) {
//                byte[] boardCastData = new byte[2];
//                boardCastData[0] = (byte) 0x81;
//                boardCastData[1] = (byte) message.getBytes().length;
//
//                ByteBuffer wrap = ByteBuffer.wrap(boardCastData);
//                channel.write(wrap);
//            }else if (message.length() <= 65536) {
//                byte[] boardCastData = new byte[4];
//                boardCastData[0] = (byte) 0x81;
//                boardCastData[1] = (byte) 0x7e;
//
//                String hexLength = Integer.toHexString(message.length());
//                byte[] bytes = hexStr2bytes(hexLength);
//                boardCastData[2] = bytes[0];
//                boardCastData[3] = bytes[1];
//
//                ByteBuffer wrap = ByteBuffer.wrap(boardCastData);
//                channel.write(wrap);
//            }else {
//                byte[] boardCastData = new byte[10];
//                boardCastData[0] = (byte) 0x81;
//                boardCastData[1] = (byte) 0x7f;
//
//                String hexLength = Integer.toHexString(message.length());
//                hexLength = paddingTo16(hexLength);
//                byte[] bytes = hexStr2bytes(hexLength);
//                for (int i = 0; i < bytes.length; i++) {
//                    boardCastData[i + 2] = bytes[i];
//                }
//
//                ByteBuffer wrap = ByteBuffer.wrap(boardCastData);
//                channel.write(wrap);
//            }
//
//            ByteBuffer wrap = ByteBuffer.wrap(message.getBytes());
//            writeToChannel(wrap, channel);
//        }
//    }
//
//    private void sendBoardCast2(ByteBuffer buffer, ClientSession ownSession, byte[] mask) throws IOException {
//        Iterator<ClientSession> iterator = connSessionMap.values().iterator();
//
//        while (iterator.hasNext()) {
//            ClientSession nextSession = iterator.next();
//            if (nextSession == ownSession) {
//                continue;
//            }
//
//            SocketChannel channel = nextSession.getChannel();
//
//            byte[] boardCastData = new byte[10];
//            boardCastData[0] = (byte) 0x81;
//            boardCastData[1] = (byte) 0x7f;
//
//            String hexLength = Integer.toHexString(buffer.limit());
//            hexLength = paddingTo16(hexLength);
//            byte[] bytes = hexStr2bytes(hexLength);
//            for (int i = 0; i < bytes.length; i++) {
//                boardCastData[i + 2] = bytes[i];
//            }
//
//            ByteBuffer wrap = ByteBuffer.wrap(boardCastData);
//            channel.write(wrap);
//
//            ByteBuffer allocate = ByteBuffer.allocate(buffer.limit() - buffer.position());
//            try {
//                int i = 0;
//                while (buffer.hasRemaining()) {
//                    allocate.put((byte) (buffer.get() ^ mask[i % 4]));
//                    i++;
//                }
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//            writeToChannel(allocate, channel);
//        }
//    }
//
//
//}
