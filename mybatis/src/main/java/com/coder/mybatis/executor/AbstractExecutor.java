package com.coder.mybatis.executor;

import com.coder.mybatis.mapping.MapperStatement;

import java.util.List;

/**
 * 模板方法
 */
public abstract class AbstractExecutor implements Executor {

    protected abstract int execute(MapperStatement mapperStatement, Object[] args);

    protected abstract <T> List<T> query(MapperStatement mapperStatement, Object[] args);

    @Override
    public int insert(MapperStatement mapperStatement, Object[] args) {
        return execute(mapperStatement, args);
    }

    @Override
    public int update(MapperStatement mapperStatement, Object[] args) {
        return execute(mapperStatement, args);
    }

    @Override
    public int delete(MapperStatement mapperStatement, Object[] args) {
        return execute(mapperStatement, args);
    }

    @Override
    public <T> T selectOne(MapperStatement mapperStatement, Object[] args) {
        List<T> list = query(mapperStatement, args);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <T> List<T> selectList(MapperStatement mapperStatement, Object[] args) {
        return query(mapperStatement, args);
    }
}
