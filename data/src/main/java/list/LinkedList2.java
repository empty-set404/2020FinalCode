package list;

// 双向链表
public class LinkedList2<E> extends AbstractList<E>{
    private Node<E> first;
    private Node<E> last;

    // 节点对象
    private static class Node<E> {
        Node<E> prev;
        E element;
        Node<E> next;
        public Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            if (prev != null) {
                stringBuffer.append(prev.element);
            }else{
                stringBuffer.append("null");
            }
            stringBuffer.append("_").append(element).append("_");
            if (next != null) {
                stringBuffer.append(next.element);
            }else{
                stringBuffer.append("null");
            }
            return stringBuffer.toString();
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        first = null;
        size = 0;
        last = null;
    }

    public E get(int index) {
        return node(index).element;
    }

    public E set(int index, E element) {
        Node<E> node = node(index);
        E oldElement = node.element;
        node.element = element;
        return oldElement;
    }

    public void add(int index, E element) {
        rangeCheckForAdd(index);

        if (index == size) {  // 末尾添加元素
            Node<E> prev = this.last;
            Node<E>  newElement = new Node<>(prev, element, null);
            this.last = newElement;

            if (prev == null) {  // 链表无元素
                this.first = newElement;
            }else{
                prev.next = newElement;
            }

        }else{
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newElement = new Node<>(prev, element, next);
            next.prev = newElement;

            if (prev == null) {  // index == 0
                this.first = newElement;
            }else{
                prev.next = newElement;
            }
        }

        size++;
    }

    public E remove(int index) {
        rangeCheck(index);

        Node<E> oldNode = node(index);
        Node<E> next = oldNode.next;
        Node<E> prev = oldNode.prev;

        if (next == null) {
            this.last = prev;
        }else{
            next.prev = prev;
        }

        if (prev == null) {
            this.first = next;
        }else{
            prev.next = next;
        }
        size--;

        return oldNode.element;
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
        if (index > (size >> 1)) {
            Node<E> last = this.last;
            for (int i = size - 1; i > index; i--) {
                last = last.prev;
            }
            return last;
        }else{
            Node<E> first = this.first;
            for (int i = 0; i < index; i++) {
                first = first.next;
            }
            return first;
        }
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
