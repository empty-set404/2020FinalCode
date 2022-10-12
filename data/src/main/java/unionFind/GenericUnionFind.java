package unionFind;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GenericUnionFind<V> {

    private Map<V, Node<V>> nodes = new HashMap<>();

    private static class Node<V> {
        V value;
        Node<V> parent = this;
        int rank = 1;
        public Node(V value) {
            this.value = value;
        }
    }

    public void makeSet(V v) {
        if (this.nodes.containsKey(v)) return;
        this.nodes.put(v, new Node<>(v));
    }

    public void makeSet(Collection<? extends V> list) {
        for (V v : list) makeSet(v);
    }

    public boolean isSame(V v1, V v2) {
        return Objects.equals(find(v1), find(v2));
    }


    public V find(V v) {
        Node<V> node = findNode(v);
        return node == null ? null : node.value;
    }

    public Node<V> findNode(V v) {
        Node<V> node = this.nodes.get(v);
        if (node == null) return null;

        while (!Objects.equals(node.value, node.parent.value)) {
            node.parent = node.parent.parent;
            node = node.parent;
        }
        return node;
    }

    public void union(V v1, V v2) {
        Node<V> p1 = findNode(v1);
        Node<V> p2 = findNode(v2);
        if (p1 == null || p2 == null) return;
        if (Objects.equals(p1.value, p2.value)) return;

        if (p1.rank < p2.rank) {
            p1.parent = p2;
        }else if(p1.rank > p2.rank) {
            p2.parent = p1;
        }else{
            p1.parent = p2;
            p1.rank++;
        }
    }

}
