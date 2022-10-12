package entity;

import constant.HttpCode;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServletResponse {

    private Integer httpCode = HttpCode.success;

    private Map<String, List<String>> header;

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public boolean containsHeader(String name) {
        if(header == null){
            return false;
        }
        return header.containsKey(name);
    }

    public List<String> getHead(String name) {
        if (header == null) {
            header = new HashMap<>();
        }
        return header.get(name);
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(String name, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        List<String> list = header.get(name);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        header.put(name, list);
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ByteArrayOutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
