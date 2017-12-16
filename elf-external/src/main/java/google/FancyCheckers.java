package google;

import java.util.Arrays;

/**
 *
 * http://en.wikipedia.org/wiki/Dynamic_programming
 *
Consider a checkerboard with n × n squares and a cost-function c(i, j) which returns a cost associated with square i, j (i being the row, j being the column). For instance (on a 5 × 5 checkerboard),
5 	6 	7 	4 	7 	8
4 	7 	6 	1 	1 	4
3 	3 	5 	7 	8 	2
2 	- 	6 	7 	0 	-
1 	- 	- 	*5* 	- 	-
1 	2 	3 	4 	5

Thus c(1, 3) = 5

Let us say you had a checker that could start at any square on the first rank (i.e., row) and you wanted to know the shortest path (sum of the costs of the visited squares are at a minimum) to get to the last rank, assuming the checker could move only diagonally left forward, diagonally right forward, or straight forward. That is, a checker on (1,3) can move to (2,2), (2,3) or (2,4).
5
4
3
2 		x 	x 	x
1 			o
1 	2 	3 	4 	5

This problem exhibits optimal substructure. That is, the solution to the entire problem relies on solutions to subproblems. Let us define a function q(i, j) as

q(i, j) = the minimum cost to reach square (i, j)

If we can find the values of this function for all the squares at rank n, we pick the minimum and follow that path backwards to get the shortest path.

Note that q(i, j) is equal to the minimum cost to get to any of the three squares below it (since those are the only squares that can reach it) plus c(i, j). For instance:
5
4 			A
3 		B 	C 	D
2
1
1 	2 	3 	4 	5

q(A) = min(q(B),q(C),q(D)) + c(A)

Now, let us define q(i, j) in somewhat more general terms:

q(i,j)=\begin{cases} \infty & j < 1 \mbox{ or }j > n \\ c(i, j) & i = 1 \\ \min(q(iHUGE, jHUGE), q(iHUGE, j), q(iHUGE, j+1)) + c(i,j) & \mbox{otherwise.}\end{cases}

The first line of this equation is there to make the recursive property simpler (when dealing with the edges, so we need only one recursion). The second line says what happens in the last rank, to provide a base case. The third line, the recursion, is the important part. It is similar to the A,B,C,D example. From this definition we can make a straightforward recursive code for q(i, j). In the following pseudocode, n is the size of the board, c(i, j) is the cost-function, and min() returns the minimum of a number of v
 * @author wnevins
 */
public class FancyCheckers {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //dump();
        int cost = minCost(5, 3);
        System.out.println("Cost from 1,3 to 5,3 = " + cost);
    }

    private static int minCost(int endi, int endj) {
        if (endj < 1 || endj > numCols)
            return HUGE;

        if (costs[endi][endj] == -10) {
            costs[endi][endj] = minCostInternal(endi, endj);
            //System.out.printf("minCost(%d, %d) == %d\n", endi, endj, costs[endi][endj]);
        }

        return costs[endi][endj];
    }

    private static int minCostSlow(int endi, int endj) {
        // just to make printing easy!
        int ret = minCostInternal(endi, endj);
        //System.out.printf("minCost(%d, %d) == %d\n", endi, endj, ret);
        return ret;
    }

    private static int minCostInternal(int endi, int endj) {
        // return lowest cost to this end point

        int costhere = board[endi][endj];

        if (endi == 1)
            return costhere;

        int rowbelow = endi - 1;
        return costhere + min(rowbelow, endj,
                minCost(rowbelow, endj - 1),
                minCost(rowbelow, endj),
                minCost(rowbelow, endj + 1));
    }

    private static int min(int row, int col, int i1, int i2, int i3) {
        int min = i1;
        if (i2 < min)
            min = i2;
        if (i3 < min)
            min = i3;
        if (i1 == min)
            col -= 1;
        else if (i3 == min)
            col += 1;

        if (min < HUGE)
            System.out.printf("%d,%d wins\n", row, col);
        return min;
    }

    private static void dump() {
        System.out.printf("Rows: %d, Columns: %d\n", board.length, board[0].length);
        for (int i = board.length - 1; i >= 0; i--) {
            System.out.printf("%d: ", i);
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("   " + "0 1 2 3 4");
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

    static {
        costs = new int[board[0].length][board.length];
        for (int i = 0; i < costs.length; i++)
            Arrays.fill(costs[i], -10);
    }
}
