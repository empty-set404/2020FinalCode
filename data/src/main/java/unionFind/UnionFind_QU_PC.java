package unionFind;

/**
 * UnionFind_QU -- 基于rank优化 - 路径压缩(path compression)
 */
public class UnionFind_QU_PC extends UnionFind_QU_R{

    public UnionFind_QU_PC(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        if(v != parents[v]) {
            parents[v] = find(parents[v]);
        }
        return parents[v];
    }
}
