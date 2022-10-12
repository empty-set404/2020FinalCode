package unionFind;

public class Main {
    static final int count = 100000;

    public static void main(String[] args) {
//		testTime(new UnionFind_QF(count)); // 太慢了,不测试
//		testTime(new UnionFind_QU(count)); // 太慢了,不测试
        testTime(new UnionFind_QU_S(count));
        testTime(new UnionFind_QU_R(count));
        testTime(new UnionFind_QU_PC(count));
        testTime(new UnionFind_QU_PS(count));
        testTime(new UnionFind_QU_PH(count));
        testTime(new GenericUnionFind<Integer>());
    }

    static void testTime(GenericUnionFind<Integer> uf) {
        for (int i = 0; i < count; i++) {
            uf.makeSet(i);
        }

        Times.test(uf.getClass().getSimpleName(), () -> {
            for (int i = 0; i < count; i++) {
                uf.union((int)(Math.random() * count),
                        (int)(Math.random() * count));
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int)(Math.random() * count),
                        (int)(Math.random() * count));
            }
        });
    }

    static void testTime(UnionFind uf) {
        Times.test(uf.getClass().getSimpleName(), () -> {
            for (int i = 0; i < count; i++) {
                uf.union((int)(Math.random() * count),
                        (int)(Math.random() * count));
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int)(Math.random() * count),
                        (int)(Math.random() * count));
            }
        });
    }

}
