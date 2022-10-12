package com.coder.mybatis.executor;

import com.coder.mybatis.mapping.Configuration;
import com.coder.mybatis.mapping.MapperStatement;
import com.coder.mybatis.parameter.DefaultParameterHandler;
import com.coder.mybatis.pool.MiniDataSource;
import com.coder.mybatis.reflection.ReflectionUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultExecutor extends AbstractExecutor {

    private MiniDataSource dataSource;

    // 解析sql 正则表达式
    private final String SQLREGEX = "[\\$|\\#]\\{(.*?)\\}";

    public DefaultExecutor(Configuration configuration) {
        this(MiniDataSource.getInstance(configuration.getEnvironment()));
    }

    public DefaultExecutor(MiniDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected int execute(MapperStatement mapperStatement, Object[] args) {
        int executeRows = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.dataSource.getConnection();
            preparedStatement = connection.prepareStatement(parseSql(mapperStatement.getSql()));
            // 设置参数
            DefaultParameterHandler defaultParameterHandler = new DefaultParameterHandler(preparedStatement);
            defaultParameterHandler.parseParameter(args);
            executeRows = preparedStatement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                this.dataSource.releaseConnection(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return executeRows;
    }

    @Override
    protected <T> List<T> query(MapperStatement mapperStatement, Object[] args) {
        List<T> resultList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.dataSource.getConnection();
            preparedStatement = connection.prepareStatement(parseSql(mapperStatement.getSql()));
            // 设置参数
            DefaultParameterHandler defaultParameterHandler = new DefaultParameterHandler(preparedStatement);
            defaultParameterHandler.parseParameter(args);
            resultSet = preparedStatement.executeQuery();
            this.parseResultSet(resultSet, resultList, mapperStatement.getResultType());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                this.dataSource.releaseConnection(connection);
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    // 解析sql
    private String parseSql(String sql) {
        return sql.replaceAll(SQLREGEX, "?");
    }

    // 结果集封装
    private <T> void parseResultSet(ResultSet resultSet, List<T> resultList, String resultType) {
        try {
            Class<?> clazz = Class.forName(resultType);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T bean = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    ReflectionUtils.setter(bean, metaData.getColumnName(i), resultSet.getObject(i));
                }
                resultList.add(bean);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
