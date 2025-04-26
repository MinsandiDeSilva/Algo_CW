import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class FordFulkerson {
    /**
     * Computes maximum flow in a flow network using BFS (Edmonds-Karp variant)
     *
     * @param g The flow network graph
     * @param source Source node
     * @param sink Sink node
     * @return Maximum flow value
     */
    public static int maxFlow(Graph g, int source, int sink) {
        int maxFlow = 0; // Initialize maximum flow to 0

        // Array to store parent nodes for path reconstruction
        int[] parent = new int[g.getNumVertices()];

        // Continue while augmenting paths exist
        while (bfs(g, source, sink, parent)) {
            // Find bottleneck capacity in the found path
            int pathFlow = Integer.MAX_VALUE;

            // Trace back from sink to source to find minimum residual capacity
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, getResidualCapacity(g, u, v));
            }

            // Update flow values along the path
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                updateFlow(g, u, v, pathFlow);
            }

            // Add this path's flow to the total
            maxFlow += pathFlow;

            // Print augmentation details (for debugging/understanding)
            System.out.println("Augmenting Path Flow: " + pathFlow +
                    ", Total Flow: " + maxFlow);
        }

        return maxFlow;
    }

    /**
     * BFS implementation to find an augmenting path
     */
    private static boolean bfs(Graph g, int source, int sink, int[] parent) {
        // Initialize parent array with -1 (no parent)
        Arrays.fill(parent, -1);

        // Mark source as visited (special value -2)
        parent[source] = -2;

        // Standard BFS queue
        Queue<Integer> q = new LinkedList<>();
        q.add(source);

        while (!q.isEmpty()) {
            int u = q.poll();

            // Explore all adjacent edges
            for (Graph.Edge e : g.getAdj(u)) {
                // If node not visited and residual capacity exists
                if (parent[e.to] == -1 && e.capacity > e.flow) {
                    parent[e.to] = u; // Set parent

                    // If we reached the sink, return true
                    if (e.to == sink) {
                        return true;
                    }

                    q.add(e.to);
                }
            }
        }

        // No augmenting path found
        return false;
    }

    /**
     * Helper to get residual capacity of edge u→v
     */
    private static int getResidualCapacity(Graph g, int u, int v) {
        for (Graph.Edge e : g.getAdj(u)) {
            if (e.to == v) {
                return e.capacity - e.flow;
            }
        }
        return 0;
    }

    /**
     * Helper to update flow along edge u→v and its residual
     */
    private static void updateFlow(Graph g, int u, int v, int flow) {
        for (Graph.Edge e : g.getAdj(u)) {
            if (e.to == v) {
                e.flow += flow;           // Increase forward flow
                e.reverse.flow -= flow;   // Decrease residual flow
                break;
            }
        }
    }
}