package com.coder.mybatis.session;

import com.coder.mybatis.annotation.*;
import com.coder.mybatis.mapping.Configuration;
import com.coder.mybatis.mapping.Environment;
import com.coder.mybatis.mapping.MapperStatement;
import com.coder.mybatis.parsing.XMLConfigBuilder;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlSessionFactoryBuilder {

    private Configuration configuration;

    public SqlSessionFactory build(InputStream inputStream) {
        Configuration configuration = new Configuration();
        try {
            //1.解析mybatis-config.xml
            XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
            xmlConfigBuilder.load(inputStream);
            Environment environment = xmlConfigBuilder.parse();
            configuration.setEnvironment(environment);

            // 2. 读取mapper类路径
            String scanPackage = xmlConfigBuilder.getOne("//configuration//mappers//mapper", "resource");

            // 3. 扫描路径
            List<String> list = new ArrayList<>();
            scanPackage(scanPackage, list);

            Map<String, MapperStatement> mapperStatementMap = new ConcurrentHashMap<>();
            // 4. 注解扫描
            for (String fileName : list) {
                Class<?> clazz = Class.forName(fileName);
                if (!clazz.isAnnotationPresent(Mapper.class)) {
                    continue;
                }

                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Insert.class) || method.isAnnotationPresent(Update.class) || method.isAnnotationPresent(Delete.class) || method.isAnnotationPresent(Select.class)) {
                        final MapperStatement statement = new MapperStatement();
                        statement.setNamespace(clazz.getName());
                        statement.setId(method.getName());

                        //处理Collection
                        if (Collection.class.isAssignableFrom(method.getReturnType())) {
                            final Type type = method.getGenericReturnType();

                            // 断获取的类型是否是参数类型、强制转型为带参数的泛型类型，
                            if (type instanceof ParameterizedType) {
                                final Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                                statement.setResultType(arguments[0].getTypeName());
                            }
                        }else {
                            statement.setResultType(method.getReturnType().getName());
                        }

                        // 处理自定义注解Insert
                        Insert insert = method.getAnnotation(Insert.class);
                        if (insert != null){
                            statement.setSql(insert.value().trim());
                        }

                        // 处理自定义注解Delete
                        Delete delete = method.getAnnotation(Delete.class);
                        if (delete != null){
                            statement.setSql(delete.value().trim());
                        }


                        // 处理自定义注解Update
                        Update update = method.getAnnotation(Update.class);
                        if (update != null){
                            statement.setSql(update.value().trim());
                        }

                        // 处理自定义注解Select
                        Select select = method.getAnnotation(Select.class);
                        if (select != null){
                            statement.setSql(select.value().trim());
                        }

                        mapperStatementMap.putIfAbsent(clazz.getName() + "." + method.getName(), statement);
                    }
                }
                configuration.setMapperStatementMap(mapperStatementMap);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new SqlSessionFactory(configuration);
    }

    private void scanPackage(String scanPackage, List<String> list) {
        URL url = SqlSessionFactoryBuilder.class.getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        File file = new File(url.getFile());
        for (File file1 : file.listFiles()) {
            if (file1.isDirectory()) {
                scanPackage(scanPackage + "." + file1.getName(), list);
            }else {
                if (file1.getName().endsWith(".class")) {
                    list.add(scanPackage + "." + file1.getName().replaceAll(".class", ""));
                }
            }
        }
    }


}
