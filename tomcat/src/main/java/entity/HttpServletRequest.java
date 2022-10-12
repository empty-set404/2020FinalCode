package entity;

import config.TomcatConfig;
import parameter.ParamentAdapt;
import utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServletRequest {

    private String method = "GET";

    private String protocol;

    private String requestURI;

    private String requestURL;

    private Map<String, String> header;

    private ByteArrayInputStream inputStream;

    private String basePath;

    private String scheme = (TomcatConfig.HTTP_PORT == 443 ? "https" : "http");

    private Map<String, List<Object>> params;

    private String queryString = "";

    private Integer contextLength = 0;

    public Object getParam(String name) {
        initParams();
        if (params.containsKey(name)) {
            return params.get(name).get(0);
        }
        return null;
    }

    public Map<String, List<Object>> getParams() {
        // 获取参数
        initParams();
        return params;
    }

    public void setParams(String name, Object value) {
        if (params == null) {
            this.params = new HashMap<>();
        }
        List<Object> list = this.params.get(name);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        this.params.put(name, list);
    }

    private void initParams() {
        // 获取get参数
        params = ParamentAdapt.buildGeneralParams(queryString);

        // 获取json请求体中的参数
        if (header.containsKey("Content-Type") && header.get("Content-Type").contains("application/json")) {
            byte[] bytes = ByteUtils.getBytes(inputStream, contextLength);
            if (bytes != null) {
                params = ParamentAdapt.buildJsonParams(new String(bytes));
            }
        }
    }

    public String getHeader(String name) {
        if (header == null) {
            header = new HashMap<>();
        }
        return header.get(name);
    }

    public void setHeader(String name, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        this.header.put(name, value);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }


    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Integer getContextLength() {
        return contextLength;
    }

    public void setContextLength(Integer contextLength) {
        this.contextLength = contextLength;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
