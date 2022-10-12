package com.coder.mybatis.mapping;

import lombok.Data;

@Data
public class MapperStatement {

    private String namespace;

    private String id;

    private String parameterType;

    private String resultType;

    private String sql;

}
