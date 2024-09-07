/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 25 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());

        for (int i = 0; i < n; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
