package cn.coder.spring.core.io;

import cn.coder.spring.bean.BeansException;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface Resource {

    InputStream getInputStream() throws BeansException, FileNotFoundException;

}
