package tree;

import java.util.Comparator;

public class BST<E> extends BinaryTree {

    private Comparator<E> comparator;

    public BST() {
        this(null);
    }

    public BST(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    // 添加节点后的调整
    protected void afterAdd(Node<E> node) {};

    // 删除节点后的调整
    protected void afterRemove(Node<E> node, Node<E> replacemen) {};

    // 添加元素
    public void add(E element) {
        elementNotNullCheck(element);

        // 第一个元素
        if (root == null) {
            root = createNode(element, null);
            size++;

            // 添加节点后的调整
            afterAdd(root);
            return;
        }

        Node<E> parent = null;
        Node<E> node = root;
        int cmp = 0;
        while(node != null) {
            cmp = compare(element, node.element);
            parent = node;
            if (cmp > 0) {  // 左边
                node = node.right;
            }else if(cmp < 0) { // 右边
                node = node.left;
            }else{  // 相等
                node.element = element;
                return ;
            }
        }
        Node<E> newNode = createNode(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        }else{
            parent.left = newNode;
        }
        size++;

        // 添加节点后的调整
        afterAdd(newNode);
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    private int compare(E e1, E e2) {
         if (comparator != null) {
             return comparator.compare(e1, e2);
         }
         return ((Comparable<E>) e1).compareTo(e2);
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    // 删除节点
    public void remove(E element) {
        remove(node(element));
    }

    private void remove (Node<E> node) {
        if (root == null) return;

        size--;

        if (node.hasTwoChildren()) {  // 度为2的节点
            Node<E> successor = successor(node);
            node.element = successor.element;
            node = successor;
        }

        // 删除度为1/0的节点
        Node<E> replacemen = node.left != null ? node.left : node.right;

        if (replacemen != null) { // 度为１
            replacemen.parent = node.parent;

            if (node.parent == null) {
                root = replacemen; // 度为1且为根节点
            }else if(node == node.parent.left) {
                node.parent.left = replacemen;
            }else {
                node.parent.right = replacemen;
            }

            // 删除节点后的调整
            afterRemove(node, replacemen);
        }else if (node.parent == null) {  // 叶子节点且为根节点
            root = null;

            // 删除节点后的调整
            afterRemove(node, null);
        }else{  // 度为0
            if (node == node.parent.left) {
                node.parent.left = null;
            }else{
                node.parent.right = null;
            }

            // 删除节点后的调整
            afterRemove(node, null);
        }
    }

    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0) {
                return node;
            }

            if (cmp > 0) {
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
    }

    public boolean contains(E element) {
        return node(element) != null;
    }



}
