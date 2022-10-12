package com.coder.mybatis.mapping;

import lombok.Data;

import java.util.Map;

@Data
public class Configuration {

    private Environment environment;

    private Map<String, MapperStatement> mapperStatementMap;


}
