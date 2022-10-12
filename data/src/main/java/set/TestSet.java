package set;

public class TestSet {

    public static void main(String[] args) {
        TreeSet<Integer> list = new TreeSet<>();
        int[] arr = {56, 55, 32, 65, 41, 19, 68, 52, 90, 88, 83, 81, 70, 40, 35, 57, 90};

        for (int i : arr) {
            list.add(i);
        }

        list.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return false;
            }
        });

        System.out.println();
        System.out.println(list.size());
        System.out.println(arr.length);

    }

}
