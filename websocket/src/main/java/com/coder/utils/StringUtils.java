package com.coder.utils;

/**
 * 字符串处理类
 */
public class StringUtils {

    // 用0填充十六位
    public static String paddingTo16(String hexLength) {
        if (hexLength.length() >= 16) return hexLength;
        int n = 16 - hexLength.length();
        for (int i = 0; i < n; i++) {
            hexLength = "0" + hexLength;
        }
        return hexLength;
    }

    // 用0填充十六位
    public static String paddingTo4(String hexLength) {
        if (hexLength.length() >= 4) return hexLength;
        int n = 4 - hexLength.length();
        for (int i = 0; i < n; i++) {
            hexLength = "0" + hexLength;
        }
        return hexLength;
    }

    // 判空
    public static boolean isEmpty(String content) {
        return "".equals(content);
    }

    public static boolean isNotEmpty(String content) {
        return !"".equals(content);
    }

}
