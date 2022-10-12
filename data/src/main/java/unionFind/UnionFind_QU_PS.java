package unionFind;

/**
 * UnionFind_QU -- 基于rank优化 - 路径分裂(path spliting)
 */
public class UnionFind_QU_PS extends UnionFind_QU_R {

    public UnionFind_QU_PS(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        while (v != parents[v]) {
            v = parents[v];
            parents[v] = parents[parents[v]];
        }
        return v;
    }

}
