////import java.io.*;
////import java.util.*;
////import java.nio.file.*;
////
////public class Main {
////    private static final String INPUT_DIR = "benchmarks/";
////    private static final String OUTPUT_DIR = "output/";
////    private static final int MAX_OUTPUT_LINES = 100;
////
////    public static void main(String[] args) {
////        System.out.println("=== Network Flow Analysis ===");
////        System.out.println("Starting execution at: " + new Date());
////        long programStart = System.nanoTime();
////
////        // Create directories if needed
////        new File(INPUT_DIR).mkdirs();
////        new File(OUTPUT_DIR).mkdirs();
////
////        // Process all files
////        processFiles("bridge");
////        processFiles("ladder");
////
////        long totalTime = System.nanoTime() - programStart;
////        System.out.printf("\n=== Execution completed in %.2f seconds ===%n",
////                totalTime / 1_000_000_000.0);
////        System.out.println("End time: " + new Date());
////    }
////
////    private static void processFiles(String prefix) {
////        System.out.println("\nProcessing " + prefix + " files...");
////
////        for (int i = 1; i <= 20; i++) {
////            String filename = prefix + "_" + i + ".txt";
////            String inputPath = INPUT_DIR + filename;
////            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
////
////            System.out.print("\n" + filename + ": ");
////            long fileStart = System.nanoTime();
////
////            try {
////                if (!new File(inputPath).exists()) {
////                    System.out.println("SKIPPED (File not found)");
////                    continue;
////                }
////
////                // Parse network
////                long parseStart = System.nanoTime();
////                FlowNetwork network = NetworkParser.parse(inputPath);
////                long parseTime = System.nanoTime() - parseStart;
////
////                // Compute max flow
////                long algoStart = System.nanoTime();
////                FordFulkerson solver = new FordFulkerson(network);
////                int maxFlow = solver.computeMaxFlow();
////                long algoTime = System.nanoTime() - algoStart;
////                long fileTime = System.nanoTime() - fileStart;
////
////                System.out.println("COMPLETED");
////                System.out.printf("  Nodes: %d | Flow: %d | Time: %.2f ms (Parse: %.2f ms, Algo: %.2f ms)%n",
////                        network.getVertexCount(),
////                        maxFlow,
////                        fileTime / 1_000_000.0,
////                        parseTime / 1_000_000.0,
////                        algoTime / 1_000_000.0);
////
////                // Save results
////                saveResults(outputPath, filename, network, maxFlow,
////                        fileTime / 1_000_000, solver);
////
////            } catch (Exception e) {
////                long failTime = System.nanoTime() - fileStart;
////                System.out.println("FAILED (" + String.format("%.2f ms", failTime / 1_000_000.0) + ")");
////                System.out.println("  Error: " + e.getMessage());
////            }
////        }
////    }
////
//////    private static void saveResults(String outputPath, String filename,
//////                                    FlowNetwork network, int maxFlow,
//////                                    double execTime,
//////                                    FordFulkerson solver) throws IOException {
//////        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
//////            // Header
//////            writer.write("=== Network Flow Analysis ===\n");
//////            writer.write("File: " + filename + "\n");
//////            writer.write("Timestamp: " + new Date() + "\n\n");
//////
//////            // Summary
//////            writer.write("=== Summary ===\n");
//////            writer.write("Nodes: " + network.getVertexCount() + "\n");
//////            writer.write("Edges: " + countEdges(network) + "\n");
//////            writer.write("Max flow: " + maxFlow + "\n");
//////            writer.write(String.format("Execution time: %.2f ms%n%n", execTime));
//////
//////            // Steps (limited)
//////            List<String> steps = solver.getSteps();
//////            int stepLimit = Math.min(steps.size(), MAX_OUTPUT_LINES);
//////            writer.write("=== Augmentation Paths (" + stepLimit + " of " + steps.size() + ") ===\n");
//////            for (int i = 0; i < stepLimit; i++) {
//////                writer.write(steps.get(i) + "\n");
//////            }
//////            if (steps.size() > MAX_OUTPUT_LINES) {
//////                writer.write("\n[OUTPUT LIMIT] Showing first " + MAX_OUTPUT_LINES + " of " +
//////                        steps.size() + " steps\n");
//////            }
//////
//////            // Flow distribution (limited)
//////            writer.write("\n=== Flow Distribution ===\n");
//////            Map<FlowEdge, Integer> flowMap = solver.getFlowDistribution();
//////            int edgeCount = 0;
//////            for (Map.Entry<FlowEdge, Integer> entry : flowMap.entrySet()) {
//////                if (edgeCount++ >= MAX_OUTPUT_LINES) {
//////                    writer.write("\n[OUTPUT LIMIT] Showing first " + MAX_OUTPUT_LINES + " of " +
//////                            flowMap.size() + " edges\n");
//////                    break;
//////                }
//////                FlowEdge edge = entry.getKey();
//////                writer.write(String.format("%d->%d: %d/%d%n",
//////                        edge.getFrom(), edge.getTo(),
//////                        entry.getValue(), edge.getCapacity()));
//////            }
//////        }
//////    }
////
////    private static void saveResults(String outputPath, String filename,
////                                    FlowNetwork network, int maxFlow,
////                                    double execTime,
////                                    FordFulkerson solver) throws IOException {
////        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
////            // Header
////            writer.write("=== Network Flow Analysis ===\n");
////            writer.write("File: " + filename + "\n");
////            writer.write("Timestamp: " + new Date() + "\n\n");
////
////            // Summary
////            writer.write("=== Summary ===\n");
////            writer.write("Nodes: " + network.getVertexCount() + "\n");
////            writer.write("Edges: " + countEdges(network) + "\n");
////            writer.write("Max flow: " + maxFlow + "\n");
////            writer.write(String.format("Execution time: %.2f ms%n%n", execTime));
////
////            // Steps
////            List<String> steps = solver.getSteps();
////            writer.write("=== First 100 Augmentation Paths ===\n");
////            writer.write("Total steps executed: " + steps.size() + "\n\n");
////
////            for (String step : steps) {
////                writer.write(step + "\n");
////            }
////
////            if (steps.size() >= 100) {
////                writer.write("\n[Note] Showing first 100 of " + steps.size() + " total steps\n");
////            }
////
////            // Flow distribution - show only edges with flow > 0
////            writer.write("\n=== Flow Distribution (Non-zero flows only) ===\n");
////            Map<FlowEdge, Integer> flowMap = solver.getFlowDistribution();
////            flowMap.entrySet().stream()
////                    .filter(entry -> entry.getValue() > 0)
////                    .limit(100) // Limit to 100 edges
////                    .forEach(entry -> {
////                        FlowEdge edge = entry.getKey();
////                        try {
////                            writer.write(String.format("%d->%d: %d/%d%n",
////                                    edge.getFrom(), edge.getTo(),
////                                    entry.getValue(), edge.getCapacity()));
////                        } catch (IOException e) {
////                            throw new UncheckedIOException(e);
////                        }
////                    });
////
////            if (flowMap.size() > 100) {
////                writer.write("\n[Note] Showing 100 of " + flowMap.size() + " total edges\n");
////            }
////        }
////    }
////    private static int countEdges(FlowNetwork network) {
////        int count = 0;
////        for (int i = 0; i < network.getVertexCount(); i++) {
////            count += network.getAdjacentEdges(i).size();
////        }
////        return count;
////    }
////}
//
////code before the input thing
//import java.io.*;
//import java.util.*;
//import java.nio.file.*;
//
//public class Main {
//    private static final String INPUT_DIR = "benchmarks/";
//    private static final String OUTPUT_DIR = "output/";
//    private static final int MAX_OUTPUT_LINES = 100;
//
//    public static void main(String[] args) {
//        System.out.println("=== Network Flow Analysis ===");
//        System.out.println("Starting execution at: " + new Date());
//        long programStart = System.nanoTime();
//
//        // Create directory if needed
//        new File(OUTPUT_DIR).mkdirs();
//
//        // Process all files
//        processFiles("bridge");
//        processFiles("ladder");
//
//        long totalTime = System.nanoTime() - programStart;
//        System.out.printf("\n=== Execution completed in %.2f seconds ===%n",
//                totalTime / 1_000_000_000.0);
//        System.out.println("End time: " + new Date());
//    }
//
//    private static void processFiles(String prefix) {
//        System.out.println("\nProcessing " + prefix + " files...");
//
//        for (int i = 1; i <= 20; i++) {
//            String filename = prefix + "_" + i + ".txt";
//            String inputPath = INPUT_DIR + filename;
//            String outputPath = OUTPUT_DIR + prefix + "_" + i + "_output.txt";
//
//            System.out.print("\n" + filename + ": ");
//            long fileStart = System.nanoTime();
//
//            try {
//                if (!new File(inputPath).exists()) {
//                    System.out.println("SKIPPED (File not found)");
//                    continue;
//                }
//
//                // Parse network
//                long parseStart = System.nanoTime();
//                FlowNetwork network = NetworkParser.parse(inputPath);
//                long parseTime = System.nanoTime() - parseStart;
//
//                // Compute max flow
//                long algoStart = System.nanoTime();
//                FordFulkerson solver = new FordFulkerson(network);
//                int maxFlow = solver.computeMaxFlow();
//                long algoTime = System.nanoTime() - algoStart;
//                long fileTime = System.nanoTime() - fileStart;
//
//                System.out.println("COMPLETED");
//                System.out.printf("  Nodes: %d | Flow: %d | Time: %.2f ms (Parse: %.2f ms, Algo: %.2f ms)%n",
//                        network.getVertexCount(),
//                        maxFlow,
//                        fileTime / 1_000_000.0,
//                        parseTime / 1_000_000.0,
//                        algoTime / 1_000_000.0);
//
//                // Save results with limited output
//                saveResults(outputPath, filename, network, maxFlow,
//                        fileTime / 1_000_000, solver);
//
//            } catch (Exception e) {
//                long failTime = System.nanoTime() - fileStart;
//                System.out.println("FAILED (" + String.format("%.2f ms", failTime / 1_000_000.0) + ")");
//                System.out.println("  Error: " + e.getMessage());
//            }
//        }
//    }
//
////    private static void saveResults(String outputPath, String filename,
////                                    FlowNetwork network, int maxFlow,
////                                    double execTime,
////                                    FordFulkerson solver) throws IOException {
////        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
////            // Header
////            writer.write("=== Network Flow Analysis ===\n");
////            writer.write("File: " + filename + "\n");
////            writer.write("Timestamp: " + new Date() + "\n\n");
////
////            // Summary
////            writer.write("=== Summary ===\n");
////            writer.write("Nodes: " + network.getVertexCount() + "\n");
////            writer.write("Edges: " + countEdges(network) + "\n");
////            writer.write("Max flow: " + maxFlow + "\n");
////            writer.write(String.format("Execution time: %.2f ms%n%n", execTime));
////
////            // Steps (limited to 100)
////            List<String> steps = solver.getSteps();
////            writer.write("=== Augmentation Paths ===\n");
////            writer.write("Total steps: " + steps.size() + "\n");
////            writer.write("Showing first " + Math.min(steps.size(), MAX_OUTPUT_LINES) + " steps:\n\n");
////
////            for (int i = 0; i < Math.min(steps.size(), MAX_OUTPUT_LINES); i++) {
////                writer.write(steps.get(i) + "\n");
////            }
////
////            if (steps.size() > MAX_OUTPUT_LINES) {
////                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " steps]\n");
////            }
////
////            // Flow distribution (limited to 100 non-zero edges)
////            writer.write("\n=== Flow Distribution ===\n");
////            Map<FlowEdge, Integer> flowMap = solver.getFlowDistribution();
////            int edgeCount = 0;
////
////            writer.write("Showing first 100 edges with non-zero flow:\n");
////            for (Map.Entry<FlowEdge, Integer> entry : flowMap.entrySet()) {
////                if (entry.getValue() > 0) {
////                    FlowEdge edge = entry.getKey();
////                    writer.write(String.format("%d->%d: %d/%d%n",
////                            edge.getFrom(), edge.getTo(),
////                            entry.getValue(), edge.getCapacity()));
////
////                    if (++edgeCount >= MAX_OUTPUT_LINES) break;
////                }
////            }
////
////            if (edgeCount >= MAX_OUTPUT_LINES) {
////                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " edges with flow]\n");
////            }
////        }
////    }
//
//    private static void saveResults(String outputPath, String filename,
//                                    FlowNetwork network, int maxFlow,
//                                    double execTime,
//                                    FordFulkerson solver) throws IOException {
//        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
//            // Header
//            writer.write("=== Network Flow Analysis ===\n");
//            writer.write("File: " + filename + "\n");
//            writer.write("Timestamp: " + new Date() + "\n\n");
//
//            // Summary
//            writer.write("=== Summary ===\n");
//            writer.write("Nodes: " + network.getVertexCount() + "\n");
//            writer.write("Edges: " + countEdges(network) + "\n");
//            writer.write("Max flow: " + maxFlow + "\n");
//            writer.write(String.format("Execution time: %.2f ms%n%n", execTime));
//
//            // Steps (limited to 100)
//            List<String> steps = solver.getSteps();
//            writer.write("=== Augmentation Paths ===\n");
//            writer.write("Total steps: " + steps.size() + "\n");
//            writer.write("Showing first " + Math.min(steps.size(), MAX_OUTPUT_LINES) + " steps:\n\n");
//
//            for (int i = 0; i < Math.min(steps.size(), MAX_OUTPUT_LINES); i++) {
//                writer.write(steps.get(i) + "\n");
//            }
//
//            if (steps.size() > MAX_OUTPUT_LINES) {
//                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " steps]\n");
//            }
//
//            // Flow distribution sorted by source then target node
//            writer.write("\n=== Flow Distribution ===\n");
//            Map<FlowEdge, Integer> flowMap = solver.getFlowDistribution();
//
//            // Create a list and sort it
//            List<FlowEdge> sortedEdges = new ArrayList<>(flowMap.keySet());
//            sortedEdges.sort(Comparator
//                    .comparingInt(FlowEdge::getFrom)
//                    .thenComparingInt(FlowEdge::getTo));
//
//            writer.write("Edges sorted by source then target node:\n\n");
//            int edgeCount = 0;
//            for (FlowEdge edge : sortedEdges) {
//                int flow = flowMap.get(edge);
//                if (flow > 0) {  // Only show edges with flow
//                    writer.write(String.format("%d->%d: %d/%d%n",
//                            edge.getFrom(), edge.getTo(),
//                            flow, edge.getCapacity()));
//
//                    if (++edgeCount >= MAX_OUTPUT_LINES) break;
//                }
//            }
//
//            if (edgeCount >= MAX_OUTPUT_LINES) {
//                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " edges with flow]\n");
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
import java.util.Scanner;

