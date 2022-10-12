package com.coder.mybatis.parameter;

/**
 * 参数解析接口
 */
public interface ParameterHandler {

    // 参数解析
    public void parseParameter(Object[] parameters);

}
