import java.util.*; // collections for queue, stack, map, set, list

public class MazeSolver {

    // stores the result of a search run (not just true/false)
    public static class SolveResult {
        private final boolean pathFound;     // did we reach the end?
        private final List<Cell> path;       // actual path from start -> end
        private final int visitedNodes;      // how many nodes explored (performance)
        private final long runtimeNanos;     // time taken for algorithm

        public SolveResult(boolean pathFound, List<Cell> path, int visitedNodes, long runtimeNanos) {
            this.pathFound = pathFound;
            this.path = path;
            this.visitedNodes = visitedNodes;
            this.runtimeNanos = runtimeNanos;
        }

        public boolean isPathFound() { return pathFound; }
        public List<Cell> getPath() { return path; }
        public int getVisitedNodes() { return visitedNodes; }
        public long getRuntimeNanos() { return runtimeNanos; }
    }

    // BFS = shortest path in unweighted graph (layer-by-layer exploration)
    public SolveResult bfs(Graph graph, Cell start, Cell end) {
        long startTime = System.nanoTime(); // start timing

        Queue<Cell> queue = new LinkedList<>(); // FIFO -> breadth-first
        Set<Cell> visited = new HashSet<>();    // avoid revisiting nodes
        Map<Cell, Cell> parent = new HashMap<>(); // track path (child -> parent)

        queue.offer(start);   // start node goes into queue
        visited.add(start);   // mark visited immediately (avoid duplicates)

        while (!queue.isEmpty()) {
            Cell current = queue.poll(); // take next node in BFS order

            if (current.equals(end)) { // goal reached -> stop early
                long endTime = System.nanoTime();
                List<Cell> path = reconstructPath(parent, start, end);
                return new SolveResult(true, path, visited.size(), endTime - startTime);
            }

            // explore neighbors (graph abstraction handles adjacency)
            for (Cell neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);        // mark as visited when discovered
                    parent.put(neighbor, current); // store how we got here
                    queue.offer(neighbor);        // add to queue for future processing
                }
            }
        }

        // no path found
        long endTime = System.nanoTime();
        return new SolveResult(false, new ArrayList<>(), visited.size(), endTime - startTime);
    }

    // DFS = explore deep first, not guaranteed shortest path
    public SolveResult dfs(Graph graph, Cell start, Cell end) {
        long startTime = System.nanoTime();

        Stack<Cell> stack = new Stack<>();   // LIFO -> depth-first
        Set<Cell> visited = new HashSet<>(); // same idea as BFS
        Map<Cell, Cell> parent = new HashMap<>(); // still track path

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Cell current = stack.pop(); // take most recent node (go deep)

            if (current.equals(end)) {
                long endTime = System.nanoTime();
                List<Cell> path = reconstructPath(parent, start, end);
                return new SolveResult(true, path, visited.size(), endTime - startTime);
            }

            // explore neighbors (order affects DFS path!)
            for (Cell neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    stack.push(neighbor); // push -> explore deeper next
                }
            }
        }

        long endTime = System.nanoTime();
        return new SolveResult(false, new ArrayList<>(), visited.size(), endTime - startTime);
    }

    // rebuild path by walking backward using parent map
    private List<Cell> reconstructPath(Map<Cell, Cell> parent, Cell start, Cell end) {
        List<Cell> path = new ArrayList<>();
        Cell current = end;

        // follow parent links: end -> ... -> start
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path); // reverse to get start -> end

        // sanity check: ensure path actually begins at start
        if (!path.isEmpty() && path.get(0).equals(start)) {
            return path;
        }

        return new ArrayList<>(); // fallback if something went wrong
    }
}