public class Main {
    private static final String INPUT_ROOT = "input/";
    private static final String OUTPUT_DIR = "output/";
    private static final int MAX_OUTPUT_LINES = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Network Flow Analysis ===");
        System.out.println("Enter file or folder path (relative to input/):");
        System.out.println("Examples:");
        System.out.println("- For single file: example.txt");
        System.out.println("- For folder: benchmarks");
        System.out.print("Your input: ");

        String userInput = scanner.nextLine().trim();
        String inputPath = INPUT_ROOT + userInput;

        System.out.println("\nStarting execution at: " + new Date());
        long programStart = System.nanoTime();

        // Create output directory if needed
        new File(OUTPUT_DIR).mkdirs();

        try {
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.out.println("Error: The specified path does not exist.");
                return;
            }

            if (inputFile.isFile()) {
                // Process single file
                processFile(inputPath, getOutputPath(inputPath));
            } else {
                // Process directory
                System.out.println("\nProcessing files in: " + inputPath);
                processDirectory(inputPath);
            }

            long totalTime = System.nanoTime() - programStart;
            System.out.printf("\n=== Execution completed in %.2f seconds ===%n",
                    totalTime / 1_000_000_000.0);
            System.out.println("End time: " + new Date());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void processDirectory(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in directory.");
            return;
        }

        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            try {
                processFile(file.getPath(), getOutputPath(file.getPath()));
            } catch (Exception e) {
                System.out.println("Failed to process " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private static void processFile(String inputPath, String outputPath) throws IOException {
        System.out.print("\nProcessing: " + inputPath.replace(INPUT_ROOT, ""));
        long fileStart = System.nanoTime();

        try {
            // Parse network
            long parseStart = System.nanoTime();
            FlowNetwork network = NetworkParser.parse(inputPath);
            long parseTime = System.nanoTime() - parseStart;

            // Compute max flow
            long algoStart = System.nanoTime();
            FordFulkerson solver = new FordFulkerson(network);
            int maxFlow = solver.computeMaxFlow();
            long algoTime = System.nanoTime() - algoStart;
            long fileTime = System.nanoTime() - fileStart;

            System.out.println(" - COMPLETED");
            System.out.printf("  Nodes: %d | Flow: %d | Time: %.2f ms (Parse: %.2f ms, Algo: %.2f ms)%n",
                    network.getVertexCount(),
                    maxFlow,
                    fileTime / 1_000_000.0,
                    parseTime / 1_000_000.0,
                    algoTime / 1_000_000.0);

            // Save results
            saveResults(outputPath, inputPath.replace(INPUT_ROOT, ""),
                    network, maxFlow, fileTime / 1_000_000, solver);

        } catch (Exception e) {
            long failTime = System.nanoTime() - fileStart;
            System.out.println(" - FAILED (" + String.format("%.2f ms", failTime / 1_000_000.0) + ")");
            System.out.println("  Error: " + e.getMessage());
            throw e;
        }
    }

    private static String getOutputPath(String inputPath) {
        // Extract the filename with the path
        //String relativePath = inputPath.replace(INPUT_ROOT, "");
        //return OUTPUT_DIR + relativePath.replace(".txt", "_output.txt");
        String filename = new File(inputPath).getName();
        return OUTPUT_DIR + filename.replace(".txt", "_output.txt");
    }

    private static void saveResults(String outputPath, String filename,
                                    FlowNetwork network, int maxFlow,
                                    double execTime,
                                    FordFulkerson solver) throws IOException {
        // Create parent directories if they don't exist
        new File(outputPath).getParentFile().mkdirs();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
            // Header
            writer.write("=== Network Flow Analysis ===\n");
            writer.write("File: " + filename + "\n");
            writer.write("Timestamp: " + new Date() + "\n\n");

            // Summary
            writer.write("=== Summary ===\n");
            writer.write("Nodes: " + network.getVertexCount() + "\n");
            writer.write("Edges: " + countEdges(network) + "\n");
            writer.write("Max flow: " + maxFlow + "\n");
            writer.write(String.format("Execution time: %.2f ms%n%n", execTime));

            // Steps (limited to 100)
            List<String> steps = solver.getSteps();
            writer.write("=== Augmentation Paths ===\n");
            writer.write("Total steps: " + steps.size() + "\n");
            writer.write("Showing first " + Math.min(steps.size(), MAX_OUTPUT_LINES) + " steps:\n\n");

            for (int i = 0; i < Math.min(steps.size(), MAX_OUTPUT_LINES); i++) {
                writer.write(steps.get(i) + "\n");
            }

            if (steps.size() > MAX_OUTPUT_LINES) {
                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " steps]\n");
            }

            // Flow distribution sorted by source then target node
            writer.write("\n=== Flow Distribution ===\n");
            Map<FlowEdge, Integer> flowMap = solver.getFlowDistribution();

            // Create a list and sort it
            List<FlowEdge> sortedEdges = new ArrayList<>(flowMap.keySet());
            sortedEdges.sort(Comparator
                    .comparingInt(FlowEdge::getFrom)
                    .thenComparingInt(FlowEdge::getTo));

            writer.write("Edges sorted by source then target node:\n\n");
            int edgeCount = 0;
            for (FlowEdge edge : sortedEdges) {
                int flow = flowMap.get(edge);
                if (flow > 0) {
                    writer.write(String.format("%d->%d: %d/%d%n",
                            edge.getFrom(), edge.getTo(),
                            flow, edge.getCapacity()));

                    if (++edgeCount >= MAX_OUTPUT_LINES) break;
                }
            }

            if (edgeCount >= MAX_OUTPUT_LINES) {
                writer.write("\n[Output limited to first " + MAX_OUTPUT_LINES + " edges with flow]\n");
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
