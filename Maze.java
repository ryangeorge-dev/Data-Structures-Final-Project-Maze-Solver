import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Maze {
    // 2D grid representation of the maze
    private final char[][] grid;

    // Dimensions of the maze
    private final int rows;
    private final int cols;

    // Start and end positions
    private Cell start;
    private Cell end;

    public Maze(String fileName) throws IOException {
        // Read all lines from file
        List<String> lines = Files.readAllLines(Path.of(fileName));

        // Validate: maze must not be empty
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Maze file is empty.");
        }

        // Determine size (rows = number of lines, cols = length of first line)
        rows = lines.size();
        cols = lines.get(0).length();

        // Initialize grid
        grid = new char[rows][cols];

        int startCount = 0;
        int endCount = 0;

        // Iterate through each row
        for (int r = 0; r < rows; r++) {
            String line = lines.get(r);

            // Validate: all rows must have same length (rectangular grid)
            if (line.length() != cols) {
                throw new IllegalArgumentException(
                    "Maze must be rectangular. Line " + (r + 1) + " has different length."
                );
            }

            // Iterate through each column
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);

                // Validate: only allow S, E, ., #
                if (ch != 'S' && ch != 'E' && ch != '.' && ch != '#') {
                    throw new IllegalArgumentException(
                        "Invalid character '" + ch + "' at row " + r + ", col " + c
                    );
                }

                // Store character in grid
                grid[r][c] = ch;

                // Track start and end positions
                if (ch == 'S') {
                    start = new Cell(r, c);
                    startCount++;
                } else if (ch == 'E') {
                    end = new Cell(r, c);
                    endCount++;
                }
            }
        }

        // Validate: exactly one start and one end
        if (startCount != 1 || endCount != 1) {
            throw new IllegalArgumentException("Maze must contain exactly one S and one E.");
        }
    }

    // Check if coordinates are within maze bounds
    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Check if cell is a wall (#)
    public boolean isWall(int row, int col) {
        return grid[row][col] == '#';
    }

    // Check if cell can be visited (in bounds AND not a wall)
    public boolean isWalkable(int row, int col) {
        return isInBounds(row, col) && !isWall(row, col);
    }

    // Get raw character at a position
    public char getCellValue(int row, int col) {
        return grid[row][col];
    }

    // Get start cell (S)
    public Cell getStart() {
        return start;
    }

    // Get end cell (E)
    public Cell getEnd() {
        return end;
    }

    // Get number of rows
    public int getRows() {
        return rows;
    }

    // Get number of columns
    public int getCols() {
        return cols;
    }

    // Print original maze (no modifications)
    public void printMaze() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(grid[r][c]);
            }
            System.out.println();
        }
    }

    // Print maze with solution path overlaid
    public void printMazeWithPath(List<Cell> path) {
        // Create a copy so original grid is not modified
        char[][] copy = new char[rows][cols];

        for (int r = 0; r < rows; r++) {
            System.arraycopy(grid[r], 0, copy[r], 0, cols);
        }

        // Overlay path using '*'
        if (path != null) {
            for (Cell cell : path) {
                int r = cell.getRow();
                int c = cell.getCol();

                // Only replace open cells, keep S and E intact
                if (copy[r][c] == '.') {
                    copy[r][c] = '*';
                }
            }
        }

        // Print modified copy
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(copy[r][c]);
            }
            System.out.println();
        }
    }
}