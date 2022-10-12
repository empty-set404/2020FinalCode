package list;


// 单向循环链表
public class LinkedList3<E> extends AbstractList<E>{
    private Node<E> first;


    // 节点对象
    private static class Node<E> {
        E element;
        Node<E> next;
        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
        @Override
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(element).append("_");
            if (next != null) {
                stringBuffer.append(next.element);
            }else{
                stringBuffer.append("null");
            }
            return stringBuffer.toString();
        }
    }

    public void clear() {
        first = null;
        size = 0;
    }

    public E get(int index) {
        rangeCheck(index);
        return node(index).element;
    }

    public E set(int index, E element) {
        rangeCheck(index);
        Node<E> node = node(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    public void add(int index, E element) {
        rangeCheckForAdd(index);

        if (index == 0) {
            first = new Node<>(element, first);
            Node<E> last = size >= 1 ? node(size - 1) : first;
            last.next = first;
        }else{
            Node<E> perv = node(index - 1);
            perv.next = new Node<>(element, perv.next);
        }
        size++;
    }

    public E remove(int index) {
        rangeCheck(index);
        if (index == 0) {
            E oldElement = first.element;
            first = first.next;
            size--;
            Node<E> last = node(size - 1);
            last.next = first;
            return oldElement;
        }else{
            Node<E> perv = node(index - 1);
            E oldElement = perv.next.element;
            perv.next = perv.next.next;
            size--;
            return oldElement;
        }
    }

    public int indexOf(E element) {
        if (element == null) {
            Node<E> first = this.first;
            int i = 0;
            while (first != null) {
                if (first.element == null) return i;
                i++;
                first = first.next;
            }
        }else{
            Node<E> first = this.first;
            int i = 0;
            while (first != null) {
                if (first.element.equals(element)) return i;
                i++;
                first = first.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    private Node<E> node(int index) {
        rangeCheck(index);
        Node<E> first = this.first;
        for (int i = 0; i < index; i++) {
            first = first.next;
        }
        return first;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("size: ").append(size).append("  [");
        Node<E> first = this.first;
        for (int i = 0; i < size; i++) {
            if (i == 0 ){
                stringBuffer.append(first);
            }else{
                stringBuffer.append(", ").append(first);
            }
            first = first.next;
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

}
