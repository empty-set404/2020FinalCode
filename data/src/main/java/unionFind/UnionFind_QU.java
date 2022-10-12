package unionFind;

public class UnionFind_QU extends UnionFind{

    public UnionFind_QU(int capacity) {
        super(capacity);
    }

    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;
        parents[p1] = p2;
    }

    @Override
    public int find(int v) {
        int parent = v;
        while (parent != parents[parent]) {
            parent = parents[parent];
        }
        return parent;
    }

    

}
