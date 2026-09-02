import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class PerformanceTest {
    // Solver used to run BFS on generated mazes
    private final MazeSolver solver;

    // Random generator for maze variability (walls + path direction)
    private final Random random;

    public PerformanceTest() {
        solver = new MazeSolver(); // reuse same solver instance
        random = new Random();     // used for randomness in generation
    }

    public void runGeneratedTests() {
        // Different maze sizes to test scalability
        int[] sizes = {10, 20, 50, 100, 150};

        // CSV-style header (easy to paste into Excel/Sheets)
        System.out.println("Maze Size,Visited Nodes,Path Length,Runtime (ms)");

        for (int size : sizes) {
            // Unique file per maze size
            String fileName = "generated_" + size + "x" + size + ".txt";

            try {
                // Generate maze file with given size and wall density
                generateMazeFile(fileName, size, size, 0.30);

                // Standard pipeline: file -> Maze -> Graph
                Maze maze = new Maze(fileName);
                Graph graph = new Graph();
                graph.buildFromMaze(maze);

                // Run BFS (we test BFS performance specifically)
                MazeSolver.SolveResult result = solver.bfs(graph, maze.getStart(), maze.getEnd());

                // Convert nanoseconds -> milliseconds for readability
                double runtimeMs = result.getRuntimeNanos() / 1_000_000.0;

                // Print results in structured format
                System.out.printf(
                    "%dx%d,%d,%d,%.3f%n",
                    size,
                    size,
                    result.getVisitedNodes(),  // how much work BFS did
                    result.getPath().size(),   // path length
                    runtimeMs                 // execution time
                );

            } catch (IOException e) {
                // Handle file errors without stopping all tests
                System.out.println("Error during performance test for size " + size + ": " + e.getMessage());
            }
        }
    }

    private void generateMazeFile(String fileName, int rows, int cols, double wallProbability) throws IOException {
        // 2D grid representation of maze
        char[][] grid = new char[rows][cols];

        // Step 1: Start with fully open maze (all walkable)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = '.';
            }
        }

        // Step 2: Place start (top-left) and end (bottom-right)
        grid[0][0] = 'S';
        grid[rows - 1][cols - 1] = 'E';

        // Step 3: Create a guaranteed valid path from S -> E
        // This ensures the maze is always solvable
        boolean[][] guaranteedPath = new boolean[rows][cols];
        carveGuaranteedPath(guaranteedPath, rows, cols);

        // Step 4: Add random walls, but NEVER block the guaranteed path
        // This keeps maze challenging but always solvable
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Skip start and end positions
                if ((r == 0 && c == 0) || (r == rows - 1 && c == cols - 1)) {
                    continue;
                }

                // Only place wall if NOT part of guaranteed path
                if (!guaranteedPath[r][c] && random.nextDouble() < wallProbability) {
                    grid[r][c] = '#';
                }
            }
        }

        // Step 5: Convert grid -> text file (so Maze class can reuse it)
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sb.append(grid[r][c]);
            }
            sb.append(System.lineSeparator());
        }

        Files.writeString(Path.of(fileName), sb.toString());
    }

    private void carveGuaranteedPath(boolean[][] guaranteedPath, int rows, int cols) {
        // Start at S (top-left)
        int r = 0;
        int c = 0;
        guaranteedPath[r][c] = true;

        // Move until reaching E (bottom-right)
        while (r < rows - 1 || c < cols - 1) {

            // If at bottom row -> can only move right
            if (r == rows - 1) {
                c++;

            // If at rightmost column -> can only move down
            } else if (c == cols - 1) {
                r++;

            } else {
                // Randomly move right or down
                // (creates variation in path shape)
                if (random.nextBoolean()) {
                    c++;
                } else {
                    r++;
                }
            }

            // Mark this cell as part of guaranteed path
            guaranteedPath[r][c] = true;
        }
    }
}