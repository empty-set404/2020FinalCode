package tree;

import java.util.Comparator;

public class RBTree<E> extends BBST<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree() {
        this(null);
    }

    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    private static class RBNode<E> extends BinaryTree.Node<E> {
        boolean color = RED;

        public RBNode(E element, BinaryTree.Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }

    @Override
    protected BinaryTree.Node<E> createNode(E element, BinaryTree.Node<E> parent) {
        return new RBNode<E>(element, parent);
    }

    @Override
    protected void afterAdd(BinaryTree.Node<E> node) {
        BinaryTree.Node<E> parent = node.parent;

        // 添加的是根节点 或者 上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) return;

        // 获取叔父节点和祖父节点
        BinaryTree.Node<E> uncle = parent.sibling();
        BinaryTree.Node<E> grand = parent.parent;

        // 叔父节点是红色【B树节点上溢】
        if (isRed(uncle)) {
            black(parent);
            black(uncle);

            // 把祖父节点当做是新添加的节点
            red(grand);
            afterAdd(grand);
            return;
        }

        // 叔父节点不是红色
        if (parent.isLeftChild()) {
            if (node.isLeftChild()) {  // LL
                black(parent);
                red(grand);
                rotateRight(grand);
            }else {  // LR
                black(node);
                red(grand);
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else {
            if (node.isLeftChild()) {  // RL
                black(node);
                red(grand);
                rotateRight(parent);
                rotateLeft(grand);
            }else {  // RR
                black(parent);
                red(grand);
                rotateLeft(grand);
            }
        }
    }

    @Override
    protected void afterRemove(BinaryTree.Node<E> node, BinaryTree.Node<E> replacemen) {
        // 如果删除的节点是红色
        if (isRed(node)) return;

        // 用以取代node的子节点是红色
        if (isRed(replacemen)) {
            black(replacemen);
            return;
        }

        // 删除的是根节点
        BinaryTree.Node<E> parent = node.parent;
        if (parent == null) return;

        // 删除的是黑色叶子节点
        boolean left = node.parent.left == null || node.isLeftChild();
        BinaryTree.Node<E> sibling = left ? parent.right : parent.left;

        // 被删除的节点在左边，兄弟节点在右边
        if (left) {
            if (isRed(sibling)) {
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色节点， 父节点要下溢
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }
            }else {
                // 向兄弟借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        }else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) {
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色节点， 父节点要下溢
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }
            }else {
                // 向兄弟借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }

    // 节点染色
    private BinaryTree.Node<E> color(BinaryTree.Node<E> node, boolean color) {
        if (node == null) return node;
        ((RBNode<E>) node).color = color;
        return node;
    }

    // 判断是否为红色节点
    private boolean isRed(BinaryTree.Node<E> node) {
        return colorOf(node) == RED;
    }

    // 判断是否为黑色节点
    private boolean isBlack(BinaryTree.Node<E> node) {
        return colorOf(node) == BLACK;
    }

    // 返回节点颜色
    private boolean colorOf(BinaryTree.Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    // 节点染红色
    private BinaryTree.Node<E> red(BinaryTree.Node<E> node) {
        return color(node, RED);
    }

    // 节点染黑色
    private BinaryTree.Node<E> black(BinaryTree.Node<E> node) {
        return color(node, BLACK);
    }


}
