package tree;

public interface CircleQueueInterface<E> {

    // 当前队列存储的元素数量
    public int size();
    // 当前队列是否为空
    public boolean isEmpty();
    // 入队
    public void enQueue(E element);
    // 出队
    public E deQueue();
    // 查看索引为0的元素
    public E front();
}
