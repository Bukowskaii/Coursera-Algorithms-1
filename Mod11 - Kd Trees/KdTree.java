/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Sep 7 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private static final double LINE_SIZE = 0.001;
    private static final double POINT_SIZE = 0.05;

    private Node2D root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        root = insert(root, p, false, null);
    }

    private Node2D insert(Node2D n, Point2D p, boolean h, Node2D parent) {
        // reached the end of the tree, add new node to empty leaf
        if (n == null) {
            size++;
            return new Node2D(p, h, parent);
        }

        // current node == point we're trying to add
        // return self before modification
        if (n.point.compareTo(p) == 0) return n;

        // set the right or left node based on node comparison
        if (n.compareTo(p) > 0)
            n.left = insert(n.left, p, !h, n);
        else
            n.right = insert(n.right, p, !h, n);

        // return self after modification
        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        return contains(root, p);

    }

    private boolean contains(Node2D n, Point2D p) {
        if (n == null)
            return false;

        if (p.equals(n.point))
            return true;

        if (n.compareTo(p) > 0)
            return contains(n.left, p);
        else
            return contains(n.right, p);
    }

    // draw all points to standard draw
    public void draw() {
        if (!isEmpty())
            root.draw();
    }

    // print to stdout
    private void print() {
        if (!isEmpty()) {
            StdOut.println("Kd Tree - size=" + size);
            root.print(0);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("rectangle must not be null");

        return range(root, rect, new ArrayList<Point2D>());
    }

    private Iterable<Point2D> range(Node2D n, RectHV r, ArrayList<Point2D> iP) {
        if (n == null) return iP;

        if (n.rect.intersects(r)) {
            if (r.contains(n.point))
                iP.add(n.point);

            range(n.left, r, iP);
            range(n.right, r, iP);
        }

        return iP;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        if (isEmpty())
            return null;

        return nearest(root, p, root).point;
    }

    private Node2D nearest(Node2D current, Point2D p, Node2D nearest) {
        if (current == null)
            return nearest;

        if (nearest.point.distanceSquaredTo(p) < current.point.distanceSquaredTo(p))
            return nearest;

        if (current.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p))
            nearest = current;

        Node2D left = nearest(current.left, p, nearest);
        if (left.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p))
            nearest = left;

        Node2D right = nearest(current.right, p, nearest);
        if (right.point.distanceSquaredTo(p) < nearest.point.distanceSquaredTo(p))
            nearest = right;

        return nearest;
    }

    private class Node2D {

        private Point2D point;
        private Node2D left;
        private Node2D right;
        private boolean horizontal;
        private RectHV rect;

        private Node2D(Point2D p, boolean h, Node2D parent) {
            point = p;
            horizontal = h;

            if (parent == null)
                rect = new RectHV(0, 0, 1, 1);
            else {
                double minX = parent.rect.xmin();
                double minY = parent.rect.ymin();
                double maxX = parent.rect.xmax();
                double maxY = parent.rect.ymax();

                if (horizontal)
                    if (parent.compareTo(point) > 0)
                        maxX = parent.point.x();
                    else
                        minX = parent.point.x();
                else if (parent.compareTo(point) > 0)
                    maxY = parent.point.y();
                else
                    minY = parent.point.y();

                rect = new RectHV(minX, minY, maxX, maxY);
            }
        }

        public int compareTo(Point2D that) {
            if (horizontal)
                return Double.compare(point.y(), that.y());
            else
                return Double.compare(point.x(), that.x());
        }

        private void draw() {
            StdDraw.setPenRadius(LINE_SIZE);
            if (horizontal) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }
            else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            }

            StdDraw.setPenRadius(POINT_SIZE);
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();

            if (left != null)
                left.draw();
            if (right != null)
                right.draw();
        }

        private void print(int level) {
            String prefix = "";
            if (level != 0)
                prefix = String.format("%" + level * 2 + "s", "");

            String suffix = horizontal ? " Horizontal" : " Vertical";
            StdOut.println(prefix + point + suffix);


            if (left != null) {
                StdOut.println(prefix + "Left Nodes:");
                left.print(level + 1);
            }

            if (right != null) {
                StdOut.println(prefix + "Right Nodes:");
                right.print((level + 1));
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));

        kdtree.draw();
        kdtree.print();

        if (args.length == 1) {
            // initialize the two data structures with point from file
            String filename = args[0];
            In in = new In(filename);
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                StdOut.println(kdtree.nearest(p));
            }
        }
    }
}
