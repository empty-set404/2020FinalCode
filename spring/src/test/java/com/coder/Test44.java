package com.coder;

import cn.coder.spring.context.support.ClassPathXmlApplicationContext;
import com.coder.bean.UserTest4;
import org.junit.Test;

public class Test44 {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        UserTest4 bean = applicationContext.getBean("userTest4", UserTest4.class);
        System.out.println(bean);
    }

}
