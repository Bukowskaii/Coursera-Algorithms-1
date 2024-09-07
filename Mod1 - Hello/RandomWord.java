/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 22 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        int i = 1;
        while (!StdIn.isEmpty()) {
            String next = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / i))
                champion = next;
            i++;
        }
        StdOut.println(champion);
    }
}
