package google;

import java.util.Arrays;

public class FancyCheckersBottomUp {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int startrow = 1;
        int startcol = 3;

        calcarrays();
    }

    private static void calcarrays() {
        for (int y = 2; y <= numRows; y++) {
            for (int x = 1; x <= numCols; x++) {

            }
        }
    }

    private static int minCost(int endi, int endj) {
        return 1;
    }

    private static int min(int i1, int i2, int i3) {
        int min = i1;
        if (i2 < min)
            min = i2;
        if (i3 < min)
            min = i3;
        return min;
    }
    private static final int HUGE = 99999;
    private final static int[][] board = new int[][]{
        {0, 0, 0, 0, 0, 0},
        {1, HUGE, HUGE, 5, HUGE, HUGE}, {2, HUGE, 6, 7, 0, HUGE},
        {3, 3, 5, 7, 8, 2}, {4, 7, 6, 1, 1, 4}, {5, 6, 7, 4, 7, 8}
    };
    private static final int numCols = board[0].length - 1;
    private static final int numRows = board.length - 1;
    private static final int[][] costs;
    private static final int[][] paths = new int[numCols][numRows];

    static {
        costs = new int[numCols][numRows];
        for (int i = 0; i < costs.length; i++)
            Arrays.fill(costs[i], -10);
    }

}
