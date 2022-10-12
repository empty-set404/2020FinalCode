package graph;

import java.util.*;

public class ListGraph<V, E> extends Graph<V, E>{

    private Map<V, Vertex<V, E>> vertecies = new HashMap<>();
    private Set<Edge<V, E>> edges = new HashSet<>();

    private WeightManger<E> weightManger;

    public ListGraph() {}

    public ListGraph(WeightManger<E> weightManger) {
        this.weightManger = weightManger;
    }

    private static class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>();
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex<?, ?> vertex = (Vertex<?, ?>) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
//            String valueString =  value == null ? "null" : value.toString();
//            return "Vertex{" +
//                    "value=" + valueString +
//                    ", inEdges=" + inEdges +
//                    ", outEdges=" + outEdges +
//                    '}';
            return value.toString();
        }
    }

    private static class Edge<V, E>{
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge<?, ?> edge = (Edge<?, ?>) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from.value +
                    ", to=" + to.value +
                    ", weight=" + weight +
                    '}';
        }
    }

    public void print() {
        vertecies.forEach((v, vertex) -> {
            System.out.println(v);
            System.out.println(vertex.outEdges);
            System.out.println(vertex.inEdges);
            System.out.println("--------------");

        });

        System.out.println("=====================");

        edges.forEach(System.out::println);
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    @Override
    public int verticesSize() {
        return vertecies.size();
    }

    @Override
    public void addVertex(V v) {
        if (vertecies.containsKey(v)) return;
        vertecies.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {

        if (!vertecies.containsKey(from)) {
            vertecies.put(from, new Vertex<>(from));
        }

        if (!vertecies.containsKey(to)) {
            vertecies.put(to, new Vertex<>(to));
        }

        Vertex<V, E> fromVertex = vertecies.get(from);
        Vertex<V, E> toVertex = vertecies.get(to);

        Edge<V, E> veEdge = new Edge<V, E>(fromVertex, toVertex);
        veEdge.weight = weight;

        fromVertex.outEdges.remove(veEdge);
        toVertex.inEdges.remove(veEdge);
        edges.remove(veEdge);

        fromVertex.outEdges.add((veEdge));
        toVertex.inEdges.add((veEdge));
        edges.add(veEdge);

    }

    @Override
    public void removeVertex(V v) {
        Vertex<V, E> veVertex = vertecies.remove(v);
        if (veVertex == null ) return;

        for (Iterator<Edge<V, E>> iterator = veVertex.outEdges.iterator(); iterator.hasNext();) {
            Edge<V, E> edge = iterator.next();
            iterator.remove();
            edge.to.inEdges.remove(edge);
            edges.remove(edge);
        }

        for (Iterator<Edge<V, E>> iterator = veVertex.inEdges.iterator(); iterator.hasNext();) {
            Edge<V, E> edge = iterator.next();
            iterator.remove();
            edge.from.outEdges.remove(edge);
            edges.remove(edge);
        }

    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromvertex = vertecies.get(from);
        if (fromvertex == null) return;

        Vertex<V, E> tovertex = vertecies.get(to);
        if (tovertex == null) return;

        Edge<V, E> veEdge = new Edge<>(fromvertex, tovertex);

        fromvertex.outEdges.remove(veEdge);
        tovertex.inEdges.remove(veEdge);
        edges.remove(veEdge);

    }

    @Override
    public void bfs(V begin) {
        Vertex<V, E> beginVertex = vertecies.get(begin);
        if (beginVertex == null) return;

        Queue<Vertex> queue = new LinkedList<>();
        HashSet<Vertex> visited = new HashSet<>();
        queue.offer(beginVertex);
        visited.add(beginVertex);

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            System.out.println(vertex.value);

            for (Object outEdge : vertex.outEdges) {
                Edge<V, E> edge = (Edge<V, E>) outEdge;
                if (!visited.contains(edge.to)) {
                    queue.offer(edge.to);
                    visited.add(edge.to);
                }
            }

        }
    }

//    @Override
//    public void dfs(V begin) {
//        Vertex<V, E> beginVertex = vertecies.get(begin);
//        if (beginVertex == null)return;
//
//        dfs(beginVertex, new HashSet<>());
//    }
//
//    public void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> vertices) {
//        if (!vertices.contains(vertex)) {
//            vertices.add(vertex);
//            System.out.println(vertex.value);
//        }
//
//        for (Edge<V, E> outEdge : vertex.outEdges) {
//            dfs(outEdge.to, vertices);
//        }
//    }

    @Override
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertecies.get(begin);
        if (beginVertex == null)return;

        HashSet<Vertex<V, E>> vistored = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();

        stack.push(beginVertex);
        vistored.add(beginVertex);
        System.out.println(beginVertex.value);

        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();
            for (Edge<V, E> outEdge : vertex.outEdges) {
                if (vistored.contains(outEdge.to)) continue;
                stack.push(outEdge.to);
                vistored.add(outEdge.to);
                System.out.println(outEdge.to.value);
                break;
            }
        }
    }

    @Override
    public List<V> tpo() {

        // 初始化
        HashMap<Vertex<V, E>, Integer> vertexInsize = new HashMap<>();
        Queue<Vertex<V, E>> vertexQueue = new LinkedList<>();
        List<V> vertexs = new ArrayList<>();
        this.vertecies.forEach((v, vertex) -> {
            if (vertex.inEdges.size() == 0) {
                vertexQueue.add(vertex);
                vertexs.add(vertex.value);
            }else{
                vertexInsize.put(vertex, vertex.inEdges.size());
            }
        });

        while (!vertexQueue.isEmpty()) {
            Vertex<V, E> vertex = vertexQueue.poll();
            for (Edge<V, E> outEdge : vertex.outEdges) {
                Integer insize = vertexInsize.get(outEdge.to) - 1;
                if (insize == 0) {
                    vertexQueue.add(outEdge.to);
                    vertexs.add(outEdge.to.value);
                }else{
                    vertexInsize.put(outEdge.to, insize);
                }
            }
        }
        return vertexs;
    }

    @Override
    public Set<EdgeInfo<V, E>> mst() {
        return Kruskal();
    }

    public Set<EdgeInfo<V, E>> Prim() {
        Iterator<Vertex<V, E>> iterator = this.vertecies.values().iterator();
        if (!iterator.hasNext()) return null;

        Vertex<V, E> veVertex = iterator.next();

        HashSet<EdgeInfo<V, E>> edges = new HashSet<>();
        HashSet<Vertex<V, E>> addvertices = new HashSet<>();
        PriorityQueue<Edge<V, E>> heap = new PriorityQueue<>((edge1, edge2) -> this.weightManger.comparator(edge1.weight, edge2.weight));

        addvertices.add(veVertex);
        for (Edge<V, E> outEdge : veVertex.outEdges) {
            heap.add(outEdge);
        }

        int size = this.vertecies.size();
        while (!heap.isEmpty() && addvertices.size() < size){
            Edge<V, E> edge = heap.remove();
            if (addvertices.contains(edge.to)) continue;

            addvertices.add(edge.to);
            edges.add(edgeInfo(edge));
            for (Edge<V, E> outEdge : edge.to.outEdges) {
                heap.add(outEdge);
            }
        }
        return edges;
    }

    public Set<EdgeInfo<V, E>> Kruskal() {
        int size = this.vertecies.size();
        if (size == 0) return null;

        HashSet<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        MinHeap<Edge<V, E>> heap = new MinHeap<>(this.edges, (e1, e2) -> weightManger.comparator(e1.weight, e2.weight));
        UnionFind<Vertex<V, E>> edgeUnionFind = new UnionFind<>();
        this.vertecies.forEach((v, vertex) -> edgeUnionFind.makeSet(vertex));

        int maxSize = this.vertecies.size() - 1;
        while (!heap.isEmpty() && edgeInfos.size() < maxSize) {
            Edge<V, E> edge = heap.remove();
            if (edgeUnionFind.isSame(edge.from, edge.to)) continue;

            edgeInfos.add(edgeInfo(edge));
            edgeUnionFind.union(edge.from, edge.to);
        }

        return edgeInfos;
    }

    public EdgeInfo<V, E> edgeInfo(Edge<V, E> edge) {
        return new EdgeInfo<V, E>(edge.from.value, edge.to.value, edge.weight);
    }

