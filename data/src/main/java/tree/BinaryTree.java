package tree;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTree<E> implements BinaryTreeInfo {

    protected int size;
    protected Node<E> root;

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((BST.Node)node).left;
    }

    @Override
    public Object right(Object node) {
        return ((BST.Node)node).right;
    }

    @Override
    public Object string(Object node) {
        return node;
    }

    protected static class Node<E> {
        E element;
        Node<E> parent;
        Node<E> left;
        Node<E> right;
        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        @Override
        public String toString() {
            return element + "_p(" + (parent == null ? null : element) + ")";
        }

        public boolean isLeftChild() {
            return parent != null && parent.left == this;
        }

        public boolean isRightChild() {
            return parent != null && parent.right == this;
        }

        // 获取兄弟节点
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

    }

    // 元素个数
    public int size() {
        return this.size;
    }

    //　是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 清空二叉树
    public void clear() {
        root = null;
        size = 0;
    }

    // 前序 遍历 (接口)
    public void preorderTraversal(BST.Vistor vistor) {
        if (root == null || vistor == null) return;
        preorderTraversal(root, vistor);
    }

    // 前序 遍历 (接口)
    private void preorderTraversal(Node<E> node, BST.Vistor vistor) {
        if (node == null) return;
        if (vistor.stop) return ;
        vistor.stop = vistor.visit(node.element);
        preorderTraversal(node.left, vistor);
        preorderTraversal(node.right, vistor);
    }

    // 中序遍历 (接口)
    public void inorderTraversal(BST.Vistor vistor) {
        if (root == null || vistor == null) return ;
        inorderTraversal(root, vistor);
    }

    // 中序 遍历 (接口)
    private void inorderTraversal(Node<E> node, BST.Vistor vistor) {
        if (node == null || vistor.stop) return;
        inorderTraversal(node.left, vistor);
        if (vistor.stop) return ;
        vistor.stop = vistor.visit(node.element);
        inorderTraversal(node.right, vistor);
    }

    // 后序 遍历 (接口)
    public void postorderTraversal(BST.Vistor vistor) {
        if (root == null || vistor == null) return;
        postorderTraversal(root, vistor);
    }

    // 后序 遍历 (接口)
    private void postorderTraversal(Node<E> node, BST.Vistor vistor) {
        if (node == null || vistor.stop) return;
        postorderTraversal(node.left, vistor);
        postorderTraversal(node.right, vistor);
        if (vistor.stop) return ;
        vistor.stop = vistor.visit(node.element);

    }

    // 层序 遍历
    public void leverorderTraversal() {
        if (root == null) return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> poll = queue.poll();
            if (poll.left != null) {
                queue.offer(poll.left);
            }
            if (poll.right != null) {
                queue.offer(poll.right);
            }
        }
    }

    // 层序遍历 （接口）
    public void leverorderTraversal(BST.Vistor vistor) {
        if (root == null) return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> poll = queue.poll();
            if (poll.left != null) {
                queue.offer(poll.left);
            }
            if (vistor.visit(poll.element)) return ;
            if (poll.right != null) {
                queue.offer(poll.right);
            }
        }
    }

    // 树状打印二叉树
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        toString(root, stringBuffer, "");
        return stringBuffer.toString();
    }

    private void toString(Node<E> node, StringBuffer sb, String prefix) {
        if (node == null) return;
        sb.append(prefix).append(node).append("\n");
        toString(node.left, sb, prefix + "L---");
        toString(node.right, sb, prefix + "R---");
    }

    // 遍历接口
    public static abstract class Vistor<E>{
        public abstract boolean visit(E element);
        boolean stop;
    }

    // 返回树的高度 (递归)
    public int height2() {
        return height(root);
    }

    // 返回某个节点的高度
    private int height(Node<E> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 返回树的高度 (迭代)
    public int height() {
        Queue<Node<E>> list = new LinkedList<>();
        list.offer(root);
        int heiht = 0;
        int levelsize = 1;
        while (!list.isEmpty()) {
            Node<E> poll = list.poll();
            levelsize--;

            if (poll.left != null) {
                list.offer(poll.left);
            }

            if (poll.right != null) {
                list.offer(poll.right);
            }

            if (levelsize == 0) {
                levelsize = list.size();
                heiht++;
            }
        }
        return heiht;
    }

    // 前驱节点
    protected Node<E> predecessor(Node<E> node) {
        if (node == null) return null;
        Node<E> p = node.left;
        if (p != null) {
            while (p.right != null ) {
                p = p.right;
            }
            return p;
        }

        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        return node.parent;

//        if (node.parent != null) {
//            p = node.parent;
//            while (p == p.parent.right) {
//                p = p.parent;
//            }
//            return p.parent;
//        }
    }

    // 后继节点
    protected Node<E> successor(Node<E> node) {
        if (node == null) return null;

        Node<E> s = node.right;
        if (s != null) {
            while (s.left != null) {
                s = s.left;
            }
            return s;
        }

        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    // 翻转二叉树 (实质 -> 遍历 +　左右节点交换)
    public Node<E> invertTree() {
        if (root == null) return null;
        Queue<Node<E>> list = new LinkedList<>();
        list.offer(root);
        while (!list.isEmpty()) {
            Node<E> poll = list.poll();

            Node<E> tmp = poll.left;
            poll.left = poll.right;
            poll.right = tmp;

            if (poll.left != null) {
                list.offer(poll.left);
            }

            if (poll.right != null) {
                list.offer(poll.right);
            }

        }
        return root;
    }

    // 翻转二叉树 (中序遍历)
    public Node<E> invertTree(Node<E> node) {
        if (node == null) return null;
        invertTree(node.left);

        Node<E> tmp = node.left;
        node.left = node.right;
        node.right = tmp;

        invertTree(node.left);  // 此时的右节点为左节点
        return node;
    }
}
