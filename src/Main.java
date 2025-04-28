////////import java.io.IOException;
////////import java.util.List;
////////
/////////**
//////// * Main class for Network Flow Coursework
//////// * Student ID: [20231157]
//////// * Name: [Minsandi De Silva]
//////// */
////////public class Main {
////////    public static void main(String[] args) {
////////        System.out.println("Network Flow Algorithm ");
////////
//////////        FlowNetwork network = new FlowNetwork(4);
//////////
//////////        // Add edges
//////////        network.addEdge(0, 1, 6); // 0→1 (cap=6)
//////////        network.addEdge(0, 2, 4); // 0→2 (cap=4)
//////////        network.addEdge(1, 2, 2); // 1→2 (cap=2)
//////////        network.addEdge(1, 3, 3); // 1→3 (cap=3)
//////////        network.addEdge(2, 3, 5); // 2→3 (cap=5)
//////////
//////////        // Print network
//////////        System.out.println(network);
//////////
//////////        // Example: Get edges from node 0 (SEARCH operation)
//////////        List<FlowEdge> edgesFrom0 = network.getAdjacentEdges(0);
//////////        System.out.println("Edges from node 0: " + edgesFrom0);
////////
//////////        try {
//////////            // Parse the input file (e.g., "input/network1.txt")
//////////            FlowNetwork network = NetworkParser.parse("input.txt");
//////////
//////////            // Print the parsed network (for examiner verification)
//////////            System.out.println("=== Parsed Network ===");
//////////            System.out.println(network);
//////////
//////////            // Print all edges (debugging)
//////////            System.out.println("\n=== Edge List ===");
//////////            for (int i = 0; i < network.getVertexCount(); i++) {
//////////                for (FlowEdge edge : network.getAdjacentEdges(i)) {
//////////                    System.out.println(edge);
//////////                }
//////////            }
//////////        } catch (IOException e) {
//////////            System.err.println("Error parsing file: " + e.getMessage());
//////////        }
////////
//////////        try {
//////////            // 1. Parse the input file
//////////            FlowNetwork network = NetworkParser.parse("input_2.txt");
//////////            System.out.println("=== Initial Network ===");
//////////            System.out.println(network);
//////////
//////////            // 2. Compute max flow
//////////            FordFulkerson solver = new FordFulkerson(network);
//////////            int maxFlow = solver.computeMaxFlow();
//////////
//////////            // 3. Print results (for examiner)
//////////            System.out.println("\n=== Algorithm Steps ===");
//////////            solver.getSteps().forEach(System.out::println);
//////////
//////////            System.out.println("\n=== Maximum Flow ===");
//////////            System.out.println("Value: " + maxFlow);
//////////
//////////            System.out.println("\n=== Flow Distribution ===");
//////////            solver.getFlowDistribution().forEach((edge, flow) ->
//////////                    System.out.println(edge.getFrom() + "->" + edge.getTo() + ": " +
//////////                            flow + "/" + edge.getCapacity()));
//////////        } catch (IOException e) {
//////////            System.err.println("Error: " + e.getMessage());
//////////        }
////////
////////    }
////////}
//////
//////import java.io.IOException;
//////import java.util.List;
//////import java.util.Map;
//////import java.util.HashMap;
//////import java.io.File;
//////import java.util.Date;
//////import java.text.SimpleDateFormat;
//////
//////public class Main {
//////    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////
//////    public static void main(String[] args) {
//////        System.out.println("Network Flow Algorithm - Coursework Solution");
//////
//////        // Process all bridge and ladder files
//////        processFiles("bridge");
//////        processFiles("ladder");
//////
//////        // Generate summary (this would be written to summary.txt)
//////        System.out.println("\n=== Summary ===");
//////        System.out.println("All networks processed successfully at " + dateFormat.format(new Date()));
//////    }
//////
//////    private static void processFiles(String prefix) {
//////        for (int i = 1; i <= 15; i++) {
//////            String filename = prefix + "_" + i + ".txt";
//////            String outputFilename = prefix + "_" + i + "_output.txt";
//////
//////            try {
//////                // Check if file exists
//////                if (!new File(filename).exists()) {
//////                    System.out.println("\nFile not found: " + filename);
//////                    continue;
//////                }
//////
//////                System.out.println("\nProcessing: " + filename);
//////
//////                // Record start time
//////                long startTime = System.currentTimeMillis();
//////
//////                // 1. Parse the input file
//////                FlowNetwork network = NetworkParser.parse(filename);
//////                System.out.println("Network successfully loaded!!");
//////
//////                // Count sources and sinks
//////                Map<Integer, Boolean> sources = new HashMap<>();
//////                Map<Integer, Boolean> sinks = new HashMap<>();
//////                for (int j = 0; j < network.getVertexCount(); j++) {
//////                    sources.put(j, true);
//////                    sinks.put(j, true);
//////                }
//////
//////                for (int j = 0; j < network.getVertexCount(); j++) {
//////                    for (FlowEdge edge : network.getAdjacentEdges(j)) {
//////                        sources.remove(edge.getTo());
//////                        sinks.remove(edge.getFrom());
//////                    }
//////                }
//////
//////                // 2. Compute max flow
//////                FordFulkerson solver = new FordFulkerson(network);
//////                int maxFlow = solver.computeMaxFlow();
//////
//////                // Record end time
//////                long endTime = System.currentTimeMillis();
//////                long executionTime = endTime - startTime;
//////
//////                // 3. Generate output
//////                System.out.println("Vertices: " + network.getVertexCount());
//////                System.out.println("Edges: " + countEdges(network));
//////                System.out.println("Sources: " + sources.keySet());
//////                System.out.println("Sinks: " + sinks.keySet());
//////                System.out.println("Max flow from source to sink: " + maxFlow);
//////                System.out.println("Execution time: " + executionTime + " ms");
//////
//////                // Print incremental improvements
//////                System.out.println("\nIncremental improvements:");
//////                solver.getSteps().forEach(System.out::println);
//////
//////                // Print flow distribution
//////                System.out.println("\nFlow distribution:");
//////                solver.getFlowDistribution().forEach((edge, flow) ->
//////                        System.out.println(edge.getFrom() + "->" + edge.getTo() + ": " +
//////                                flow + "/" + edge.getCapacity()));
//////
//////                System.out.println("\nOutput written to: " + outputFilename);
//////
//////            } catch (IOException e) {
//////                System.err.println("Error processing " + filename + ": " + e.getMessage());
//////            }
//////        }
//////    }
//////
//////    private static int countEdges(FlowNetwork network) {
//////        int count = 0;
//////        for (int i = 0; i < network.getVertexCount(); i++) {
//////            count += network.getAdjacentEdges(i).size();
//////        }
//////        return count;
//////    }
//////}
////
////
//////import java.io.IOException;
//////import java.util.List;
//////import java.util.Map;
//////import java.util.HashMap;
//////import java.io.File;
//////import java.util.Date;
//////import java.text.SimpleDateFormat;
//////import java.nio.file.Files;
//////import java.nio.file.Paths;
//////
//////public class Main {
//////    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////    private static final String INPUT_DIR = "benchmarks/";
//////    private static final String OUTPUT_DIR = "output/";
//////
//////    public static void main(String[] args) {
//////        System.out.println("Network Flow Analysis\n");
//////
//////        // Create directories if they don't exist
//////        new File(INPUT_DIR).mkdirs();
//////        new File(OUTPUT_DIR).mkdirs();
//////
//////        // Process all bridge and ladder files
//////        processFiles("bridge");
//////        processFiles("ladder");
//////    }
//////
//////    private static void processFiles(String prefix) {
//////        for (int i = 1; i <= 15; i++) {
//////            String inputPath = INPUT_DIR + prefix + "_" + i + ".txt";
//////            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
//////
//////            try {
//////                File inputFile = new File(inputPath);
//////                if (!inputFile.exists()) continue;
//////
//////                // 1. Parse the network
//////                long startTime = System.currentTimeMillis();
//////                FlowNetwork network = NetworkParser.parse(inputPath);
//////                System.out.println("Network successfully loaded!!");
//////
//////                // 2. Identify sources and sinks
//////                Map<Integer, Boolean> isSource = new HashMap<>();
//////                Map<Integer, Boolean> isSink = new HashMap<>();
//////                for (int j = 0; j < network.getVertexCount(); j++) {
//////                    isSource.put(j, true);
//////                    isSink.put(j, true);
//////                }
//////
//////                for (int j = 0; j < network.getVertexCount(); j++) {
//////                    for (FlowEdge edge : network.getAdjacentEdges(j)) {
//////                        isSource.remove(edge.getTo());
//////                        isSink.remove(edge.getFrom());
//////                    }
//////                }
//////
//////                // 3. Compute max flow
//////                FordFulkerson solver = new FordFulkerson(network);
//////                int maxFlow = solver.computeMaxFlow();
//////                long endTime = System.currentTimeMillis();
//////
//////                // 4. Display essential output
//////                System.out.println("Vertices: " + network.getVertexCount());
//////                System.out.println("Edges: " + countEdges(network));
//////                System.out.println("Sources: " + isSource.keySet());
//////                System.out.println("Sinks: " + isSink.keySet());
//////                System.out.println("Max flow from source to sink: " + maxFlow);
//////                System.out.println("Execution time: " + (endTime - startTime) + " ms");
//////
//////                // 5. Show incremental improvements
//////                System.out.println("\nIncremental improvements:");
//////                solver.getSteps().forEach(System.out::println);
//////                System.out.println("----------------------------------------");
//////
//////                // 6. Save full results to output file
//////                saveResultsToFile(outputPath, network, isSource, isSink, maxFlow,
//////                        endTime - startTime, solver);
//////
//////            } catch (IOException e) {
//////                System.err.println("Error processing " + inputPath);
//////            }
//////        }
//////    }
//////
//////    private static void saveResultsToFile(String outputPath, FlowNetwork network,
//////                                          Map<Integer, Boolean> isSource,
//////                                          Map<Integer, Boolean> isSink,
//////                                          int maxFlow, long execTime,
//////                                          FordFulkerson solver) throws IOException {
//////        StringBuilder content = new StringBuilder();
//////        content.append("=== Network Analysis Results ===\n");
//////        content.append("Network successfully loaded!!\n");
//////        content.append("Vertices: ").append(network.getVertexCount()).append("\n");
//////        content.append("Edges: ").append(countEdges(network)).append("\n");
//////        content.append("Sources: ").append(isSource.keySet()).append("\n");
//////        content.append("Sinks: ").append(isSink.keySet()).append("\n");
//////        content.append("Max flow: ").append(maxFlow).append("\n");
//////        content.append("Execution time: ").append(execTime).append(" ms\n\n");
//////        content.append("Incremental improvements:\n");
//////        solver.getSteps().forEach(step -> content.append(step).append("\n"));
//////        content.append("\nFlow distribution:\n");
//////        solver.getFlowDistribution().forEach((edge, flow) ->
//////                content.append(edge.getFrom())
//////                        .append("->")
//////                        .append(edge.getTo())
//////                        .append(": ")
//////                        .append(flow)
//////                        .append("/")
//////                        .append(edge.getCapacity())
//////                        .append("\n"));
//////
//////        Files.write(Paths.get(outputPath), content.toString().getBytes());
//////    }
//////
//////    private static int countEdges(FlowNetwork network) {
//////        int count = 0;
//////        for (int i = 0; i < network.getVertexCount(); i++) {
//////            count += network.getAdjacentEdges(i).size();
//////        }
//////        return count;
//////    }
//////}
////
////import java.io.IOException;
////import java.util.*;
////import java.io.File;
////import java.nio.file.Files;
////import java.nio.file.Paths;
////
////public class Main {
////    private static final String INPUT_DIR = "benchmarks/";
////    private static final String OUTPUT_DIR = "output/";
////
////    public static void main(String[] args) {
////        System.out.println("Network Flow Analysis\n");
////
////        // Create directories if needed
////        new File(INPUT_DIR).mkdirs();
////        new File(OUTPUT_DIR).mkdirs();
////
////        // Process all files
////        processFiles("bridge");
////        processFiles("ladder");
////    }
////
////    private static void processFiles(String prefix) {
////        for (int i = 1; i <= 15; i++) {
////            String filename = prefix + "_" + i + ".txt";
////            String inputPath = INPUT_DIR + filename;
////            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
////
////            try {
////                if (!new File(inputPath).exists()) continue;
////
////                // Parse and analyze network
////                long startTime = System.currentTimeMillis();
////                FlowNetwork network = NetworkParser.parse(inputPath);
////                System.out.println(filename + " Network successfully loaded!!");
////
////                // Identify sources/sinks
////                Set<Integer> sources = new HashSet<>();
////                Set<Integer> sinks = new HashSet<>();
////                for (int j = 0; j < network.getVertexCount(); j++) {
////                    sources.add(j);
////                    sinks.add(j);
////                }
////
////                for (int j = 0; j < network.getVertexCount(); j++) {
////                    for (FlowEdge edge : network.getAdjacentEdges(j)) {
////                        sources.remove(edge.getTo());
////                        sinks.remove(edge.getFrom());
////                    }
////                }
////
////                // Compute max flow
////                FordFulkerson solver = new FordFulkerson(network);
////                int maxFlow = solver.computeMaxFlow();
////                long execTime = System.currentTimeMillis() - startTime;
////
////                // Display results
////                System.out.println("Vertices: " + network.getVertexCount());
////                System.out.println("Edges: " + countEdges(network));
////                System.out.println("Sources: " + sources);
////                System.out.println("Sinks: " + sinks);
////                System.out.println("Max flow: " + maxFlow);
////                System.out.println("Execution time: " + execTime + " ms");
////                System.out.println("\nIncremental improvements:");
////                solver.getSteps().forEach(System.out::println);
////                System.out.println("----------------------------------------");
////
////                // Save full results to file
////                saveResults(outputPath, network, sources, sinks, maxFlow, execTime, solver);
////
////            } catch (IOException e) {
////                System.err.println("Error processing " + inputPath);
////            }
////        }
////    }
////
////    private static void saveResults(String outputPath, FlowNetwork network,
////                                    Set<Integer> sources, Set<Integer> sinks,
////                                    int maxFlow, long execTime,
////                                    FordFulkerson solver) throws IOException {
////        StringBuilder content = new StringBuilder();
////        content.append("=== Network Analysis Results ===\n")
////                .append("Input file: ").append(outputPath.replace("output/", "input/").replace("_output.txt", ".txt")).append("\n")
////                .append("Vertices: ").append(network.getVertexCount()).append("\n")
////                .append("Edges: ").append(countEdges(network)).append("\n")
////                .append("Sources: ").append(sources).append("\n")
////                .append("Sinks: ").append(sinks).append("\n")
////                .append("Max flow: ").append(maxFlow).append("\n")
////                .append("Execution time: ").append(execTime).append(" ms\n\n")
////                .append("Augmentation Paths:\n");
////
////        solver.getSteps().forEach(step -> content.append(step).append("\n"));
////        content.append("\nFlow Distribution:\n");
////
////        solver.getFlowDistribution().forEach((edge, flow) ->
////                content.append(edge.getFrom()).append("->").append(edge.getTo())
////                        .append(": ").append(flow).append("/")
////                        .append(edge.getCapacity()).append("\n"));
////
////        Files.write(Paths.get(outputPath), content.toString().getBytes());
////    }
////
////    private static int countEdges(FlowNetwork network) {
////        int count = 0;
////        for (int i = 0; i < network.getVertexCount(); i++) {
////            count += network.getAdjacentEdges(i).size();
////        }
////        return count;
////    }
////}
//
//import java.io.IOException;
//import java.util.*;
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public class Main {
//    private static final String INPUT_DIR = "benchmarks/";
//    private static final String OUTPUT_DIR = "output/";
//
//    public static void main(String[] args) {
//        System.out.println("Network Flow Analysis\n");
//
//        // Create directories if needed
//        new File(INPUT_DIR).mkdirs();
//        new File(OUTPUT_DIR).mkdirs();
//
//        // Process all files
//        processFiles("bridge");
//        processFiles("ladder");
//    }
//
//    private static void processFiles(String prefix) {
//        for (int i = 1; i <= 15; i++) {
//            String filename = prefix + "_" + i + ".txt";
//            String inputPath = INPUT_DIR + filename;
//            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
//
//            try {
//                if (!new File(inputPath).exists()) {
//                    System.out.println("File not found: " + inputPath);
//                    continue;
//                }
//
//                // Parse and analyze network
//                long startTime = System.currentTimeMillis();
//                FlowNetwork network = NetworkParser.parse(inputPath);
//                System.out.println(filename + " Network successfully loaded!!");
//
//                // Identify sources/sinks
//                Set<Integer> sources = new HashSet<>();
//                Set<Integer> sinks = new HashSet<>();
//                for (int j = 0; j < network.getVertexCount(); j++) {
//                    sources.add(j);
//                    sinks.add(j);
//                }
//
//                for (int j = 0; j < network.getVertexCount(); j++) {
//                    for (FlowEdge edge : network.getAdjacentEdges(j)) {
//                        sources.remove(edge.getTo());
//                        sinks.remove(edge.getFrom());
//                    }
//                }
//
//                // Compute max flow
//                FordFulkerson solver = new FordFulkerson(network);
//                int maxFlow = solver.computeMaxFlow();
//                long execTime = System.currentTimeMillis() - startTime;
//
//                // Display results
//                System.out.println("Vertices: " + network.getVertexCount());
//                System.out.println("Edges: " + countEdges(network));
//                System.out.println("Sources: " + sources);
//                System.out.println("Sinks: " + sinks);
//                System.out.println("Max flow: " + maxFlow);
//                System.out.println("Execution time: " + execTime + " ms");
//
//                // Display incremental improvements
//                System.out.println("\nIncremental Improvements:");
//                System.out.println("Total Iterations: " + String.format("%02d", solver.getSteps().size()));
//                solver.getSteps().forEach(System.out::println);
//                System.out.println("----------------------------------------");
//
//                // Save full results to file
//                saveResults(outputPath, filename, network, sources, sinks, maxFlow, execTime, solver);
//
//            } catch (IOException e) {
//                System.err.println("Error processing " + inputPath + ": " + e.getMessage());
//            }
//        }
//    }
//
//    private static void saveResults(String outputPath, String inputFilename,
//                                    FlowNetwork network, Set<Integer> sources,
//                                    Set<Integer> sinks, int maxFlow, long execTime,
//                                    FordFulkerson solver) throws IOException {
//        StringBuilder content = new StringBuilder();
//        content.append("=== Network Analysis Results ===\n")
//                .append("Input file: ").append(inputFilename).append("\n")
//                .append("Vertices: ").append(network.getVertexCount()).append("\n")
//                .append("Edges: ").append(countEdges(network)).append("\n")
//                .append("Sources: ").append(sources).append("\n")
//                .append("Sinks: ").append(sinks).append("\n")
//                .append("Max flow: ").append(maxFlow).append("\n")
//                .append("Execution time: ").append(execTime).append(" ms\n\n")
//                .append("Incremental Improvements:\n")
//                .append("Total Iterations: ").append(String.format("%02d", solver.getSteps().size())).append("\n");
//
//        solver.getSteps().forEach(step -> content.append(step).append("\n"));
//        content.append("\nFlow Distribution:\n");
//
//        solver.getFlowDistribution().forEach((edge, flow) ->
//                content.append(edge.getFrom()).append("->").append(edge.getTo())
//                        .append(": ").append(flow).append("/")
//                        .append(edge.getCapacity()).append("\n"));
//
//        Files.write(Paths.get(outputPath), content.toString().getBytes());
//    }
//
//    private static int countEdges(FlowNetwork network) {
//        int count = 0;
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            count += network.getAdjacentEdges(i).size();
//        }
//        return count;
//    }
//}

//
////before changing to get the outputs
//import java.io.*;
//import java.util.*;
//import java.nio.file.*;
//
//public class Main {
//    private static final String INPUT_DIR = "benchmarks/";
//    private static final String OUTPUT_DIR = "output/";
//
//    public static void main(String[] args) {
//        System.out.println("Network Flow Analysis\n");
//
//        // Create directories if needed
//        new File(INPUT_DIR).mkdirs();
//        new File(OUTPUT_DIR).mkdirs();
//
//        // Process all files
//        processFiles("bridge");
//        processFiles("ladder");
//    }
//
//    private static void processFiles(String prefix) {
//        for (int i = 1; i <= 15; i++) {
//            String filename = prefix + "_" + i + ".txt";
//            String inputPath = INPUT_DIR + filename;
//            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
//
//            try {
//                if (!new File(inputPath).exists()) {
//                    System.out.println("File not found: " + inputPath);
//                    continue;
//                }
//
//                // Parse and analyze network
//                long startTime = System.currentTimeMillis();
//                FlowNetwork network = NetworkParser.parse(inputPath);
//                System.out.println(filename + " Network successfully loaded!!");
//
//                // Identify sources/sinks
//                Set<Integer> sources = findSources(network);
//                Set<Integer> sinks = findSinks(network);
//
//                // Compute max flow
//                FordFulkerson solver = new FordFulkerson(network);
//                int maxFlow = solver.computeMaxFlow();
//                long execTime = System.currentTimeMillis() - startTime;
//
//                // Display results
//                printResults(filename, network, sources, sinks, maxFlow, execTime, solver);
//
//                // Save results to file
//                saveResults(outputPath, filename, network, sources, sinks, maxFlow, execTime, solver);
//
//            } catch (IOException e) {
//                System.err.println("Error processing " + inputPath + ": " + e.getMessage());
//            }
//        }
//    }
//
//    private static Set<Integer> findSources(FlowNetwork network) {
//        Set<Integer> sources = new HashSet<>();
//        for (int j = 0; j < network.getVertexCount(); j++) {
//            sources.add(j);
//        }
//        for (int j = 0; j < network.getVertexCount(); j++) {
//            for (FlowEdge edge : network.getAdjacentEdges(j)) {
//                sources.remove(edge.getTo());
//            }
//        }
//        return sources;
//    }
//
//    private static Set<Integer> findSinks(FlowNetwork network) {
//        Set<Integer> sinks = new HashSet<>();
//        for (int j = 0; j < network.getVertexCount(); j++) {
//            sinks.add(j);
//        }
//        for (int j = 0; j < network.getVertexCount(); j++) {
//            for (FlowEdge edge : network.getAdjacentEdges(j)) {
//                sinks.remove(edge.getFrom());
//            }
//        }
//        return sinks;
//    }
//
//    private static void printResults(String filename, FlowNetwork network,
//                                     Set<Integer> sources, Set<Integer> sinks,
//                                     int maxFlow, long execTime,
//                                     FordFulkerson solver) {
//        System.out.println("Vertices: " + network.getVertexCount());
//        System.out.println("Edges: " + countEdges(network));
//        System.out.println("Sources: " + sources);
//        System.out.println("Sinks: " + sinks);
//        System.out.println("Max flow: " + maxFlow);
//        System.out.println("Execution time: " + execTime + " ms");
//
//        System.out.println("\nIncremental Improvements:");
//        System.out.println("Total Iterations: " + String.format("%02d", solver.getSteps().size()));
//        solver.getSteps().forEach(System.out::println);
//        System.out.println("----------------------------------------");
//    }
//
//    private static void saveResults(String outputPath, String inputFilename,
//                                    FlowNetwork network, Set<Integer> sources,
//                                    Set<Integer> sinks, int maxFlow, long execTime,
//                                    FordFulkerson solver) throws IOException {
//        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
//            // Write metadata
//            writer.write("=== Network Analysis Results ===\n");
//            writer.write("Input file: " + inputFilename + "\n");
//            writer.write("Vertices: " + network.getVertexCount() + "\n");
//            writer.write("Edges: " + countEdges(network) + "\n");
//            writer.write("Sources: " + sources + "\n");
//            writer.write("Sinks: " + sinks + "\n");
//            writer.write("Max flow: " + maxFlow + "\n");
//            writer.write("Execution time: " + execTime + " ms\n\n");
//
//            // Write iterations
//            writer.write("Incremental Improvements:\n");
//            writer.write("Total Iterations: " + String.format("%02d", solver.getSteps().size()) + "\n");
//            for (String step : solver.getSteps()) {
//                writer.write(step + "\n");
//            }
//
//            // Write flow distribution
//            writer.write("\nFlow Distribution:\n");
//            for (Map.Entry<FlowEdge, Integer> entry : solver.getFlowDistribution().entrySet()) {
//                FlowEdge edge = entry.getKey();
//                writer.write(String.format("%d->%d: %d/%d%n",
//                        edge.getFrom(), edge.getTo(),
//                        entry.getValue(), edge.getCapacity()));
//            }
//        }
//    }
//
//    private static int countEdges(FlowNetwork network) {
//        int count = 0;
//        for (int i = 0; i < network.getVertexCount(); i++) {
//            count += network.getAdjacentEdges(i).size();
//        }
//        return count;
//    }
//}



import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Main {
    private static final String INPUT_DIR = "benchmarks/";
    private static final String OUTPUT_DIR = "output/";

    public static void main(String[] args) {
        System.out.println("Network Flow Analysis\n");

        // Create directories if needed
        new File(INPUT_DIR).mkdirs();
        new File(OUTPUT_DIR).mkdirs();

        // Process all files
        processFiles("bridge");
        processFiles("ladder");
    }

    private static void processFiles(String prefix) {
        for (int i = 1; i <= 20; i++) {
            String filename = prefix + "_" + i + ".txt";
            String inputPath = INPUT_DIR + filename;
            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";

            try {
                if (!new File(inputPath).exists()) {
                    System.out.println("File not found: " + inputPath);
                    continue;
                }

                // Parse and analyze network
                long parseStart = System.nanoTime();
                FlowNetwork network = NetworkParser.parse(inputPath);
                long parseTime = System.nanoTime() - parseStart;

                System.out.println("\nTesting file: " + filename);
                System.out.println("Parsed network with " + network.getVertexCount() + " nodes");

                // Identify sources/sinks
                Set<Integer> sources = findSources(network);
                Set<Integer> sinks = findSinks(network);

                // Compute max flow
                long algoStart = System.nanoTime();
                FordFulkerson solver = new FordFulkerson(network);
                int maxFlow = solver.computeMaxFlow();
                long algoTime = System.nanoTime() - algoStart;

                // Display concise results
                System.out.println("Maximum flow: " + maxFlow);
                System.out.printf("Parse time: %.4f ms%n", parseTime / 1_000_000.0);
                System.out.printf("Algorithm time: %.4f ms%n", algoTime / 1_000_000.0);
                System.out.println("Iterations: " + solver.getSteps().size());

                // Save detailed results to file
                saveResults(outputPath, filename, network, sources, sinks, maxFlow,
                        (algoTime + parseTime) / 1_000_000, solver);

            } catch (IOException e) {
                System.err.println("Error processing " + inputPath + ": " + e.getMessage());
            }
        }
    }

    private static Set<Integer> findSources(FlowNetwork network) {
        Set<Integer> sources = new HashSet<>();
        for (int j = 0; j < network.getVertexCount(); j++) {
            sources.add(j);
        }
        for (int j = 0; j < network.getVertexCount(); j++) {
            for (FlowEdge edge : network.getAdjacentEdges(j)) {
                sources.remove(edge.getTo());
            }
        }
        return sources;
    }

    private static Set<Integer> findSinks(FlowNetwork network) {
        Set<Integer> sinks = new HashSet<>();
        for (int j = 0; j < network.getVertexCount(); j++) {
            sinks.add(j);
        }
        for (int j = 0; j < network.getVertexCount(); j++) {
            for (FlowEdge edge : network.getAdjacentEdges(j)) {
                sinks.remove(edge.getFrom());
            }
        }
        return sinks;
    }

    private static void saveResults(String outputPath, String inputFilename,
                                    FlowNetwork network, Set<Integer> sources,
                                    Set<Integer> sinks, int maxFlow, double execTime,
                                    FordFulkerson solver) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            // Write metadata
            writer.write("=== Network Analysis Results ===\n");
            writer.write("Input file: " + inputFilename + "\n");
            writer.write("Vertices: " + network.getVertexCount() + "\n");
            writer.write("Edges: " + countEdges(network) + "\n");
            writer.write("Sources: " + sources + "\n");
            writer.write("Sinks: " + sinks + "\n");
            writer.write("Max flow: " + maxFlow + "\n");
            writer.write("Execution time: " + execTime + " ms\n\n");

            // Write iterations
            writer.write("Incremental Improvements:\n");
            writer.write("Total Iterations: " + String.format("%02d", solver.getSteps().size()) + "\n");
            for (String step : solver.getSteps()) {
                writer.write(step + "\n");
            }

            // Write flow distribution
            writer.write("\nFlow Distribution:\n");
            for (Map.Entry<FlowEdge, Integer> entry : solver.getFlowDistribution().entrySet()) {
                FlowEdge edge = entry.getKey();
                writer.write(String.format("%d->%d: %d/%d%n",
                        edge.getFrom(), edge.getTo(),
                        entry.getValue(), edge.getCapacity()));
            }
        }
    }

    private static int countEdges(FlowNetwork network) {
        int count = 0;
        for (int i = 0; i < network.getVertexCount(); i++) {
            count += network.getAdjacentEdges(i).size();
        }
        return count;
    }
}