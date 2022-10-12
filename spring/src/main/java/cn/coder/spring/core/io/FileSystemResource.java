package cn.coder.spring.core.io;

import cn.coder.spring.bean.BeansException;

import java.io.InputStream;

public class FileSystemResource implements Resource {

    public FileSystemResource(String location) {

    }

    public InputStream getInputStream() throws BeansException {
        return null;
    }
}
