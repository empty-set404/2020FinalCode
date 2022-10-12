package builder;

import config.TomcatConfig;
import entity.HttpServletRequest;
import entity.HttpServletResponse;
import proccess.TomcatProccess;

import java.io.IOException;

/**
 * 模板方法
 */
public abstract class HttpBuilder {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected String splitFlag = "\r\n";

    public abstract void buildRequestHeader();

    protected abstract void buildRequest() throws Exception;

    protected abstract void flush() throws IOException;

    public void builder() {
        try {
            buildRequest();
            buildRequestHeader();
            this.response = new HttpServletResponse();
            TomcatProccess.doService(this);
            buildResponse();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildResponse() throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(response.getHttpCode(), response.getOutputStream().toByteArray());
    }

    public void buildResponse(byte[] data) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(response.getHttpCode(), data);
    }

    public void buildResponse(int httpCode, String msg) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }
        buildResponse(response.getHttpCode(), msg.getBytes(TomcatConfig.ENCODE));
    }

    /**
     * HTTP/1.1 200 OK
     Cache-Control: private
     Connection: keep-alive
     Content-Encoding: gzip
     Content-Type: text/html;charset=utf-8
     Date: Sat, 26 Jun 2021 06:40:01 GMT
     Expires: Sat, 26 Jun 2021 06:40:01 GMT
     Server: BWS/1.0
     Vary: Accept-Encoding
     Content-Length: 78
     */
    public void buildResponse(int httpCode, byte[] data) throws IOException {
        if (response == null) {
            response = new HttpServletResponse();
        }

        // 创建响应头
        buildResponseHeader();

        // 获取 Content-Length
        Integer contentLength = 0;
        if (data != null) {
            contentLength = data.length;
        }
        response.setHeader("Content-Length", contentLength.toString());

        // 构建响应头
        StringBuilder responseHeader = new StringBuilder().append("HTTP/1.1 ").append(httpCode).append(" ").append(splitFlag);
        for (String key : response.getHeader().keySet()) {
            for (String value : response.getHeader().get(key)) {
                responseHeader.append(key).append(": ").append(value).append(splitFlag);
            }
        }

        // 请求空行
        responseHeader.append(splitFlag);

        // 写入输入流中
        response.getOutputStream().reset();
        response.getOutputStream().write(responseHeader.toString().getBytes(TomcatConfig.ENCODE));
        if (data != null) {
            response.getOutputStream().write(data);
        }
    }

    public void buildResponseHeader() {
        if (response == null) {
            throw new RuntimeException("Response尚未初始化");
        }
        // 长连接或短链接
        response.setHeader("Connection", "close");
        response.setHeader("Server", "Tomcat/1.0 By coder");
        if (!response.containsHeader("Content-Type")) {
            response.setHeader("Content-Type", "text/html;charset=" + TomcatConfig.ENCODE.toLowerCase());
        }
    }

    /**
     * 关闭流
     */
    private void destroy() {
        if (request != null && request.getInputStream() != null) {
            try {
                request.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (response != null && response.getOutputStream() != null) {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void flushAndClose() {
        try {
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            destroy();
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
