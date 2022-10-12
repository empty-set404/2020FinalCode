package list;

public class LinkedList3Test {
    public static void main(String[] args) {

        LinkedList3<Integer> integerLinkedList3 = new LinkedList3<>();
        integerLinkedList3.add(0, 1);
        integerLinkedList3.add(1, 2);
        integerLinkedList3.add(2, 3);
        integerLinkedList3.add(3, 4);
        integerLinkedList3.add(null);

        System.out.println(integerLinkedList3.indexOf(3));
        System.out.println(integerLinkedList3.indexOf(null));


        System.out.println(integerLinkedList3);

    }
}
