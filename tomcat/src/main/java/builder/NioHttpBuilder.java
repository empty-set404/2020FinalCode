package builder;

import config.TomcatConfig;
import entity.HttpServletRequest;
import utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioHttpBuilder extends HttpBuilder {

    private SocketChannel channel;

    public NioHttpBuilder(SocketChannel channel) {
        if (channel == null) {
            throw new RuntimeException("channel create error");
        }
        this.channel = channel;
    }

    @Override
    protected void buildRequest() throws Exception {
        this.request = new HttpServletRequest();
    }

    @Override
    protected void flush() throws IOException {
        byte[] data = response.getOutputStream().toByteArray();
        if (data == null || data.length == 0) {
            return;
        }
        channel.write(ByteBuffer.wrap(data));
    }

    /**
     * 一个典型的http协议
     *
     *GET /home/xman/data/tipspluslist?indextype=manht&_req_seqid=0x82203fe1000c4250&asyn=1&t=1624689601933 HTTP/1.1
     Host: www.baidu.com
     Connection: keep-alive
     sec-ch-ua: " Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"
     Accept: text/plain; q=0.01
     X-Requested-With: XMLHttpRequest
     sec-ch-ua-mobile: ?0
     User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36
     Sec-Fetch-Site: same-origin
     Sec-Fetch-Mode: cors
     Sec-Fetch-Dest: empty
     Referer: https://www.baidu.com/
     Accept-Encoding: gzip, deflate, br
     Accept-Language: zh-CN,zh;q=0.9
     Cookie: BIDUPSID=3D7

     请求体
     *
     */
    @Override
    public void buildRequestHeader() {
        if (request == null) {
            throw new RuntimeException("request not init");
        }

        try {
            // 从通道中读取数据
            ByteBuffer buffer = ByteBuffer.allocateDirect(TomcatConfig.MAX_HEADER_LENGTH);
            int count = channel.read(buffer);
            if (count < 1) {
                throw new RuntimeException("错误的请求报文");
            }

            // 判断是否已经全部读完
            boolean isReadEnd = count < TomcatConfig.MAX_HEADER_LENGTH;

            // Buffer --> String
            buffer.flip();
            byte[] headData = new byte[buffer.remaining()];
            buffer.get(headData, 0, headData.length);
            String headContext = new String(headData, "iso-8859-1");
            String[] headers = headContext.split("\r\n");

            // 分离出 GET /home HTTP/1.1
            String bodyContext = null;
            if (headContext.contains(splitFlag)) {
                bodyContext = headContext.substring(headContext.indexOf(splitFlag) + splitFlag.length());
                headContext = headContext.substring(0, headContext.indexOf(splitFlag));
            }
            headContext += splitFlag;


            // 解析 GET /home HTTP/1.1
            headContext.replaceAll("  ", "");
            String[] content = headContext.split(" ");
            if (content.length < 2) {
                throw new RuntimeException("错误的请求报文");
            }
            request.setMethod(content[0]);

            // 解析 URI 中的get参数
            String URI = content[1];
            if (URI.contains("?")) {
                int index = URI.indexOf("?");
                if (index < URI.length() - 1) {
                    request.setQueryString(URI.substring(index + 1));
                    URI = URI.substring(0, index);
                }
            }
            request.setRequestURI(URI);
            request.setProtocol(content[2]);

            // 解析其余请求头
            for (int i = 1; i < headers.length; i++) {
                int index = headers[i].indexOf(":");
                if (index < 1) {
                    break;
                }
                String name = headers[i].substring(0, index).trim();
                String value = headers[i].substring(index + 1).trim();
                if ("".equals(name) || "".equals(value)) {
                    continue;
                }
                request.setHeader(name, value);

                if (name.equals("Host")) {
                    String basePath = request.getScheme() + "://" + value;
                    if (URI.startsWith(basePath)) {
                        URI = URI.substring(basePath.length());
                        request.setRequestURI(URI);
                    }
                }
                if (name.equals("Content-Length")) {
                    request.setContextLength(Integer.valueOf(value));
                }
            }

            // 将数据写入输入流中
            if (isReadEnd) {
                request.setInputStream(new ByteArrayInputStream(bodyContext.getBytes("iso-8859-1")));
                return;
            }

            // 执行到这里，说明通道还有数据
            byte[] bytes = bodyContext.getBytes("iso-8859-1");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(bytes);
            int remainLength = request.getContextLength() - bytes.length;
            byte[] data = ByteUtils.getBytes(channel, remainLength);
            outputStream.write(data);
            request.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
