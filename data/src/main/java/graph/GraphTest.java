package graph;

import java.util.Map;
import java.util.Set;

public class GraphTest {
    
    public static void main(String[] args) {
        Graph<Object, Double> objectDoubleGraph = directedGraph(Data.NEGATIVE_WEIGHT1);
        Map<Object, Graph<Object, Double>.PathInfos<Object, Double>> a = objectDoubleGraph.shortestPath("A");
        if (a == null) return;
        a.forEach((v, path) -> {
            System.out.println(v + "->" +  path.toString());
        });
    }

    public static void test(){
        ListGraph<String, Integer> integerIntegerListGraph = new ListGraph<>(new Graph.WeightManger<Integer>() {
            @Override
            public int comparator(Integer w1, Integer w2) {
                return w1.compareTo(w2);
            }

            @Override
            public Integer add(Integer w1, Integer w2) {
                return w1 + w2;
            }

            @Override
            public Integer zero() {
                return 0;
            }
        });

        integerIntegerListGraph.addEdge("A", "B", 6);
        integerIntegerListGraph.addEdge("A", "D", 5);
        integerIntegerListGraph.addEdge("A", "C", 1);
        integerIntegerListGraph.addEdge("B", "E", 3);
        integerIntegerListGraph.addEdge("B", "C", 5);
        integerIntegerListGraph.addEdge("C", "E", 6);
        integerIntegerListGraph.addEdge("C", "F", 4);
        integerIntegerListGraph.addEdge("C", "D", 5);
        integerIntegerListGraph.addEdge("D", "F", 2);
        integerIntegerListGraph.addEdge("E", "F", 6);

        integerIntegerListGraph.addEdge("B", "A", 6);
        integerIntegerListGraph.addEdge("D", "A", 5);
        integerIntegerListGraph.addEdge("C", "A", 1);
        integerIntegerListGraph.addEdge("E", "B", 3);
        integerIntegerListGraph.addEdge("C", "B", 5);
        integerIntegerListGraph.addEdge("E", "C", 6);
        integerIntegerListGraph.addEdge("F", "C", 4);
        integerIntegerListGraph.addEdge("D", "C", 5);
        integerIntegerListGraph.addEdge("F", "D", 2);
        integerIntegerListGraph.addEdge("F", "E", 6);

        Set<Graph<String, Integer>.EdgeInfo<String, Integer>> mst = integerIntegerListGraph.mst();
        for (Graph<String, Integer>.EdgeInfo<String, Integer> stringIntegerEdgeInfo : mst) {
            System.out.println(stringIntegerEdgeInfo);
        }

    }

    static Graph.WeightManger<Double> weightManager = new Graph.WeightManger<Double>() {

        @Override
        public int comparator(Double w1, Double w2) {
            return w1.compareTo(w2);
        }

        @Override
        public Double add(Double w1, Double w2) {
            return w1 + w2;
        }

        @Override
        public Double zero() {
            return 0.0;
        }
    };

    /**
     * 有向图
     */
    private static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
            }
        }
        return graph;
    }

    /**
     * 无向图
     * @param data
     * @return
     */
    private static Graph<Object, Double> undirectedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
                graph.addEdge(edge[1], edge[0]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
                graph.addEdge(edge[1], edge[0], weight);
            }
        }
        return graph;
    }
}
