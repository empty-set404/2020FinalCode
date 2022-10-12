package cn.coder.spring.core.io;

import cn.coder.spring.bean.BeansException;

import java.io.InputStream;
import java.net.URL;

public class UrlResource implements Resource {

    public UrlResource(URL url) {

    }

    public InputStream getInputStream() throws BeansException {
        return null;
    }
}
