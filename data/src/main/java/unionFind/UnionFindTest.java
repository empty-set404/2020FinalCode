package unionFind;

import Util.TestTime;

public class UnionFindTest {

    private static int count = 10000000;

    public static void main(String[] args) {

        UnionFind_QU unionFind_qu = new UnionFind_QU(count);
        UnionFind_QU_S unionFind_qu_s = new UnionFind_QU_S(count);
        UnionFind_QU_R unionFind_qu_r = new UnionFind_QU_R(count);
        UnionFind_QU_PC unionFind_qu_pc = new UnionFind_QU_PC(count);
        UnionFind_QU_PS unionFind_qu_ps = new UnionFind_QU_PS(count);
        UnionFind_QU_PH unionFind_qu_ph = new UnionFind_QU_PH(count);

//        times(unionFind_qu_s);
//        times(unionFind_qu);
//        times(unionFind_qu_r);
//        times(unionFind_qu_pc);
//        times(unionFind_qu_ps);

        test(unionFind_qu_ph);


    }

    static void times(UnionFind uf) {
        TestTime.testTime(() -> {
            for (int i = 0; i < count; i++) {
                uf.union((int)Math.random()*count, (int)Math.random()*count);
            }

            for (int i = 0; i < count; i++) {
                uf.isSame((int)Math.random()*count, (int)Math.random()*count);
            }

        });
    }

    static void test(UnionFind unionFind_qu) {
        unionFind_qu.union(0, 4);
        unionFind_qu.union(0, 1);
        unionFind_qu.union(0, 3);
        unionFind_qu.union(3, 2);
        unionFind_qu.union(2, 5);

        unionFind_qu.union(6,7);

        unionFind_qu.union(8, 9);
        unionFind_qu.union(8, 10);
        unionFind_qu.union(9, 11);

        System.out.println(unionFind_qu.isSame(1, 2));
        System.out.println(unionFind_qu.isSame(0, 5));

        System.out.println(unionFind_qu.isSame(0, 6));
        unionFind_qu.union(4,7);
        System.out.println(unionFind_qu.isSame(0, 6));

        System.out.println(unionFind_qu.isSame(9, 10));
    }
}
