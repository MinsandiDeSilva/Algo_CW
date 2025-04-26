import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private int numVertices;  // Number of vertices (nodes) in the graph
//    ArrayList<LinkedList<Parser>> adjacentList ;
    private List<Edge> [] adj;  // Adjacency list: Array of lists of edges

    class Edge{
        int to; //Target node
        int capacity; //Max flow capacity
        int flow; //Current flow
        Edge reverse; //Reverse edge for residual graph

        public Edge(int to, int capacity) {
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;  //Initially no flow is passing through
        }
    }

    //constructor

    public Graph(int numVertices){
        this.numVertices = numVertices;
        adj = new ArrayList[numVertices];  //create an array of lists
        for (int i =0 ;i < numVertices; i++){
            adj[i] = new ArrayList<>();    //Each node starts with an empty list of edges
        }
    }

    //Add edges to graph with residual edge
    public void addEdge(int from, int to, int capacity){
        Edge forward = new Edge(to, capacity); //Original edge
        Edge backward = new Edge(from,0); //Residual edge

        //Link them so we can access the residual edge kater
        forward.reverse = backward;
        backward.reverse = forward;

        //Add original edge to 'from's adjacency list
        adj[from].add(forward);

        // Add residual edge to 'to's adjacency list
        adj[to].add(backward);
    }

    // Get list of edges from a node
    public List<Edge> getAdj(int numVertices) {
        return adj[numVertices];
    }

    // Get number of vertices
    public int getNumVertices() {
        return numVertices;
    }

}
