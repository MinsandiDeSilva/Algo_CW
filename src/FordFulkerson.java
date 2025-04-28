import java.util.*;
import java.util.stream.Collectors;

public class FordFulkerson {
    private final FlowNetwork network;
    private int maxFlow;
    private List<String> steps;
    private static final int MAX_DISPLAY_STEPS = 100; // Only affects display

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

            // BFS to find augmenting path
            Queue<Integer> queue = new LinkedList<>();
            int[] parent = new int[network.getVertexCount()];
            Arrays.fill(parent, -1);

            queue.add(source);
            parent[source] = source;
            boolean foundPath = false;

            while (!queue.isEmpty() && !foundPath) {
                int current = queue.poll();

                // Check forward edges
                for (FlowEdge edge : network.getAdjacentEdges(current)) {
                    int neighbor = edge.getTo();
                    int residual = edge.getCapacity() - edge.getFlow();

                    if (parent[neighbor] == -1 && residual > 0) {
                        parent[neighbor] = current;
                        if (neighbor == sink) {
                            foundPath = true;
                            break;
                        }
                        queue.add(neighbor);
                    }
                }

                // Check backward edges
                if (!foundPath) {
                    for (int j = 0; j < network.getVertexCount(); j++) {
                        for (FlowEdge edge : network.getAdjacentEdges(j)) {
                            if (edge.getTo() == current && edge.getFlow() > 0) {
                                if (parent[j] == -1) {
                                    parent[j] = current;
                                    queue.add(j);
                                }
                            }
                        }
                    }
                }
            }

            if (!foundPath) break;

            // Calculate bottleneck capacity
            int bottleneck = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                FlowEdge edge = getEdge(u, v);
                int residual = (edge.getFrom() == u)
                        ? edge.getCapacity() - edge.getFlow()
                        : edge.getFlow();
                bottleneck = Math.min(bottleneck, residual);
            }

            // Update flow along the path
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                FlowEdge edge = getEdge(u, v);
                if (edge.getFrom() == u) {
                    edge.addFlow(bottleneck);
                } else {
                    edge.addFlow(-bottleneck);
                }
            }

            maxFlow += bottleneck;

            // Only store first 100 steps for display
            if (steps.size() < MAX_DISPLAY_STEPS) {
                steps.add(String.format(
                        "Step %03d: path = %s, bottleneck = %d, flow = %d",
                        iteration,
                        buildPath(parent, source, sink),
                        bottleneck,
                        maxFlow
                ));
            }
        }

        validateFlow();
        return maxFlow;
    }

    private FlowEdge getEdge(int u, int v) {
        for (FlowEdge edge : network.getAdjacentEdges(u)) {
            if (edge.getTo() == v) return edge;
        }
        for (FlowEdge edge : network.getAdjacentEdges(v)) {
            if (edge.getTo() == u) return edge;
        }
        throw new IllegalArgumentException("Edge not found: " + u + "->" + v);
    }

    private String buildPath(int[] parent, int source, int sink) {
        List<Integer> path = new ArrayList<>();
        for (int v = sink; v != source; v = parent[v]) {
            path.add(v);
        }
        path.add(source);
        Collections.reverse(path);
        return path.stream().map(Object::toString).collect(Collectors.joining("->"));
    }

    private void validateFlow() {
        // Check capacity constraints
        for (int i = 0; i < network.getVertexCount(); i++) {
            for (FlowEdge edge : network.getAdjacentEdges(i)) {
                if (edge.getFlow() < 0 || edge.getFlow() > edge.getCapacity()) {
                    throw new IllegalStateException(
                            "Invalid flow on edge " + edge.getFrom() + "->" + edge.getTo() +
                                    ": " + edge.getFlow() + "/" + edge.getCapacity()
                    );
                }
            }
        }

        // Check flow conservation
        int source = 0;
        int sink = network.getVertexCount() - 1;
        for (int i = 0; i < network.getVertexCount(); i++) {
            if (i == source || i == sink) continue;

            int inflow = 0;
            int outflow = 0;

            // Calculate inflow
            for (int j = 0; j < network.getVertexCount(); j++) {
                for (FlowEdge edge : network.getAdjacentEdges(j)) {
                    if (edge.getTo() == i) inflow += edge.getFlow();
                }
            }

            // Calculate outflow
            for (FlowEdge edge : network.getAdjacentEdges(i)) {
                outflow += edge.getFlow();
            }

            if (inflow != outflow) {
                throw new IllegalStateException(
                        "Flow conservation violated at node " + i +
                                ": inflow=" + inflow + ", outflow=" + outflow
                );
            }
        }
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(steps);
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