//    @Override
//    public Map<V, E> shortestPath(V begin) {
//        Vertex<V, E> beginVertex = this.vertecies.get(begin);
//        if (beginVertex == null) return null;
//
//        Map<V, E> selectedPath = new HashMap<>();
//        Map<Vertex<V, E>, E> paths = new HashMap<>();
//
//        for (Edge<V, E> outEdge : beginVertex.outEdges) {
//            paths.put(outEdge.to, outEdge.weight);
//        }
//
//
//        while (!paths.isEmpty()) {
//            Map.Entry<Vertex<V, E>, E> minEntry = getMinEntry(paths);
//            Vertex<V, E> Minvertex = minEntry.getKey();
//
//            selectedPath.put(Minvertex.value, minEntry.getValue());
//            paths.remove(Minvertex);
//
//            for (Edge<V, E> outEdge : Minvertex.outEdges) {
//                if (selectedPath.containsKey(outEdge.to.value) || outEdge.to.equals(beginVertex)) continue;
//                // 原权值 Minweight
//                E oldWeight = paths.get(outEdge.to);
//                // 现权值
//                E newWeight = this.weightManger.add(minEntry.getValue(), outEdge.weight);
//
//                if (oldWeight == null || this.weightManger.comparator(oldWeight, newWeight) > 0) {
//                    paths.put(outEdge.to, newWeight);
//                }
//            }
//        }
//        return selectedPath;
//    }

