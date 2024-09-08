/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Sep 7 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode solution;

    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        public SearchNode predecessor;
        public int moves;
        public int priority;

        public SearchNode(Board b, SearchNode p) {
            board = b;
            predecessor = p;
            moves = p == null ? 0 : p.moves + 1;
            priority = board.manhattan() + moves;
        }

        public void insert(MinPQ<SearchNode> queue) {
            for (Board n : board.neighbors()) {
                if (predecessor != null && n.equals(predecessor.board)) continue;

                SearchNode node = new SearchNode(n, this);
                queue.insert(node);
            }
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(priority, that.priority);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("inital board must not be null");

        MinPQ<SearchNode> solutionQ = new MinPQ<SearchNode>();

        SearchNode firstNode = new SearchNode(initial, null);
        solutionQ.insert(firstNode);

        int i = 0;
        int upperBound = (initial.dimension() * 2) + (int) Math.pow(initial.dimension(), 2);
        while (i <= upperBound) {
            if (solutionQ.isEmpty())
                break;

            SearchNode node = solutionQ.delMin();

            if (node.board.isGoal()) {
                solution = node;
                break;
            }

            node.insert(solutionQ);
            i = node.moves;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;

        return solution.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> boards = new Stack<>();
        SearchNode next = solution;
        while (next != null) {
            boards.push(next.board);
            next = next.predecessor;
        }
        return boards;
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
