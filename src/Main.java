import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Parse input file into a graph
            Graph g = Parser.parse("input.txt");

            // Step 2: Identify source (0) and sink (last node)
            int source = 0;
            int sink = g.getNumVertices() - 1;

            // Step 3: Compute maximum flow using Ford-Fulkerson
            int maxFlow = FordFulkerson.maxFlow(g, source, sink);

            // Step 4: Output the result
            System.out.println("Maximum Flow: " + maxFlow);

        } catch (FileNotFoundException e) {
            System.err.println("Error: Input file not found!");
            e.printStackTrace();
        }
    }
}