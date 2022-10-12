package BloomFilter;

public class BloomFIlterTest {
    public static void main(String[] args) {

        BloomFilter<Integer> bloomFilter = new BloomFilter<Integer>(10000, 0.01);

        for (int i = 1; i <= 10000; i++) {
            bloomFilter.put(i);
        }

        int count = 0;
        for (int i = 10001; i <= 20000; i++) {
            if (bloomFilter.contains(i)) count++;
        }
        System.out.println(count);
    }
}
