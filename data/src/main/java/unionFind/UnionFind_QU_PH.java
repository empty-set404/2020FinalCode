package unionFind;

/**
 * UnionFind_QU -- 基于rank优化 - 路径减半(path halving)
 */
public class UnionFind_QU_PH extends UnionFind_QU_R {
    public UnionFind_QU_PH(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        while (v != parents[v]) {
            int tmp = parents[parents[v]];
            parents[v] = tmp;
            v = tmp;
        }
        return v;
    }
}
