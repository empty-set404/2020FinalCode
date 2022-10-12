package tree;

import list.LinkedList2;

// 双向链表 实现循环队列
public class CircleQueue<E> implements CircleQueueInterface {
    private LinkedList2<E> list = new LinkedList2<>();
    private int size;
    private int first;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void enQueue(Object element) {

    }

    @Override
    public Object deQueue() {
        return null;
    }

    @Override
    public Object front() {
        return null;
    }
}
