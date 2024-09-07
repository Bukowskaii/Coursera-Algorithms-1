/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 24 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_RESET = "\u001B[0m";

    private int size;
    private boolean[] open;
    private int nOpen;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("N must be > 0");

        size = n;
        open = new boolean[(n * n)];
        nOpen = 0;

        // use n^2 + 2, so we have a nxn matrix + 2 virtual sites
        // 0 to n^2-1 will be the nodes,
        // n^2 is top,
        // n^2+1 is bottom
        uf = new WeightedQuickUnionUF((n * n) + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // if it's already open, do nothing
        if (!isOpen(row, col)) {
            int n = rc2n(row, col);
            if (row != 1 && isOpen(row - 1, col))
                uf.union(n, rc2n(row - 1, col));
            if (col != 1 && isOpen(row, col - 1))
                uf.union(n, rc2n(row, col - 1));
            if (row != size && isOpen(row + 1, col))
                uf.union(n, rc2n(row + 1, col));
            if (col != size && isOpen(row, col + 1))
                uf.union(n, rc2n(row, col + 1));

            if (row == 1)
                uf.union(n, size * size);
            if (row == size)
                uf.union(n, (size * size) + 1);

            open[n] = true;
            nOpen += 1;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int n = rc2n(row, col);
        return open[n];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int n = rc2n(row, col);
        return uf.find(n) == uf.find(size * size);
    }

    private int rc2n(int row, int col) {
        if (row > size || row < 1)
            throw new IllegalArgumentException(
                    "Row number must be less than matrix size (" + size + ")");
        if (col > size || col < 1)
            throw new IllegalArgumentException(
                    "Column number must be less than matrix size (" + size + ")");

        return ((row - 1) * size) + (col - 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(size * size) == uf.find((size * size) + 1);
    }

    private void print() {
        StringBuilder output = new StringBuilder();
        String current = "";

        output.append(padString(""));
        for (int c = 1; c <= size; c++)
            output.append(padString(Integer.toString(c)));
        output.append('\n');

        for (int r = 1; r <= size; r++) {
            output.append(padString(Integer.toString(r)));
            for (int c = 1; c <= size; c++) {
                if (isOpen(r, c)) {
                    if (isFull(r, c))
                        current = padString("███");
                    else
                        current = padString("░░░");
                }
                else {
                    current = padString("");
                }
                output.append(current);
            }
            output.append("\n");
        }

        StdOut.print("Current State:\n");
        StdOut.print(output);
        StdOut.print('\n');
    }

    private static String padString(String s) {
        if (s == null)
            s = "";

        return String.format("%4s", s);
    }

    public static void main(String[] args) {
        int n = 0;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        else
            n = 5;

        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            int randomRow = StdRandom.uniformInt(1, n + 1);
            int randomCol = StdRandom.uniformInt(1, n + 1);
            p.open(randomRow, randomCol);
            p.print();
        }
        StdOut.print("Percolated after " + p.numberOfOpenSites() + '\n');
    }
}
