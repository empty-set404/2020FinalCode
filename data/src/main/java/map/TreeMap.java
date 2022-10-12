package map;

import list.Queue;

import java.util.Comparator;

/**
 * 红黑树实现映射
 * @param <K>
 * @param <V>
 */
public class TreeMap<K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;
    private Node<K, V> root;
    private Comparator<K> comparator;

    public TreeMap() {
        this(null);
    }

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    /**
     * 红黑树节点
     * @param <K>
     * @param <V>
     */
    private static class Node<K, V> {
        K key;
        V value;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }
        public boolean isLeaf() { // 是否为叶子结点
            return left == null && right == null;
        }

        public boolean hasTwoChildren() { // 是否有两个子节点
            return left != null && right != null;
        }

        public boolean isLeftChild() { // 是否为左节点
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() { // 是否为右节点
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() { // 返回兄弟节点
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }
            return null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        KeyNotNullCheck(key);

        // 第一个元素
        if (root == null) {
            root = new Node<K, V>(key, value, null);
            size++;

            // 添加节点后的调整
            afterPut(root);
            return null;
        }

        Node<K ,V> parent = null;
        Node<K ,V> node = root;
        int cmp = 0;
        while(node != null) {
            cmp = compare(key, node.key);
            parent = node;
            if (cmp > 0) {  // 左边
                node = node.right;
            }else if(cmp < 0) { // 右边
                node = node.left;
            }else{  // 相等
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        Node<K ,V> newNode = new Node<K, V>(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        }else{
            parent.left = newNode;
        }
        size++;

        // 添加节点后的调整
        afterPut(newNode);
        return null;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    private V remove(Node<K, V> node) {
        if (root == null) return null;
        Node<K, V> oldNode = node;

        size--;

        if (node.hasTwoChildren()) {  // 度为2的节点
            Node<K ,V> successor = successor(node);
            node.value = successor.value;
            node.key = successor.key;
            node = successor;
        }

        // 删除度为1/0的节点
        Node<K ,V> replacemen = node.left != null ? node.left : node.right;

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

        return oldNode.value;
    }

    // 后继节点
    protected Node<K ,V> successor(Node<K ,V> node) {
        if (node == null) return null;

        Node<K ,V> s = node.right;
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

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null) return false;

        Queue<Node<K, V>> queue = new Queue<>();
        queue.enQueue(root);

        while (!queue.isEmpty()) {
            Node<K, V> node = queue.deQueue();
            if (valEquals(value, node.value)) return true;

            if (node.left != null) {
                queue.enQueue(node.left);
            }

            if (node.right != null) {
                queue.enQueue(node.right);
            }
        }

        return false;
    }

    private boolean valEquals(V v1, V v2) {
        return v1 == null ? v2 == null : v1.equals(v2);
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        traversal(root, visitor);
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop) return;

        traversal(node.left, visitor);
        if (visitor.stop) return;
        visitor.visit(node.key, node.value);
        traversal(node.right, visitor);
    }

    protected void afterPut(Node<K ,V> node) {
        Node<K ,V> parent = node.parent;

        // 添加的是根节点 或者 上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) return;

        // 获取叔父节点和祖父节点
        Node<K ,V> uncle = parent.sibling();
        Node<K ,V> grand = parent.parent;

        // 叔父节点是红色【B树节点上溢】
        if (isRed(uncle)) {
            black(parent);
            black(uncle);

            // 把祖父节点当做是新添加的节点
            red(grand);
            afterPut(grand);
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

    protected void afterRemove(Node<K ,V> node, Node<K, V> replacemen) {
        // 如果删除的节点是红色
        if (isRed(node)) return;

        // 用以取代node的子节点是红色
        if (isRed(replacemen)) {
            black(replacemen);
            return;
        }

        // 删除的是根节点
        Node<K ,V> parent = node.parent;
        if (parent == null) return;

        // 删除的是黑色叶子节点
        boolean left = node.parent.left == null || node.isLeftChild();
        Node<K ,V> sibling = left ? parent.right : parent.left;

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

    protected void afterRotate(Node<K ,V> grand, Node<K ,V> parent, Node<K ,V> child) {
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

    protected void rotateRight(Node<K ,V> grand) {
        Node<K ,V> parent = grand.left;
        Node<K ,V> child = parent.right;
        grand.left = child;
        parent.right = grand;

        // 更新高度和父节点
        afterRotate(grand, parent, child);
    }

    protected void rotateLeft(Node<K ,V> grand) {
        Node<K ,V> parent = grand.right;
        Node<K ,V> child = parent.left;
        grand.right = child;
        parent.left = grand;

        // 更新 grand parent 和 child的父节点 以及高度
        afterRotate(grand, parent, child);
    }

    private Node<K, V> node(K key) {
        Node<K, V> node = root;

        while (node != null) {
            int cmp = compare(key, node.key);
            if (cmp == 0) return node;

            if (cmp > 0) {
                node = node.right;
            }else {
                node = node.left;
            }
        }
        return null;
    }

    private int compare(K e1, K e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<K>) e1).compareTo(e2);
    }

    // 节点染色
    private Node<K ,V> color(Node<K ,V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    // 判断是否为红色节点
    private boolean isRed(Node<K ,V> node) {
        return colorOf(node) == RED;
    }

    // 判断是否为黑色节点
    private boolean isBlack(Node<K ,V> node) {
        return colorOf(node) == BLACK;
    }

    // 返回节点颜色
    private boolean colorOf(Node<K ,V> node) {
        return node == null ? BLACK : node.color;
    }

    // 节点染红色
    private Node<K ,V> red(Node<K ,V> node) {
        return color(node, RED);
    }

    // 节点染黑色
    private Node<K ,V> black(Node<K ,V> node) {
        return color(node, BLACK);
    }

    private void KeyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

}
