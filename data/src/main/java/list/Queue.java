package list;

// 使用双向链表 实现队列
public class Queue<E> {
    private LinkedList2<E> list = new LinkedList2<>();

    // 元素大小
    public int size() {
        return list.size();
    }

    // 是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // 入队 (添加到队尾)
    public void enQueue(E element) {
        list.add(element);
    }

    // 出队 (删除对头元素)
    public E deQueue() {
        return list.remove(0);
    }

    // 获取队列头元素
    public E font() {
        return list.get(0);
    }

    public void clear() {
        list.clear();
    }

}
