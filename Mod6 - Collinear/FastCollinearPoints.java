/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 26 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        // check for null input
        if (points == null)
            throw new IllegalArgumentException("point array must not be null");

        // copy
        Point[] deepCopy = points.clone();
        // check for any p in input null
        for (int i = 0; i < deepCopy.length; i++)
            if (deepCopy[i] == null)
                throw new IllegalArgumentException("no points in array can be null");

        // sort
        Arrays.sort(deepCopy);
        // check for any duplicates
        for (int i = 0; i < deepCopy.length; i++)
            if (i != 0 && deepCopy[i].compareTo(deepCopy[i - 1]) == 0)
                throw new IllegalArgumentException("no points in array can be duplicates");


        ArrayList<LineSegment> found = new ArrayList<LineSegment>();
        for (int p = 0; p < deepCopy.length - 1; p++) {

            Point[] localCopy = Arrays.copyOfRange(deepCopy, p + 1, deepCopy.length - 1);
            Arrays.sort(localCopy, deepCopy[p].slopeOrder());

            int pointCounter = 1;
            for (int q = 1; q < localCopy.length; q++) {
                if (Double.compare(deepCopy[p].slopeTo(localCopy[q - 1]),
                                   deepCopy[p].slopeTo(localCopy[q])) == 0) {
                    pointCounter += 1;
                }
                else {
                    if (pointCounter >= 3) {
                        found.add(new LineSegment(deepCopy[p], localCopy[q - 1]));
                    }
                    pointCounter = 1;
                }
            }
            if (pointCounter >= 3) {
                found.add(new LineSegment(deepCopy[p], localCopy[localCopy.length - 1]));
            }
        }

        segments = found.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
