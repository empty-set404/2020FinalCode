package tree;

import java.util.Comparator;

public class BBST<E> extends BST<E> {

    public BBST() {
        this(null);
    }

    public BBST(Comparator<E> comparator) {
        super(comparator);
    }

    // 统一旋转
    protected void rotate(Node<E> r, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        d.parent = r.parent;
        if (r.isLeftChild()) {
            r.parent.left = d;
        }else if(r.isRightChild()) {
            r.parent.right = d;
        }else {
            root = d;
        }

        // a-b-c
        b.left = a;
        if (a != null) {
            a.parent = b;
        }
        b.right = c;
        if (c != null) {
            c.parent = b;
        }

        // c-f-g
        f.left = e;
        if (e != null) {
            e.parent = f;
        }
        f.right = g;
        if (g != null) {
            g.parent = f;
        }

        // b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
    }

    // 右旋
    protected void rotateRight(Node<E> grand) {
        Node<E> parent = grand.left;
        Node<E> child = parent.right;
        grand.left = child;
        parent.right = grand;

        // 更新高度和父节点
        afterRotate(grand, parent, child);
    }

    // 左旋
    protected void rotateLeft(Node<E> grand) {
        Node<E> parent = grand.right;
        Node<E> child = parent.left;
        grand.right = child;
        parent.left = grand;

        // 更新 grand parent 和 child的父节点 以及高度
        afterRotate(grand, parent, child);
    }

    // 旋转之后
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        // 更新父节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        }else if(grand.isRightChild()) {
            grand.parent.right = parent;
        }else {
            // parent 成为根节点
            root = parent;
        }
        grand.parent = parent;
        if (child != null) {
            child.parent = grand;
        }

    }

}
