package list;

public class LinkedListTest {
    public static void main(String[] args) {

        LinkedList<Integer> integerLinkedList = new LinkedList<>();
        integerLinkedList.add(0, 1);
        integerLinkedList.add(1, 2);
        integerLinkedList.add(2, 3);
        integerLinkedList.add(3, 4);
        integerLinkedList.add(4, 5);

        integerLinkedList.add(7);

        System.out.println(integerLinkedList);


    }
}
