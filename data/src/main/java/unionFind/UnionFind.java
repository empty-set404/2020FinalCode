package unionFind;

public abstract class UnionFind {

    protected int[] parents;

    public UnionFind(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        this.parents = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            parents[i] = i;
        }
    }

    protected void rangeCheck(int v) {
        if (v < 0 || v >= parents.length) {
            throw new IndexOutOfBoundsException("index: " + v + " size: " + parents.length);
        }
    }

    public boolean isSame(int v1, int v2) {
        return find(v1) == find(v2);
    }

    public abstract void union(int v1, int v2);

    public abstract int find(int v);

}
