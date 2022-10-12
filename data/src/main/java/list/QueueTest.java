package list;

public class QueueTest {
    public static void main(String[] args) {

        Queue<Integer> integerQueue = new Queue<>();
        integerQueue.enQueue(1);
        integerQueue.enQueue(2);
        integerQueue.enQueue(3);
        integerQueue.enQueue(4);
        integerQueue.enQueue(5);

//        System.out.println(integerQueue.font());
//        System.out.println(integerQueue.font());

        while (!integerQueue.isEmpty()) {
            System.out.print(integerQueue.deQueue() +  " ");
        }

        integerQueue.clear();
        System.out.println(integerQueue.isEmpty());


    }
}
