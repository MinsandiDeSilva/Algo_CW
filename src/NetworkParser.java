import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NetworkParser {
    /**
     * Parses a flow network from a file.
     *
     * @param filename Path to the input file
     * @return FlowNetwork object
     * @throws IOException If file is missing or malformed
     */

    public static FlowNetwork parse(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read number of nodes (first line)
            String line = reader.readLine();
            if (line == null) throw new IOException("Empty file");
            int nodeCount = Integer.parseInt(line.trim());

            // Create empty network
            FlowNetwork network = new FlowNetwork(nodeCount);

            // Read edges (remaining lines)
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // Skip blank lines

                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    throw new IOException("Invalid edge format: " + line);
                }

                // Parse edge (from, to, capacity)
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                int capacity = Integer.parseInt(parts[2]);

                network.addEdge(from, to, capacity);
            }
            return network;
        }
    }

}
