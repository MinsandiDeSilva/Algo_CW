import java.util.ArrayList;
import java.util.List;

public class FlowNetwork {
    private final int numVertices; // Total Nodes(n)
    private final List<List<FlowEdge>> adj;

    public FlowNetwork(int numVertices) {
        this.numVertices = numVertices;
        adj = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adj.add(new ArrayList<>()); // Initialize empty lists
        }
    }

    //add an edge
    public void addEdge(int from, int to, int capacity) {
        validateVertex(from);
        validateVertex(to);
        adj.get(from).add(new FlowEdge(from, to, capacity));
    }

    /**
     * Returns edges leaving node v (SEARCH operation - Task 2 requirement)
     */
    public List<FlowEdge> getAdjacentEdges(int v) {
        validateVertex(v);
        return adj.get(v);
    }

    /**
     * Removes an edge from the network (DELETE operation for Task 2 marking scheme).
     * @param from Source node of the edge
     * @param to Target node of the edge
     * @return true if edge was found and removed, false otherwise
     */
    public boolean removeEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return adj.get(from).removeIf(edge -> edge.getTo() == to);
    }

    /**
     * Validates node index (0 ≤ v < vertexCount)
     */
    private void validateVertex(int v) {
        if (v < 0 || v >= numVertices)
            throw new IllegalArgumentException("Invalid node index: " + v);
    }


    /**
     * Returns total nodes
     */
    public int getVertexCount() {
        return numVertices;
    }

    /**
     * Prints network for debugging (examiner-friendly)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Flow Network (Nodes: ").append(numVertices).append(")\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        return sb.toString();
    }




}
