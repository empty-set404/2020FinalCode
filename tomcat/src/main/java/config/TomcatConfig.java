package config;

public class TomcatConfig {

    /**
     * HTTP线程数量
     */
    public static Integer HTTP_THREAD_NUM = 5;

    /**
     * 默认端口
     */
    public static Integer HTTP_PORT = 8090;

    /**
     * HttpSocket超时时间
     */
    public static Integer HTTP_SO_TIMEOUT = 3000;

    /**
     * 全局编码
     */
    public static String ENCODE = "UTF-8";

    /**
     * 最大Head长度
     */
    public static Integer MAX_HEADER_LENGTH = 8192;

    /**
     * 首页
     */
    public static String WELCOME_PATH = "/index.html";

}
