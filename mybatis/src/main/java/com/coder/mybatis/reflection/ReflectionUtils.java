package com.coder.mybatis.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    /**
     * setter 注入
     */
    public static <T> void setter(T target, String filedName, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
        Class<T> clazz = (Class<T>) target.getClass();
        if (checkField(clazz, filedName)) {
            Field field = clazz.getDeclaredField(filedName);
            String methodName = "set" + toUpperCase(filedName);
            Method method = clazz.getDeclaredMethod(methodName, field.getType());
            method.setAccessible(true);
            method.invoke(target, value);
        }
    }


    // 首字母转大写
    private static String toUpperCase(String name) {
       return name.substring(0 , 1).toUpperCase() + name.substring(1);
    }

    /**
     * 判断方法有对应的字段
     */
    private static boolean checkField(Class<?> clazz, String field) {
        try {
            clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            return false;
        }
        return true;
    }

}
