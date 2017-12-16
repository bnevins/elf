package google;

import java.util.*;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Given a square of side = n. Starting at upper-left (0,0) how many ways to get
 * to n-1, n-1 You can only move 1-square down or 1 square to the right
 *
 * @author wnevins
 */
public class Robot {

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Robot r = new Robot(3, 3);
    }
    private static final boolean debug = true;
    int height;
    int width;
    int counter;
    private final Stack<Node> mainStack = new Stack<Node>();
    private final Queue<Node> currentPathQueue = new LinkedList<Node>();
    private int[][] nums;
    private int currentPathNum;

    private Robot(int w, int h) {
        height = h;
        width = w;
        useStack();
        useRecursion();
    }

    private void useStack() {
        reset();
        ++nums[0][0];
        mainStack.push(new Node(0, 0));
        numPathsUsingStack();
        int numpaths = nums[width - 1][height - 1];
        System.out.printf("NumPaths using Stack = %d, num-pops= %d\n", numpaths, counter);
    }

    private void useRecursion() {
        reset();
        numPathsRecursive(0, 0);
        int numpaths = nums[width - 1][height - 1];
        System.out.printf("NumPaths using Recursion = %d, num-recursions= %d\n", numpaths, counter);
        printPath();
    }

    private void reset() {
        counter = 0;
        nums = new int[width][height];
        currentPathNum = 0;
    }

    void numPathsUsingStack() {
        while (!mainStack.isEmpty()) {
            counter++;
            Node node = mainStack.pop();
            System.out.printf("%d:%d ", node.x, node.y);

            if (counter % 10 == 0) {
                System.out.println("");
            }

            Node n1 = new Node(node.x + 1, node.y);
            Node n2 = new Node(node.x, node.y + 1);

            if (n1.isValid()) {
                ++nums[n1.x][n1.y];
                mainStack.push(n1);
            }
            if (n2.isValid()) {
                ++nums[n2.x][n2.y];
                mainStack.push(n2);
            }
        }
        System.out.println("");
    }

    void numPathsRecursive(int x, int y) {
        // off the playing field!
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        ++nums[x][y];
        ++counter;
        currentPathQueue.add(new Node(x, y));
        dumpArray();
        if (x == width - 1 && y == height - 1) {
            return; // all done!!
        }

        numPathsRecursive(x + 1, y);
        numPathsRecursive(x, y + 1);
    }

    private void printPath() {
        System.out.printf("Path# %d:  ", ++currentPathNum);
        while (!currentPathQueue.isEmpty()) {
            Node n = currentPathQueue.remove();
            System.out.printf("%s ", n);
        }
        System.out.println("");
        currentPathQueue.clear();
    }

    private void dumpArray() {

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; x++) {
                System.out.printf("%4d", nums[x][y]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    class Node {

        int x, y;

        Node(int xx, int yy) {
            x = xx;
            y = yy;
        }

        public String toString() {
            return "[" + x + "," + y + "]";
        }

        boolean isValid() {
            return x >= 0 && x < width
                    && y >= 0 && y < height;
        }
    }
}
