package Util;

import java.sql.Date;

public class TestTime {

    public static void testTime(testTimeFun test){
        long time1 = System.currentTimeMillis();
        test.test();
        long time2 = System.currentTimeMillis();
        System.out.println("[" + Class.class.getName() + "]");
        System.out.println("开始: " + new Date(time1));
        System.out.println("结束: " + new Date(time2));
        System.out.println("耗时: " + ((time2 - time1)/1000.0) + "秒");
        System.out.println("---------------------------------------------------------");
    }

}
