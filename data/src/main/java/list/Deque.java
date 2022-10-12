package list;

// 使用双向链表 实现双端队列
public class Deque<E> {
    private LinkedList2<E> list = new LinkedList2<>();

    //  元素大小
    public int size() {
        return list.size();
    }

    // 是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // 从队尾入队
    public void enQueueRear(E element) {
        list.add(element);
    }

    // 从队头出队
    public E deQueueFront() {
        return list.remove(0);
    }

    // 从队头入队
    public void enQueueFront(E element) {
        list.add(0, element);
    }

    // 从队尾出队
    public E deQueueRear() {
        return list.remove(size() - 1);
    }

    // 获取队列的头元素
    public E front() {
        return list.get(0);
    }

    // 获取队列的尾元素
    public E rear() {
        return list.get(size() - 1);
    }

}
