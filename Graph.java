import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    // adjacency list: each node (Cell) maps to a list of its neighbors
    private final Map<Cell, List<Cell>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>(); // empty graph initially
    }

    public void buildFromMaze(Maze maze) {
        // direction vectors: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};   

        // iterate through every cell in the maze
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {

                // skip walls / invalid cells -> only walkable cells become nodes
                if (!maze.isWalkable(r, c)) {
                    continue;
                }

                Cell current = new Cell(r, c);

                // ensure current node exists in graph
                adjacencyList.putIfAbsent(current, new ArrayList<>());

                // check all 4 possible neighbors
                for (int i = 0; i < 4; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];

                    // if neighbor is valid and not a wall, connect it
                    if (maze.isWalkable(nr, nc)) {
                        Cell neighbor = new Cell(nr, nc);

                        // ensure neighbor also exists as a node
                        adjacencyList.putIfAbsent(neighbor, new ArrayList<>());

                        // add edge: current -> neighbor
                        adjacencyList.get(current).add(neighbor);
                        // note: reverse edge added later when neighbor is processed -> effectively undirected
                    }
                }
            }
        }
    }

    public List<Cell> getNeighbors(Cell cell) {
        // return adjacent nodes (used in BFS/DFS)
        // empty list if node not found (avoids null errors)
        return adjacencyList.getOrDefault(cell, new ArrayList<>());
    }

    public boolean contains(Cell cell) {
        // check if a cell exists as a vertex in the graph
        return adjacencyList.containsKey(cell);
    }

    public int getVertexCount() {
        // total number of vertices = number of walkable cells
        return adjacencyList.size();
    }
}