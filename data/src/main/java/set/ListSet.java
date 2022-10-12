package set;

import list.LinkedList;
import list.List;

/**
 * LinkedList实现的ListSet
 * @param <E>
 */
public class ListSet<E> implements Set<E> {

    private LinkedList<E> list = new LinkedList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public void add(E element) {
        final int index = list.indexOf(element);
        if (index == List.ELEMENT_NOT_FOUND) {
            list.add(element);
        }else {
            list.set(index, element);
        }
    }

    @Override
    public void remove(E element) {
        int index = list.indexOf(element);
        if (index != List.ELEMENT_NOT_FOUND) {
            list.remove(index);
        }
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            boolean visit = visitor.visit(list.get(i));
            if (visit) return;
        }
    }
}
