package com.coder.mybatis.session;

import com.coder.mybatis.executor.Executor;
import com.coder.mybatis.mapping.Configuration;
import com.coder.mybatis.mapping.MapperStatement;
import com.coder.mybatis.proxy.MapperProxy;
import java.lang.reflect.Proxy;
import java.util.List;

public class SqlSession {

    private Configuration configuration;

    private Executor executor;

    public SqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader()
                , new Class[]{clazz}
                , new MapperProxy(this));
    }

    public int insert(String statementKey, Object[] args) {
        MapperStatement mapperStatement = this.configuration.getMapperStatementMap().get(statementKey);
        return this.executor.insert(mapperStatement, args);
    }

    public int delete(String statementKey, Object[] args) {
        MapperStatement mapperStatement = this.configuration.getMapperStatementMap().get(statementKey);
        return this.executor.delete(mapperStatement, args);
    }

    public int update(String statementKey, Object[] args) {
        MapperStatement mapperStatement = this.configuration.getMapperStatementMap().get(statementKey);
        return this.executor.update(mapperStatement, args);
    }

    public <T> T selectOne(String statementKey, Object[] args) {
        MapperStatement mapperStatement = this.configuration.getMapperStatementMap().get(statementKey);
        return this.executor.selectOne(mapperStatement, args);
    }

    public <T> List<T> selectList(String statementKey, Object[] args) {
        MapperStatement mapperStatement = this.configuration.getMapperStatementMap().get(statementKey);
        return this.executor.selectList(mapperStatement, args);
    }

}
