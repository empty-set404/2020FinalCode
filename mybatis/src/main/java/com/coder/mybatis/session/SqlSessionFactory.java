package com.coder.mybatis.session;

import com.coder.mybatis.executor.DefaultExecutor;
import com.coder.mybatis.mapping.Configuration;

public class SqlSessionFactory {

    private Configuration configuration;

    public SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new SqlSession(this.configuration, new DefaultExecutor(this.configuration));
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