//    private Map.Entry<Vertex<V, E>, E> getMinEntry(Map<Vertex<V, E>, E> paths) {
//        Iterator<Map.Entry<Vertex<V, E>, E>> iterator = paths.entrySet().iterator();
//        Map.Entry<Vertex<V, E>, E> minEntry = iterator.next();
//        while (iterator.hasNext()) {
//            Map.Entry<Vertex<V, E>, E> nextEntry = iterator.next();
//            if (weightManger.comparator(nextEntry.getValue(), minEntry.getValue()) < 0) {
//                minEntry = nextEntry;
//            }
//        }
//        return minEntry;
//    }

    @Override
    public Map<V, PathInfos<V, E>> shortestPath(V begin) {
        return dijkstra(begin);
    }

    private Map<V, PathInfos<V, E>> dijkstra(V begin) {
        Vertex<V, E> beginVertex = this.vertecies.get(begin);
        if (beginVertex == null) return null;

        Map<V, PathInfos<V, E>> selectedPath = new HashMap<>();
        Map<Vertex<V, E>, PathInfos<V, E>> paths = new HashMap<>();

        for (Edge<V, E> outEdge : beginVertex.outEdges) {
            PathInfos<V, E> pathInfo = new PathInfos<V, E>();
            pathInfo.weight = outEdge.weight;
            pathInfo.edgeInfos.add(edgeInfo(outEdge));
            paths.put(outEdge.to, pathInfo);
        }

        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, PathInfos<V, E>> minEntry = getMinEntry(paths);
            Vertex<V, E> minVertex = minEntry.getKey();
            selectedPath.put(minVertex.value, minEntry.getValue());
            paths.remove(minVertex);

            for (Edge<V, E> outEdge : minVertex.outEdges) {
                if (selectedPath.containsKey(outEdge.to.value)) continue;

                relax(outEdge, paths, minEntry.getValue());

            }
        }
        selectedPath.remove(beginVertex.value);
        return selectedPath;
    }

    private Map<V, PathInfos<V, E>> bellmanFord(V begin) {
        Vertex<V, E> beginVertex = vertecies.get(begin);
        if (beginVertex == null ) return null;

        Map<V, PathInfos<V, E>> selectedPaths = new HashMap<>();
        selectedPaths.put(begin, new PathInfos<V, E>(this.weightManger.zero()));

        int maxSize = this.edges.size() - 1;
        for (int i = 0; i < maxSize; i++) {
            for (Edge<V, E> edge : this.edges) {
                PathInfos<V, E> fromPath = selectedPaths.get(edge.from.value);
                if (fromPath == null) continue;
                relaxForBellmanFord(edge, selectedPaths, fromPath);
            }
        }

        for (Edge<V, E> edge : this.edges) {
            PathInfos<V, E> fromPath = selectedPaths.get(edge.from.value);
            if (fromPath == null) continue;
            if (relaxForBellmanFord(edge, selectedPaths, fromPath)) {
                System.out.println("有负权环, 不存在最短路径");
                return null;
            }
        }

        selectedPaths.remove(begin);
        return selectedPaths;
    }

    private Map.Entry<Vertex<V, E>, PathInfos<V, E>> getMinEntry(Map<Vertex<V, E>, PathInfos<V, E>> paths) {
        Iterator<Map.Entry<Vertex<V, E>, PathInfos<V, E>>> iterator = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, PathInfos<V, E>> minEntry = iterator.next();
        while (iterator.hasNext()) {
            Map.Entry<Vertex<V, E>, PathInfos<V, E>> nextEntry = iterator.next();
            if (weightManger.comparator(nextEntry.getValue().weight, minEntry.getValue().weight) < 0) {
                minEntry = nextEntry;
            }
        }
        return minEntry;
    }

    private void relax(Edge<V, E> outEdge, Map<Vertex<V, E>, PathInfos<V, E>> paths, PathInfos<V, E> minPathInfos) {
        // 原权值
        PathInfos<V, E> oldPathInfo = paths.get(outEdge.to);

        // 新权值
        E newweight = weightManger.add(minPathInfos.weight, outEdge.weight);

        if (oldPathInfo != null && weightManger.comparator(oldPathInfo.weight, newweight) <= 0) return;

        if (oldPathInfo == null) {
            oldPathInfo = new PathInfos<V, E>();
        }else{
            oldPathInfo.edgeInfos.clear();
        }

        oldPathInfo.weight = newweight;
        oldPathInfo.edgeInfos.addAll(minPathInfos.edgeInfos);
        oldPathInfo.edgeInfos.add(edgeInfo(outEdge));
        paths.put(outEdge.to, oldPathInfo);
    }

    private boolean relaxForBellmanFord(Edge<V, E> outEdge, Map<V, PathInfos<V, E>> paths, PathInfos<V, E> minPathInfos) {
        // 原权值
        PathInfos<V, E> oldPathInfo = paths.get(outEdge.to);

        // 新权值
        E newweight = weightManger.add(minPathInfos.weight, outEdge.weight);

        if (oldPathInfo != null && weightManger.comparator(oldPathInfo.weight, newweight) <= 0) return false;

        if (oldPathInfo == null) {
            oldPathInfo = new PathInfos<V, E>();
        }else{
            oldPathInfo.edgeInfos.clear();
        }

        oldPathInfo.weight = newweight;
        oldPathInfo.edgeInfos.addAll(minPathInfos.edgeInfos);
        oldPathInfo.edgeInfos.add(edgeInfo(outEdge));
        paths.put(outEdge.to.value, oldPathInfo);
        return true;
    }

}
