package tree;

import java.util.Comparator;

public class AVLTee<E> extends BBST<E> {

    public AVLTee() {
        this(null);
    }

    public AVLTee(Comparator<E> comparator) {
        super(comparator);
    }


    @Override
    protected void afterAdd(BinaryTree.Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度
                updateHeight(node);
            }else{
                // 恢复平衡
                rebalance(node);
                break;
            }
        }
    }

    @Override
    protected void afterRemove(BinaryTree.Node<E> node, BinaryTree.Node<E> replacemen) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度
                updateHeight(node);
            }else{
                // 恢复平衡
                rebalance(node);
            }
        }
    }

    static class AVLNode<E> extends BinaryTree.Node<E> {
        int height = 1;

        public AVLNode(E element, BinaryTree.Node<E> parent) {
            super(element, parent);
        }

        public int balanceFactor() {
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;
            return leftHeight - rightHeight;
        }

        public void updateHeight() {
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public BinaryTree.Node<E> tallerChild() {
            int leftHeight = left != null ? ((AVLNode<E>) left).height : 0;
            int rightHeight = right != null ? ((AVLNode<E>) right).height : 0;
            if (leftHeight > rightHeight) return left;
            if (rightHeight > leftHeight) return right;
            return isLeftChild() ? left : right;
        }
        @Override
        public String toString() {
            String parentString = "null";
            if (parent != null) {
                parentString = parent.element.toString();
            }
            return element + "_p(" + parentString + ")_h(" + height + ")";
        }
    }

    @Override
    protected BinaryTree.Node<E> createNode(E element, BinaryTree.Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    // 判断当前节点是否平衡
    private boolean isBalanced(BinaryTree.Node<E> node) {
        return Math.abs((((AVLNode<E>)node).balanceFactor())) <= 1;
    }

    // 更新高度
    private void updateHeight(BinaryTree.Node<E> node) {
        ((AVLNode<E>)node).updateHeight();
    }

    // 恢复平衡
    private void rebalance2(BinaryTree.Node<E> grand) {
        BinaryTree.Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        BinaryTree.Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {   // L
            if (node.isLeftChild()) {  // LL
                rotateRight(grand);
            }else {  // LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else {   // R
            if (node.isRightChild()) {  // RR
                rotateLeft(grand);
            }else {  // RL
                rotateRight(parent);
                rotateLeft(grand);
            }
        }
    }

    // 恢复平衡
    private void rebalance(BinaryTree.Node<E> grand) {
        BinaryTree.Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        BinaryTree.Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {   // L
            if (node.isLeftChild()) {  // LL
                rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
            }else {  // LR
                rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
            }
        }else {   // R
            if (node.isRightChild()) {  // RR
                rotate(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
            }else {  // RL
                rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
            }
        }
    }


    @Override
    protected void rotate(BinaryTree.Node<E> r, BinaryTree.Node<E> a, BinaryTree.Node<E> b, BinaryTree.Node<E> c, BinaryTree.Node<E> d, BinaryTree.Node<E> e, BinaryTree.Node<E> f, BinaryTree.Node<E> g) {
        super.rotate(r, a, b, c, d, e, f, g);
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    @Override
    protected void afterRotate(BinaryTree.Node<E> grand, BinaryTree.Node<E> parent, BinaryTree.Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 先更新grand高度 再更新 parent高度
        updateHeight(grand);
        updateHeight(parent);
    }
}
