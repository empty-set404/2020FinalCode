package BloomFilter;

/**
 * 布隆过滤器
 * @param <T>
 */
public class BloomFilter<T> {

    private int bitSize;  //　二进制向量长度
    private long[] bits;
    private int hashSize;

    public BloomFilter(int n, double p) {
        if (n <= 0 || p <= 0 || p >= 1) {
            throw new IllegalArgumentException("wrong n or p");
        }

        double ln2 = Math.log(2);
        this.bitSize = (int) (-(n * Math.log(p) / (ln2 * ln2)));
        this.hashSize = (int) (ln2 * (this.bitSize / n));

        bits = new long[((this.bitSize + Long.SIZE - 1) / Long.SIZE)];

    }

    public boolean put(T value) {
        check(value);

        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;
        boolean result = false;
        for (int i = 1; i <= this.hashSize; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            int index = combinedHash % bitSize;
            if (set(index)) result = true;
        }
        return result;
    }

    public boolean contains(T value) {
        check(value);

        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;
        for (int i = 1; i <= this.hashSize; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            int index = combinedHash % bitSize;
            if (!get(index)) return false;
        }
        return true;
    }

    private void check(T value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
    }

    private boolean set(int bit) {
        long value = this.bits[bit / Long.SIZE];
        int indexValue = 1 << bit % Long.SIZE;
        this.bits[bit / Long.SIZE] = value | indexValue;
        return (value & indexValue) == 0;
    }

    private boolean get(int bit) {
        long value = this.bits[bit / Long.SIZE];
        long indexValule = 1 << (bit % Long.SIZE);
        return (value & indexValule) != 0;
    }

}

