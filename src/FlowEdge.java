/**
 * Represents a directed edge in the flow network.
 */
public class FlowEdge {
    private final int from; // Source node
    private final int to;   // Target node
    private final int capacity;  // Maximum capacity
    private int flow;   // Current flow (initially 0)

    public FlowEdge(int from, int to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = 0;   // Initial flow = 0
    }

    //Getters
    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    //Residual Capacity
    public int getResidualCapacity() {
        return capacity - flow;
    }

    //Update flow(adding)
    public void addFlow(int amount) {
        flow += amount;
        if (flow > capacity) throw new IllegalArgumentException("Flow exceeds capacity!");
    }

    @Override
    public String toString() {
        return String.format("%d->%d (%d/%d)", from, to, flow, capacity);
    }

}
