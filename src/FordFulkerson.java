//////import java.util.*;
//////import java.util.stream.Collectors;
//////
//////public class FordFulkerson {
//////    private final FlowNetwork network;
//////    private int maxFlow;
//////    private List<String> steps;
//////
//////
//////    //constructor
//////    public FordFulkerson(FlowNetwork network) {
//////        this.network = network;
//////        this.steps = new ArrayList<>();
//////    }
//////    public int computeMaxFlow(){
//////        int source = 0;
//////        int sink = network.getVertexCount() -1;
//////        maxFlow = 0;
//////
//////        //while there are augmenting paths
//////        while(true){
//////            // BFS to find the shortest augmenting path
//////            Queue<Integer> queue = new LinkedList<>();
//////            int[] parent = new int [network.getVertexCount()];
//////            Arrays.fill(parent, -1);
//////            queue.add(source);
//////            parent[source] = source;
//////
//////            boolean foundPath = false;
//////            System.out.println("\n[Debug] Starting BFS...");
//////
//////            while(!queue.isEmpty() && !foundPath){
//////                int current = queue.poll();
//////                System.out.println("\n[Debug] Exploring node " + current);
//////
//////                for(FlowEdge edge : network.getAdjacentEdges(current)){
//////                    int neighbor = edge.getTo();
//////                    int residual = edge.getResidualCapacity();
//////
//////                    System.out.printf("[Debug] Edge %d->%d: flow=%d, cap=%d, residual=%d%n", edge.getFrom(), edge.getTo(), edge.getFlow(),edge.getCapacity(), residual);
//////                    if(parent[neighbor] == -1 && edge.getResidualCapacity() > 0){
//////                        parent[neighbor] = current;
//////                        System.out.printf("[Debug] Found viable edge to %d (parent=%d)%n", neighbor, current);
//////
//////                        if (neighbor == sink){
//////                            foundPath = true;
//////                            System.out.println("[Debug] Reached sink!");
//////                            break;
//////                        }
//////                        queue.add(neighbor);
//////
//////                    }
//////                }
//////            }
//////
////////            if (!foundPath) break; // No more augmenting paths
//////            if (!foundPath) {
//////                System.out.println("[Debug] No more augmenting paths");
//////                break;
//////            }
//////
//////            //calculate bottleneck capacity
//////            int bottleneck = Integer.MAX_VALUE;
//////            List<Integer> path = new ArrayList<>();
//////            for(int v = sink; v !=source; v = parent[v]){
//////                int u = parent[v];
//////                FlowEdge edge = getEdge(u, v);
//////                bottleneck = Math.min(bottleneck, edge.getResidualCapacity());
//////                path.add(v);
//////            }
//////            path.add(source);
//////            Collections.reverse(path);
//////
//////            //update flow along the path
//////            for (int v = sink; v!=source; v = parent[v]){
//////                int u = parent[v];
//////                FlowEdge edge = getEdge(u,v);
//////                edge.addFlow(bottleneck);
//////            }
//////
//////            maxFlow += bottleneck;
//////            String pathStr = path.stream()
//////                    .map(Object::toString)
//////                    .collect(Collectors.joining("->"));
//////            steps.add("Augmenting path: " + buildPath(parent,source,sink) + " | Added flow: " +bottleneck);
//////
//////            System.out.println("\n[Debug] After augmentation:");
//////            printResidualNetwork();
//////
//////        }
//////        return maxFlow;
//////
//////    }
//////    private FlowEdge getEdge(int u, int v) {
//////        for (FlowEdge edge : network.getAdjacentEdges(u)) {
//////            if (edge.getTo() == v) return edge;
//////        }
//////        throw new IllegalArgumentException("Edge not found");
//////    }
//////
//////    private void printResidualNetwork() {
//////        for (int i = 0; i < network.getVertexCount(); i++) {
//////            System.out.print(i + ": [");
//////            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//////                System.out.printf("%d->%d (%d/%d), ",
//////                        edge.getFrom(), edge.getTo(),
//////                        edge.getFlow(), edge.getCapacity());
//////            }
//////            System.out.println("]");
//////        }
//////    }
//////
//////    private String buildPath(int[] parent, int source, int sink) {
//////        List<Integer> path = new ArrayList<>();
//////        for (int v = sink; v != source; v = parent[v]) {
//////            path.add(v);
//////        }
//////        path.add(source);
//////        Collections.reverse(path);
//////        return path.stream().map(Object::toString).collect(Collectors.joining("->"));
//////    }
//////
//////    public List<String> getSteps() {
//////        return steps;
//////    }
//////
//////    /** Returns flow distribution */
//////    public Map<FlowEdge, Integer> getFlowDistribution() {
//////        Map<FlowEdge, Integer> flowMap = new HashMap<>();
//////        for (int i = 0; i < network.getVertexCount(); i++) {
//////            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//////                flowMap.put(edge, edge.getFlow());
//////            }
//////        }
//////        return flowMap;
//////    }
//////
//////
//////
//////}
////
////
////import java.util.*;
////import java.util.stream.Collectors;
////
////public class FordFulkerson {
////    private final FlowNetwork network;
////    private int maxFlow;
////    private List<String> steps;
////
////    public FordFulkerson(FlowNetwork network) {
////        this.network = network;
////        this.steps = new ArrayList<>();
////    }
////
////    public int computeMaxFlow() {
////        int source = 0;
////        int sink = network.getVertexCount() - 1;
////        maxFlow = 0;
////
////        while (true) {
////            // BFS to find augmenting path
////            Queue<Integer> queue = new LinkedList<>();
////            int[] parent = new int[network.getVertexCount()];
////            Arrays.fill(parent, -1);
////
////            queue.add(source);
////            parent[source] = source;
////            boolean foundPath = false;
////
////            while (!queue.isEmpty() && !foundPath) {
////                int current = queue.poll();
////
////                for (FlowEdge edge : network.getAdjacentEdges(current)) {
////                    int neighbor = edge.getTo();
////                    if (parent[neighbor] == -1 && edge.getResidualCapacity() > 0) {
////                        parent[neighbor] = current;
////                        if (neighbor == sink) {
////                            foundPath = true;
////                            break;
////                        }
////                        queue.add(neighbor);
////                    }
////                }
////            }
////
////            if (!foundPath) break;
////
////            // Calculate bottleneck capacity
////            int bottleneck = Integer.MAX_VALUE;
////            for (int v = sink; v != source; v = parent[v]) {
////                int u = parent[v];
////                bottleneck = Math.min(bottleneck, getEdge(u, v).getResidualCapacity());
////            }
////
////            // Update flow along the path
////            for (int v = sink; v != source; v = parent[v]) {
////                int u = parent[v];
////                getEdge(u, v).addFlow(bottleneck);
////            }
////
////            maxFlow += bottleneck;
////            steps.add("Augmenting path: " + buildPath(parent, source, sink) +
////                    " | Added flow: " + bottleneck);
////        }
////
////        return maxFlow;
////    }
////
////    private FlowEdge getEdge(int u, int v) {
////        for (FlowEdge edge : network.getAdjacentEdges(u)) {
////            if (edge.getTo() == v) return edge;
////        }
////        throw new IllegalArgumentException("Edge not found");
////    }
////
////    private String buildPath(int[] parent, int source, int sink) {
////        List<Integer> path = new ArrayList<>();
////        for (int v = sink; v != source; v = parent[v]) {
////            path.add(v);
////        }
////        path.add(source);
////        Collections.reverse(path);
////        return path.stream().map(Object::toString).collect(Collectors.joining("->"));
////    }
////
////    public List<String> getSteps() {
////        return steps;
////    }
////
////    public Map<FlowEdge, Integer> getFlowDistribution() {
////        Map<FlowEdge, Integer> flowMap = new HashMap<>();
////        for (int i = 0; i < network.getVertexCount(); i++) {
////            for (FlowEdge edge : network.getAdjacentEdges(i)) {
////                flowMap.put(edge, edge.getFlow());
////            }
////        }
////        return flowMap;
////    }
////}
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class FordFulkerson {
//    private final FlowNetwork network;
//    private int maxFlow;
//    private List<String> steps;
//
//    public FordFulkerson(FlowNetwork network) {
//        this.network = network;
//        this.steps = new ArrayList<>();
//    }
//
//    public int computeMaxFlow() {
//        int source = 0;
//        int sink = network.getVertexCount() - 1;
//        maxFlow = 0;
//        int iteration = 0;
//
//        while (true) {
//            iteration++;
//            // BFS to find augmenting path
//            Queue<Integer> queue = new LinkedList<>();
//            int[] parent = new int[network.getVertexCount()];
//            Arrays.fill(parent, -1);
//
//            queue.add(source);
//            parent[source] = source;
//            boolean foundPath = false;
//
//            while (!queue.isEmpty() && !foundPath) {
//                int current = queue.poll();
//
//                for (FlowEdge edge : network.getAdjacentEdges(current)) {
//                    int neighbor = edge.getTo();
//                    if (parent[neighbor] == -1 && edge.getResidualCapacity() > 0) {
//                        parent[neighbor] = current;
//                        if (neighbor == sink) {
//                            foundPath = true;
//                            break;
//                        }
//                        queue.add(neighbor);
//                    }
//                }
//            }
//
//            if (!foundPath) break;
//
//            // Calculate bottleneck capacity
//            int bottleneck = Integer.MAX_VALUE;
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                FlowEdge edge = getEdge(u, v);
//                bottleneck = Math.min(bottleneck, edge.getResidualCapacity());
//            }
//
//            // Update flow along the path
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                getEdge(u, v).addFlow(bottleneck);
//            }
//
//            maxFlow += bottleneck;
//            steps.add(String.format("Iteration %02d : Aug Path = %s, Bottleneck = %d, Total Flow = %d",
//                    iteration,
//                    buildPath(parent, source, sink),
//                    bottleneck,
//                    maxFlow));
//        }
//
//        return maxFlow;
//    }
//
//    private FlowEdge getEdge(int u, int v) {
//        for (FlowEdge edge : network.getAdjacentEdges(u)) {
//            if (edge.getTo() == v) return edge;
//        }
//        throw new IllegalArgumentException("Edge not found: " + u + "->" + v);
//    }
//
//    private String buildPath(int[] parent, int source, int sink) {
//        List<Integer> path = new ArrayList<>();
//        for (int v = sink; v != source; v = parent[v]) {
//            path.add(v);
//        }
//        path.add(source);
//        Collections.reverse(path);
//        return path.stream().map(Object::toString).collect(Collectors.joining("->"));
//    }
//
//    public List<String> getSteps() {
//        return steps;
//    }
//
//    public Map<FlowEdge, Integer> getFlowDistribution() {
//        Map<FlowEdge, Integer> flowMap = new HashMap<>();
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//                flowMap.put(edge, edge.getFlow());
//            }
//        }
//        return flowMap;
//    }
//}


////before changing the algo part
import java.util.*;
import java.util.stream.Collectors;

public class FordFulkerson {
    private final FlowNetwork network;
    private int maxFlow;
    private List<String> steps;

    public FordFulkerson(FlowNetwork network) {
        this.network = network;
        this.steps = new ArrayList<>();
    }

    public int computeMaxFlow() {
        int source = 0;
        int sink = network.getVertexCount() - 1;
        maxFlow = 0;
        int iteration = 0;

        while (true) {
            iteration++;
            // BFS with memory-efficient path tracking
            int[] parent = new int[network.getVertexCount()];
            Arrays.fill(parent, -1);
            parent[source] = source;

            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);
            boolean foundPath = false;

            while (!queue.isEmpty() && !foundPath) {
                int current = queue.poll();

                //to get the correct flow
                // Sort edges by 'to' node to enforce a fixed order
                List<FlowEdge> edges = new ArrayList<>(network.getAdjacentEdges(current));
                edges.sort(Comparator.comparingInt(FlowEdge::getTo)); // Sort edges by 'to' node

                for (FlowEdge edge : edges) { // Process edges in sorted order
                    int neighbor = edge.getTo();
                    if (parent[neighbor] == -1 && edge.getResidualCapacity() > 0) {
                        parent[neighbor] = current;
                        if (neighbor == sink) {
                            foundPath = true;
                            break;
                        }
                        queue.add(neighbor);
                    }
                }
            }

            if (!foundPath) break;

            // Calculate bottleneck with direct iteration
            int bottleneck = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                bottleneck = Math.min(bottleneck, getEdge(u, v).getResidualCapacity());
            }

            // Update flow
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                getEdge(u, v).addFlow(bottleneck);
            }

            maxFlow += bottleneck;

            // Memory-efficient path building
            steps.add(formatStep(iteration, parent, source, sink, bottleneck, maxFlow));
        }
        return maxFlow;
    }

    private String formatStep(int iteration, int[] parent, int source, int sink,
                              int bottleneck, int totalFlow) {
        // Build path directly without intermediate collections
        StringBuilder path = new StringBuilder();
        int node = sink;
        while (node != source) {
            path.insert(0, "->" + node);
            node = parent[node];
        }
        path.insert(0, source);

        return String.format("Iteration %02d : Aug Path = %s, Bottleneck = %d, Total Flow = %d",
                iteration, path.toString(), bottleneck, totalFlow);
    }

    private FlowEdge getEdge(int u, int v) {
        for (FlowEdge edge : network.getAdjacentEdges(u)) {
            if (edge.getTo() == v) return edge;
        }
        throw new IllegalArgumentException("Edge not found: " + u + "->" + v);
    }

    public List<String> getSteps() {
        return steps;
    }

    public Map<FlowEdge, Integer> getFlowDistribution() {
        Map<FlowEdge, Integer> flowMap = new HashMap<>();
        for (int i = 0; i < network.getVertexCount(); i++) {
            for (FlowEdge edge : network.getAdjacentEdges(i)) {
                flowMap.put(edge, edge.getFlow());
            }
        }
        return flowMap;
    }
}

