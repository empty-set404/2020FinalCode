package com.coder.mybatis.parameter;

import java.sql.PreparedStatement;

public class DefaultParameterHandler extends AbstractParameterHandler {

    public DefaultParameterHandler(PreparedStatement preparedStatement) {
        super(preparedStatement);
    }

    @Override
    public void parseParameter(Object[] parameters) {
        try {
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i] instanceof Integer) {
                        this.preparedStatement.setInt(i + 1, (Integer) parameters[i]);
                    }else if(parameters[i] instanceof Long) {
                        this.preparedStatement.setLong(i + 1, (Long) parameters[i]);
                    }else if(parameters[i] instanceof String) {
                        this.preparedStatement.setString(i + 1, String.valueOf(parameters[i]));
                    }else if(parameters[i] instanceof Boolean) {
                        this.preparedStatement.setBoolean(i + 1, (Boolean) parameters[i]);
                    }else {
                        this.preparedStatement.setString(i + 1, String.valueOf(parameters[i]));
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
