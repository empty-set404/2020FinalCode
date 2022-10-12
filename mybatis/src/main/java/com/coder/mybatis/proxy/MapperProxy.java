package com.coder.mybatis.proxy;

import com.coder.mybatis.annotation.Delete;
import com.coder.mybatis.annotation.Insert;
import com.coder.mybatis.annotation.Select;
import com.coder.mybatis.annotation.Update;
import com.coder.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

public class MapperProxy implements InvocationHandler {

    private SqlSession session;

    public MapperProxy(SqlSession sqlSession) {
        this.session = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String statementKey = method.getDeclaringClass().getName() + "." + method.getName();

        // 判断注解
        if (method.isAnnotationPresent(Insert.class)) {
            return this.session.insert(statementKey, args);
        }

        if (method.isAnnotationPresent(Update.class)) {
            return this.session.update(statementKey, args);
        }

        if (method.isAnnotationPresent(Delete.class)) {
            return this.session.delete(statementKey, args);
        }

        if (method.isAnnotationPresent(Select.class)) {
            if (Collection.class.isAssignableFrom(method.getReturnType())) {
                return this.session.selectList(statementKey, args);
            }else{
                return this.session.selectOne(statementKey, args);
            }
        }

        // 调用方法
        return method.invoke(this, args);
    }
}