//
////chaninging the logic
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class FordFulkerson {
//    private final FlowNetwork network;
//    private int maxFlow;
//    private List<String> steps;
//
//    public FordFulkerson(FlowNetwork network) {
//        this.network = network;
//        this.steps = new ArrayList<>();
//    }
//
//    public int computeMaxFlow() {
//        int source = 0;
//        int sink = network.getVertexCount() - 1;
//        maxFlow = 0;
//        int iteration = 0;
//
//        while (true) {
//            iteration++;
//            // BFS to find augmenting path
//            Queue<Integer> queue = new LinkedList<>();
//            int[] parent = new int[network.getVertexCount()];
//            Arrays.fill(parent, -1);
//
//            queue.add(source);
//            parent[source] = source;
//            boolean foundPath = false;
//
//            while (!queue.isEmpty() && !foundPath) {
//                int current = queue.poll();
//
//                for (FlowEdge edge : network.getAdjacentEdges(current)) {
//                    int neighbor = edge.getTo();
//                    // Check residual capacity (forward edge: capacity - flow, backward edge: flow)
//                    int residual = (edge.getFrom() == current)
//                            ? edge.getCapacity() - edge.getFlow()
//                            : edge.getFlow();
//
//                    if (parent[neighbor] == -1 && residual > 0) {
//                        parent[neighbor] = current;
//                        if (neighbor == sink) {
//                            foundPath = true;
//                            break;
//                        }
//                        queue.add(neighbor);
//                    }
//                }
//            }
//
//            if (!foundPath) break;
//
//            // Calculate bottleneck capacity
//            int bottleneck = Integer.MAX_VALUE;
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                FlowEdge edge = getEdge(u, v);
//                int residual = (edge.getFrom() == u)
//                        ? edge.getCapacity() - edge.getFlow()
//                        : edge.getFlow();
//                bottleneck = Math.min(bottleneck, residual);
//            }
//
//            // Update flow along the path
//            for (int v = sink; v != source; v = parent[v]) {
//                int u = parent[v];
//                FlowEdge edge = getEdge(u, v);
//                if (edge.getFrom() == u) {
//                    // Forward edge: increase flow
//                    edge.addFlow(bottleneck);
//                } else {
//                    // Backward edge: decrease flow
//                    edge.addFlow(-bottleneck);
//                }
//            }
//
//            maxFlow += bottleneck;
//            steps.add(String.format(
//                    "Iteration %02d : Aug Path = %s, Bottleneck = %d, Total Flow = %d",
//                    iteration,
//                    buildPath(parent, source, sink),
//                    bottleneck,
//                    maxFlow
//            ));
//        }
//
//        // Validate flow conservation and capacity constraints
//        validateFlow();
//        return maxFlow;
//    }
//
//    private FlowEdge getEdge(int u, int v) {
//        for (FlowEdge edge : network.getAdjacentEdges(u)) {
//            if (edge.getTo() == v || edge.getFrom() == v) {
//                return edge;
//            }
//        }
//        throw new IllegalArgumentException("Edge not found: " + u + "->" + v);
//    }
//
//    private String buildPath(int[] parent, int source, int sink) {
//        List<Integer> path = new ArrayList<>();
//        for (int v = sink; v != source; v = parent[v]) {
//            path.add(v);
//        }
//        path.add(source);
//        Collections.reverse(path);
//        return path.stream().map(Object::toString).collect(Collectors.joining("->"));
//    }
//
//    private void validateFlow() {
//        // Check no edge exceeds capacity
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//                if (edge.getFlow() > edge.getCapacity()) {
//                    throw new IllegalStateException(
//                            "Flow exceeds capacity on edge " + edge.getFrom() + "->" + edge.getTo() +
//                                    ": " + edge.getFlow() + "/" + edge.getCapacity()
//                    );
//                }
//            }
//        }
//
//        // Check flow conservation (except source and sink)
//        int source = 0;
//        int sink = network.getVertexCount() - 1;
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            if (i == source || i == sink) continue;
//            int inflow = 0;
//            int outflow = 0;
//            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//                if (edge.getTo() == i) inflow += edge.getFlow();
//                if (edge.getFrom() == i) outflow += edge.getFlow();
//            }
//            if (inflow != outflow) {
//                throw new IllegalStateException(
//                        "Flow conservation violated at node " + i +
//                                ": inflow=" + inflow + ", outflow=" + outflow
//                );
//            }
//        }
//    }
//
//    public List<String> getSteps() {
//        return steps;
//    }
//
//    public Map<FlowEdge, Integer> getFlowDistribution() {
//        Map<FlowEdge, Integer> flowMap = new HashMap<>();
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            for (FlowEdge edge : network.getAdjacentEdges(i)) {
//                flowMap.put(edge, edge.getFlow());
//            }
//        }
//        return flowMap;
//    }
//}