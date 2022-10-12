package graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Graph<V, E> {


    interface WeightManger<E> {
        int comparator(E w1, E w2);
        E add(E w1, E w2);
        E zero();
    }

    interface VertexVisitor<V>{
        boolean visit(V v);

    }

    public abstract int edgesSize();
    
    public abstract int verticesSize();

    public abstract void addVertex(V v);

    public abstract void addEdge(V from, V to);

    public abstract void addEdge(V from, V to, E weight);

    public abstract void removeVertex(V v);

    public abstract void removeEdge(V from, V to);

    public abstract void bfs(V begin);

    public abstract void dfs(V begin);

    public abstract List<V> tpo();  // 拓扑排序

    public abstract Set<EdgeInfo<V, E>> mst();  // 最小生成树

//    public abstract Map<V, E> shortestPath(V begin); // 最短路径
    public abstract Map<V,PathInfos<V, E>> shortestPath(V begin); // 最短路径

    class EdgeInfo<V, E> {
        protected V from;
        protected V to;
        protected E weight;
        public EdgeInfo(V from, V to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }

        public V getFrom() {
            return from;
        }

        public void setFrom(V from) {
            this.from = from;
        }

        public V getTo() {
            return to;
        }

        public void setTo(V to) {
            this.to = to;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }
    }

    class PathInfos<V, E> {
        protected E weight;
        protected List<EdgeInfo<V, E>> edgeInfos = new LinkedList<>();

        public PathInfos() {}

        public PathInfos(E weight) {
            this.weight = weight;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        public List<EdgeInfo<V, E>> getEdgeInfos() {
            return edgeInfos;
        }

        public void setEdgeInfos(List<EdgeInfo<V, E>> edgeInfos) {
            this.edgeInfos = edgeInfos;
        }

        @Override
        public String
        toString() {
            return "PathInfos{" +
                    "weight=" + weight +
                    ", edgeInfos=" + edgeInfos +
                    '}';
        }
    }


}
