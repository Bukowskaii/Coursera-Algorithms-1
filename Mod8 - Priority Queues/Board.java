/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Sep 7 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private int[][] tiles;
    private int hamming;
    private int manhattan;
    private int emptyRow;
    private int emptyCol;
    private int inversions;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tilesIn) {
        tiles = new int[tilesIn.length][tilesIn.length];
        int[] linearTiles = new int[(tilesIn.length * tilesIn.length) - 1];

        emptyCol = tiles.length;
        emptyRow = tiles.length;
        inversions = 0;

        hamming = 0;
        manhattan = 0;
        int i = 0;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                tiles[row][col] = tilesIn[row][col]; // assign class object

                // find the target
                int target = row * tiles.length + col + 1;
                if (row == tiles.length - 1 && col == tiles.length - 1)
                    target = 0; // bottom right corner, target is 0

                if (tiles[row][col] == 0) {
                    // found empty, store to generate neighbors in constant time
                    emptyRow = row;
                    emptyCol = col;
                    continue;
                }
                else {
                    linearTiles[i++] = tilesIn[row][col];
                }

                if (tiles[row][col] != target) {
                    hamming++; // out of place, increment hamming

                    // calculate manhattan distance
                    int targetRow = (tiles[row][col] - 1) / tiles.length;
                    int targetCol = (tiles[row][col] - 1) - targetRow * tiles.length;
                    manhattan += Math.abs(row - targetRow) + Math.abs(col - targetCol);
                }
            }
        }

        for (i = 0; i < linearTiles.length; i++) {
            if (linearTiles[i] == 0) continue;
            for (int j = i + 1; j < linearTiles.length; j++) {
                if (linearTiles[j] == 0) continue;

                if (linearTiles[i] > linearTiles[j]) inversions++;
            }
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(tiles.length);
        builder.append("\n");

        int padding = (int) Math.log10(tiles.length * tiles.length) + 2;

        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                builder.append(String.format("%" + padding + "s", tiles[row][col])); // build string
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0 && manhattan == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // if its null or wrong class
        if (y == null) return false;
        if (getClass() != y.getClass()) return false;

        // cast and check dimensions
        Board yBoard = (Board) y;
        if (tiles.length != yBoard.tiles.length) return false;

        // check all tiles
        for (int row = 0; row < tiles.length; row++)
            for (int col = 0; col < tiles.length; col++)
                if (tiles[row][col] != yBoard.tiles[row][col])
                    return false;

        // if we got here, they are the same
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();

        // build neighbors
        int[][] neighborTiles;
        if (emptyRow != 0) {
            // build neighbor by shifting from above
            neighborTiles = cloneTiles();
            neighborTiles[emptyRow][emptyCol] = tiles[emptyRow - 1][emptyCol];
            neighborTiles[emptyRow - 1][emptyCol] = 0;
            neighbors.add(new Board(neighborTiles));
        }
        if (emptyRow != tiles.length - 1) {
            // build neighbor by shifting from below
            neighborTiles = cloneTiles();
            neighborTiles[emptyRow][emptyCol] = tiles[emptyRow + 1][emptyCol];
            neighborTiles[emptyRow + 1][emptyCol] = 0;
            neighbors.add(new Board(neighborTiles));
        }
        if (emptyCol != 0) {
            // build neighbor by shifting from left
            neighborTiles = cloneTiles();
            neighborTiles[emptyRow][emptyCol] = tiles[emptyRow][emptyCol - 1];
            neighborTiles[emptyRow][emptyCol - 1] = 0;
            neighbors.add(new Board(neighborTiles));
        }
        if (emptyCol != tiles.length - 1) {
            // build neighbor by shifting from left
            neighborTiles = cloneTiles();
            neighborTiles[emptyRow][emptyCol] = tiles[emptyRow][emptyCol + 1];
            neighborTiles[emptyRow][emptyCol + 1] = 0;
            neighbors.add(new Board(neighborTiles));
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    // I guess lets pick two at random?
    public Board twin() {
        int[][] clone = cloneTiles();

        if (emptyRow != 0) { // if the empty is not in the first row
            // swap the first rows first two tiles
            clone[0][0] = tiles[0][1];
            clone[0][1] = tiles[0][0];
        }
        else {
            // swap the second rows first two tiles
            clone[1][0] = tiles[1][1];
            clone[1][1] = tiles[1][0];
        }

        return new Board(clone);
    }

    private boolean isSolvable() {
        if (tiles.length % 2 == 1) {
            return inversions % 2 == 0;
        }
        else {
            if ((tiles.length - emptyRow) % 2 == 1)
                return inversions % 2 == 0;
            else
                return inversions % 2 == 1;
        }
    }

    private int[][] cloneTiles() {
        int[][] clone = new int[tiles.length][tiles.length];

        for (int row = 0; row < tiles.length; row++)
            for (int col = 0; col < tiles.length; col++)
                clone[row][col] = tiles[row][col];

        return clone;
    }

    private static Board generate(int dimension) {
        int[] nums = new int[dimension * dimension];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }
        StdRandom.shuffle(nums);

        int[][] tiles = new int[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                tiles[row][col] = nums[row * dimension + col];
            }
        }
        return new Board(tiles);
    }

    public static void main(String[] args) {
        int dimension = 4;
        if (args.length >= 1)
            dimension = Integer.parseInt(args[0]);

        int count = 1;
        if (args.length >= 2)
            count = Integer.parseInt((args[1]));


        if (count == 1) {
            Board b = Board.generate(dimension);
            StdOut.println("Board:");
            StdOut.print(b.toString());
            StdOut.println("Hamming Score: " + b.hamming());
            StdOut.println("Manhattan Score: " + b.manhattan());
            StdOut.println("Solvable: " + b.isSolvable());
        }
        else {
            int numSolved = 0;
            int numSolvable = 0;
            int numGenerated = 0;

            for (int n = 0; n < count; n++) {
                Board b = Board.generate(dimension);
                if (b.isSolvable())
                    numSolvable++;
                if (b.isGoal())
                    numSolved++;
                numGenerated++;
            }

            StdOut.println("Generated " + numGenerated + " random boards");
            StdOut.println(numSolvable + " were solvable.");
            StdOut.println(numSolved + " were solved.");
        }

        // println("Board Neighbors:");
        // for (Board neighbor : b.neighbors()) {
        //    StdOut.print(neighbor.toString());
        //    StdOut.println("Hamming Score: " + neighbor.hamming());
        //    StdOut.println("Manhattan Score: " + neighbor.manhattan());
        //    StdOut.println();
        //}

    }
}
