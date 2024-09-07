/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 24 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONF_95 = 1.96;
    private double[] runps;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("N must be > 0");
        if (trials <= 0)
            throw new IllegalArgumentException("T must be > 0");

        runps = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int randomRow = StdRandom.uniformInt(1, n + 1);
                int randomCol = StdRandom.uniformInt(1, n + 1);
                p.open(randomRow, randomCol);
            }
            runps[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(runps);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(runps);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONF_95 * stddev()) / Math.sqrt(runps.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONF_95 * stddev()) / Math.sqrt(runps.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException("Must provide a size and trial count");

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats s = new PercolationStats(n, t);
        StdOut.print("mean = " + s.mean() + "\n");
        StdOut.print("stddev = " + s.stddev() + "\n");
        StdOut.print(
                "95% confidence interval = [" + s.confidenceLo() + ", " + s.confidenceHi()
                        + "]\n");

    }

}
