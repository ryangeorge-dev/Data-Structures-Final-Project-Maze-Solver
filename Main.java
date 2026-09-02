public class Main {
    public static void main(String[] args) {

        // No arguments -> show how to use program
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            // "solve" mode -> solve a user-provided maze file
            if (args[0].equalsIgnoreCase("solve")) {

                // Need at least: solve <mazeFile>
                if (args.length < 2) {
                    System.out.println("Missing maze file.");
                    printUsage();
                    return;
                }

                String fileName = args[1];

                // Optional algorithm argument -> default to BFS if not provided
                String algorithm = (args.length >= 3) ? args[2].toLowerCase() : "bfs";

                // Step 1: Load and validate maze from file
                Maze maze = new Maze(fileName);

                // Step 2: Convert maze -> graph (adjacency list)
                Graph graph = new Graph();
                graph.buildFromMaze(maze);

                // Step 3: Run selected algorithm (BFS = shortest path, DFS = any path)
                MazeSolver solver = new MazeSolver();
                MazeSolver.SolveResult result;

                if (algorithm.equals("dfs")) {
                    result = solver.dfs(graph, maze.getStart(), maze.getEnd());
                } else {
                    result = solver.bfs(graph, maze.getStart(), maze.getEnd());
                }

                // Print original maze
                System.out.println("Original Maze:");
                maze.printMaze();
                System.out.println();

                // Print stats about the graph + algorithm run
                System.out.println("Graph vertex count: " + graph.getVertexCount()); // # of walkable cells
                System.out.println("Algorithm used: " + algorithm.toUpperCase());
                System.out.println("Visited nodes: " + result.getVisitedNodes()); // search effort
                System.out.printf("Runtime: %.3f ms%n", result.getRuntimeNanos() / 1_000_000.0);

                // If path exists -> print path + visualize it
                if (result.isPathFound()) {
                    System.out.println("Path found.");
                    System.out.println("Path length: " + result.getPath().size()); // number of steps
                    System.out.println("Path: " + result.getPath()); // list of coordinates
                    System.out.println();

                    System.out.println("Maze with path:");
                    maze.printMazeWithPath(result.getPath()); // overlay '*' on path
                } else {
                    System.out.println("No path found.");
                }

            // "perf" mode -> run performance tests on generated mazes
            } else if (args[0].equalsIgnoreCase("perf")) {
                PerformanceTest performanceTest = new PerformanceTest();
                performanceTest.runGeneratedTests();

            // Invalid command -> show usage
            } else {
                printUsage();
            }

        // Catch any runtime errors (file issues, invalid maze, etc.)
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Prints how to run the program correctly
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java Main solve <mazeFile> [bfs|dfs]");
        System.out.println("  java Main perf");
    }
}