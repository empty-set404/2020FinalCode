package list;

public class ArrayList<E> extends AbstractList<E> {

    private int size;
    private E[] elements;

    private static final int DEFAULT_CAPACITY = 10; // 默认长度

    public ArrayList() {
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public ArrayList(int size) {
        this.size = size < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : size;
        elements = (E[]) new Object[size];
    }

    /**
     * 返回index位置的元素
     * @param index
     * @return
     */
    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    /**
     * 设置index元素的值
     * @param index
     * @param element
     * @return
     */
    public E set(int index, E element) {
        rangeCheck(index);
        E oldElement = elements[index];
        elements[index] = element;
        return oldElement;
    }

    /**
     * 替换index的元素并返回被替换的元素
     * @param index
     * @param element
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        // 扩容检查
        ensureCapacity(size + 1);

        // 元素后移
        for (int i = size - 1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }
        elements[index] = element;
        size++;
    }

    /**
     * 动态扩容
     * @param i
     */
    private void ensureCapacity(int i) {
        if (i > elements.length) {
            int newlength = elements.length + (elements.length >> 1);
            E[] newElements = (E[]) new Object[newlength];
            for (int i1 = 0; i1 < elements.length; i1++) {
                newElements[i1] = elements[i1];
            }
            elements = newElements;
            System.out.println("数组扩容为: " + elements.length);
        }
    }

    /**
     * 删除index元素
     * @param index
     * @return
     */
    public E remove(int index) {
        rangeCheck(index);
        E oldElement = elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null;
        size--;

        trim();

        return oldElement;
    }

    /**
     * 动态数组缩容
     */
    private void trim() {
        if (size < (elements.length >> 1) && elements.length > DEFAULT_CAPACITY) {
            E[] newlist = (E[]) new Object[elements.length >> 1];
            for (int i = 0; i < size; i++) {
                newlist[i] = elements[i];
            }
            elements = newlist;

            System.out.println("数组缩容为: " + elements.length);
        }
    }

    /**
     * 查找元素的位置
     * @param element
     * @return
     */
    public int indexOf(E element) {
        if (element == null) {
            for (int i = 0; i <= size - 1; i++) {
                if (elements[i] == null) return i;
            }
        }else{
            for (int i = 0; i <= size - 1; i++) {
                if (elements[i].equals(element)) return i;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        for (E element : elements) {
            element = null;
        }
        size = 0;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("size: ").append(size).append("  [");
        for (int i = 0; i < size; i++) {
            if (i == 0 ){
                stringBuffer.append(elements[i]);
            }else{
                stringBuffer.append(",").append(elements[i]);
            }
        }
        stringBuffer.append("]  " + elements.length);
        return stringBuffer.toString();
    }

}
