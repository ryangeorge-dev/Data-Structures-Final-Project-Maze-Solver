Maze Solver Using Graphs and BFS/DFS

--------------------------------------------------

Overview:
This project implements a maze-solving program using graph data structures and traversal algorithms. The program reads a maze from a text file, converts it into a graph, and applies either Breadth-First Search (BFS) or Depth-First Search (DFS) to find a path from the start (S) to the end (E).

--------------------------------------------------

Requirements:
- Java JDK (version 8 or higher)
- All .java files must be located in the same folder

--------------------------------------------------

How to Compile:

Open a terminal (Command Prompt / PowerShell / Terminal) in the project directory and run:

javac *.java

This will compile all Java files and generate .class files.

--------------------------------------------------

How to Run:

1. Solve a Maze using BFS:

java Main solve maze1.txt bfs

2. Solve a Maze using DFS:

java Main solve maze1.txt dfs

--------------------------------------------------

How to Run Performance Tests:

java Main perf

This will generate mazes of different sizes and output:
- number of visited nodes
- path length
- runtime (in milliseconds)

--------------------------------------------------

Input Format:

The maze must be provided as a text file using the following characters:

S  → Start position
E  → End position
.  → Walkable path
#  → Wall (blocked path)

The maze must:
- be rectangular
- contain exactly one S and one E

--------------------------------------------------

Notes:
- The maze file (e.g., maze1.txt) must be in the same directory as the Java files.
- The program was developed and tested using VS Code but can be run in any Java-supported IDE such as IntelliJ or Eclipse.
- Output is displayed directly in the terminal.

--------------------------------------------------

Additional Testing:

During development, multiple test cases were used to validate edge cases such as:
- invalid characters
- non-rectangular mazes
- missing start or end points
- unsolvable mazes

These tests ensured the robustness of the program.

--------------------------------------------------

Additional Test Cases:

A folder named "test_cases" is included containing additional maze inputs used during development. 
These cover edge cases such as invalid input, missing start/end points, and unsolvable mazes.

These files are optional and are not required to run the program.

--------------------------------------------------

End of README