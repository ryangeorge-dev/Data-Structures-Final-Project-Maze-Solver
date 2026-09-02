import java.util.Objects;

// Represents a single cell (node) in the maze using its position
public class Cell {
    // Row and column define the unique identity of the cell
    private final int row;
    private final int col;

    // Constructor initializes the cell position (immutable after creation)
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Getter for row index
    public int getRow() {
        return row;
    }

    // Getter for column index
    public int getCol() {
        return col;
    }

    @Override
    // Ensures cells with same coordinates are treated as the same node
    // (important for graph traversal and visited tracking)
    public boolean equals(Object obj) {
        // Same object in memory -> equal
        if (this == obj) return true;

        // If not a Cell, cannot be equal
        if (!(obj instanceof Cell)) return false;

        // Cast and compare positions
        Cell other = (Cell) obj;

        // Two cells are equal if they have same row and col
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        // Generates hash based on row and col
        // Must match equals() for correct behavior in HashMap/HashSet
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        // Formats cell for readable output (used when printing path)
        return "(" + row + ", " + col + ")";
    }
}