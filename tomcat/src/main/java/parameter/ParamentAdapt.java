package parameter;

import config.TomcatConfig;
import utils.StringUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamentAdapt {

    /**
     * 封装get参数
     * @param queryString
     * @return
     */
    public static Map<String, List<Object>> buildGeneralParams(String queryString) {
        if (!StringUtil.isNullOrEmpty(queryString)) {
            return new HashMap<>();
        }
        Map<String, List<Object>> parames = new HashMap<>();
        String[] lines = queryString.split("&");
        for (String line : lines) {
            try {
                int index = line.indexOf("=");
                if (index < 1 && index == line.length() - 1) {
                    continue;
                }
                String name = line.substring(0, index);
                String value = URLDecoder.decode(line.substring(index + 1), TomcatConfig.ENCODE);
                if (!parames.containsKey(name)) {
                    List<Object> list = new ArrayList<>();
                    parames.put(name, list);
                }
                parames.get(name).add(value);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parames;
    }

    /**
     * 封装json参数
     * @param json
     * @return
     */
    public static Map<String, List<Object>> buildJsonParams(String json) {
        return null;
    }


}
