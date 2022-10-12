package utils;

import entity.HttpServletRequest;
import servlet.HttpFilter;

public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return !"".equals(str);
    }

    /**
     * 路径匹配算法 --> 可使用正则表达式优化
     * @param request
     * @param filter
     * @return
     */
    public static boolean isAntMatch(HttpServletRequest request, HttpFilter filter) {
        if (request.getRequestURI() != null) {
            return request.getRequestURI().startsWith(filter.getMapping());
        }
        return false;
    }

    public static boolean hasNull(String name, String value) {
        return !isNullOrEmpty(name) && !isNullOrEmpty(value);
    }
}
