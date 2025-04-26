import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
    public static Graph parse(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("D:\\IIT\\2nd year\\2nd sem\\ALGO\\CW\\Algo_CW\\input.txt"));
        int numOfVertices = scanner.nextInt();  // First number = vertex count
        Graph graph = new Graph(numOfVertices);

        while(scanner.hasNext()){
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int capacity = scanner.nextInt();
            graph.addEdge(from,to,capacity);
        }
        scanner.close();
        return graph;
    }
}
