package map;

public class MapTest {
    public static void main(String[] args) {
        Map<Integer, Integer> treeMap = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            treeMap.put(i, i + 1);
        }
        System.out.println(treeMap.size());

        for (int i = 99; i >= 0; i--) {
            System.out.print(treeMap.get(i) + " ");
        }
        System.out.println();
        System.out.println(treeMap.size());

        System.out.println(treeMap.containsKey(55));
        System.out.println(treeMap.containsValue(99));
        System.out.println(treeMap.containsValue(102));

//        for (int i = 0; i < 100; i++) {
//            treeMap.remove(i);
//        }
//
//        System.out.println(treeMap.size());

        treeMap.remove(55);
        System.out.println(treeMap.get(55));


    }
}
