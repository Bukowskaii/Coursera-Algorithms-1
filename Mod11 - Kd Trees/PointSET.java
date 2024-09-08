/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Sep 7 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        points.forEach(Point2D::draw);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("rectangle must not be null");

        ArrayList<Point2D> insidePoints = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax() &&
                    point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
                insidePoints.add(point);
            }
        }
        return insidePoints;
    }


    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("point must not be null");

        if (isEmpty())
            return null;

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D closestPoint = null;
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < minDistance) {
                minDistance = point.distanceSquaredTo(p);
                closestPoint = point;
            }
        }
        return closestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
