package list;

// 动态数组 实现循环队列
public class CircleQueue {
    public static void main(String[] args) {
        
        int d  = 3;
        int n = 3;
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= i; j++) {
                sum += d*Math.pow(10, j);
            }
        }
        System.out.println(sum);

    }
}
