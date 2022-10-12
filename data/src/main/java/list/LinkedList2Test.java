package list;

public class LinkedList2Test {
    public static void main(String[] args) {

        LinkedList2<Integer> integerLinkedList2 = new LinkedList2<>();

        integerLinkedList2.add(0,1);
        integerLinkedList2.add(1,2);
        integerLinkedList2.add(2,3);

//        integerLinkedList2.remove(2);

        integerLinkedList2.add(1, 99);

        System.out.println(integerLinkedList2.indexOf(99));

        integerLinkedList2.set(0 ,11);


        System.out.println(integerLinkedList2);


    }
}
