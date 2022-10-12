package map;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class HashMap <K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;

    private Node<K, V>[] table;
    private static final int DEFAULT_CAPACITY = 1 << 4;

    public HashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    private static class Node<K, V> {
        K key;
        V value;
        int hash;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
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
        if (size == 0) return;
        size = 0;
        for (int i = 0; i < this.table.length; i++) {
            this.table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        resize();

        int index = index(key);
        Node<K, V> root = this.table[index];

        if (root == null) {
            this.table[index] = createNode(key, value, null);
            size++;

            // 添加节点后的调整
            fixAfterPut(this.table[index]);
            return null;
        }

        Node<K ,V> parent = null;
        Node<K ,V> node = root;
        int cmp = 0;
        K k1 = key;
        int h1 = hash(k1);
        Node<K, V> result = null;
        boolean searched = false; // 是否已经搜索过这个key

        while(node != null) {
            parent = node;
            K k2 = node.key;
            int h2 = hash(k2);

            if (h1 > h2) {
                cmp = 1;
            }else if(h1 < h2) {
                cmp = -1;
            }else if(Objects.equals(k1, k2)) {
                cmp = 0;
            }else if(k1 != null && k2 != null && k1 instanceof Comparable && k1.getClass() == k2.getClass() && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
                node = cmp > 0 ? node.right : node.left;
            }else if(searched) {  // 已经扫描过了
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }else {
                if ((node.left != null && (result = node(node.left, k1)) != null) || (node.right != null && (result = node(node.right, k1)) != null)) {
                    node = result;
                    cmp = 0;
                }else {
                    searched = true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }

            if (cmp > 0) {  // 左边
                node = node.right;
            }else if(cmp < 0) { // 右边
                node = node.left;
            }else{  // 相等
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                node.hash = h1;
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
        fixAfterPut(newNode);
        return null;
    }

    private Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    private void resize() {

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

    protected void fixAfterPut(Node<K ,V> node) {
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
            fixAfterPut(grand);
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

    protected void fixAfterRemove(Node<K ,V> node, Node<K, V> replacemen) {
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
                    fixAfterRemove(parent, null);
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
                    fixAfterRemove(parent, null);
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
        int index = index(grand);

        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        }else if(grand.isRightChild()) {
            grand.parent.right = parent;
        }else {
            // parent 成为根节点
            this.table[index] = parent;
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

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    private V remove(Node<K, V> node) {
        if (node == null) return null;

        Node<K, V> oldNode = node;

        Node<K, V> willNode = node;

        size--;

        if (node.hasTwoChildren()) {  // 度为2的节点
            Node<K ,V> successor = successor(node);
            node.value = successor.value;
            node.key = successor.key;
            node.hash = successor.hash;
            node = successor;
        }

        // 删除度为1/0的节点
        Node<K ,V> replacemen = node.left != null ? node.left : node.right;
        int index = index(node);

        if (replacemen != null) { // 度为１
            replacemen.parent = node.parent;

            if (node.parent == null) {
                this.table[index] = replacemen; // 度为1且为根节点
            }else if(node == node.parent.left) {
                node.parent.left = replacemen;
            }else {
                node.parent.right = replacemen;
            }

            // 删除节点后的调整
            fixAfterRemove(node, replacemen);
        }else if (node.parent == null) {  // 叶子节点且为根节点
            this.table[index] = null;

            // 删除节点后的调整
            fixAfterRemove(node, null);
        }else{  // 度为0
            if (node == node.parent.left) {
                node.parent.left = null;
            }else{
                node.parent.right = null;
            }

            // 删除节点后的调整
            fixAfterRemove(node, null);
        }

        return oldNode.value;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < this.table.length; i++) {
            queue.offer(this.table[i]);
            while (!queue.isEmpty()) {
                final Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value)) return true;

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) return;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < this.table.length; i++) {
            queue.offer(this.table[i]);
            while (!queue.isEmpty()) {
                final Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) return;

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];
        return root == null ? null : node(root, key);
    }

    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        Node<K, V> result = null;
        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = hash(k2);
            if (h1 > h2) {
                node = node.right;
            }else if(h1 < h2) {
                node = node.left;
            }else if(Objects.equals(k1, k2)) {
                return node;
            }else if(k1 != null && k2 != null && k1 instanceof Comparable && k1.getClass() == k2.getClass() && (cmp = ((Comparable)k1).compareTo(k2)) != 0) {
                node = cmp > 0 ? node.right : node.left;
            }else if(node.right != null && (result = node(node.right, k1)) != null) {
                return result;
            }else {
                node = node.left;
            }
        }
        return null;
    }

    private int index(K key) {
        return hash(key) & (table.length - 1);
    }

    private int index(Node<K, V> node) {
        return node.hash & (table.length - 1);
    }

    private int hash(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }


}
