package list;

// 使用动态数组实现 栈
public class Stack<E> {

    private ArrayList<E> list = new ArrayList<>();

    // 元素数量
    public int size() {
        return list.size();
    }

    // 是否为空
    public boolean isEmpty(){
        return list.isEmpty();
    }

    // 入栈
    public void push(E element) {
        list.add(element);
    }

    // 出栈
    public E pop() {
        return list.remove(this.size() - 1);
    }

    // 获取栈顶元素
    public E top() {
        return list.get(this.size() - 1);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
