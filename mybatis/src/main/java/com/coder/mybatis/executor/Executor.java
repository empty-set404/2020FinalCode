package com.coder.mybatis.executor;

import com.coder.mybatis.mapping.MapperStatement;

import java.util.List;

public interface Executor {

    public int insert(MapperStatement mapperStatement, Object[] args);

    public int update(MapperStatement mapperStatement, Object[] args);

    public int delete(MapperStatement mapperStatement, Object[] args);

    public <T> T selectOne(MapperStatement mapperStatement, Object[] args);

    public <T> List<T> selectList(MapperStatement mapperStatement, Object[] args);

}
