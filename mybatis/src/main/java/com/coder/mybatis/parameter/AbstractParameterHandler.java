package com.coder.mybatis.parameter;

import java.sql.PreparedStatement;

public abstract class AbstractParameterHandler implements ParameterHandler{

    protected PreparedStatement preparedStatement;

    public AbstractParameterHandler(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }


}
