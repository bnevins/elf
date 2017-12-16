package com.elf.practice;

/**
 *
 * @author Byron Nevins
 */
public class Elevator3 {

    public static void main(String[] args) {
        if(args.length != 3)
            System.out.println("USAGE: #floors, start#, stop#");
        else
            Elevator3.initializeProblem(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }

    int floor;
    int parent;
    int left;
    int right;
    int distTo;
    static Elevator3[] levels;
    private static int numFloors;
    private static int BOTTOM_FLOOR = 1;
    private static int startingFloor;
    private static int endingFloor;

    public static void initializeProblem(int theNumFloors, int theStartingFloor, int theEndingFloor) {
        
        numFloors = theNumFloors;
        startingFloor = theStartingFloor;
        endingFloor = theEndingFloor;
        levels = new Elevator3[numFloors + 1];
        setup(startingFloor, 0);
    }

    public Elevator3() {
        floor = parent = left = right = distTo = -1;
    }

    static void setup(int currFloor, int currParent) {

        if (levels[currFloor] == null) {
            // never saw this floor before
            levels[currFloor] = new Elevator3();

            Elevator3 currNode = levels[currFloor];

            if (currParent == 0) {
                currNode.distTo = 0;
            } else {
                currNode.distTo = levels[currParent].distTo + 1;
            }

            currNode.floor = currFloor;
            currNode.parent = currParent;

            if (currFloor == endingFloor) {
                System.out.println("FOUND FLOOR: " + currNode);
                dump(currNode);
                System.exit(0);
            }
        } else {
            // is this path the best so far?
            int newDistTo = levels[currParent].distTo + 1;
            int oldDistTo = levels[currFloor].distTo;

            if (newDistTo < oldDistTo) {
                levels[currFloor].distTo = newDistTo;
                levels[currFloor].parent = currParent;
            } else // keep the old distTo and the old parent
            {
                return;
            }
        }

        System.out.println("XXX " + currFloor);
        Elevator3 currNode = levels[currFloor];
        currNode.right = currNode.floor + 11;
        currNode.left = currNode.floor - 6;

        if (currNode.right > numFloors) {
            currNode.right = 0;
        } else {
            setup(currNode.right, currNode.floor);
        }

        if (currNode.left < 1) {
            currNode.left = 0;
        } else {
            setup(currNode.left, currNode.floor);
        }

        System.out.println("Finished Setup: " + currNode.toString());
    }

    public static void dump(Elevator3 node) {
        while (true) {
            if (node.parent <= 0) {
                System.out.println("All Done!!");
                System.out.flush();
                return;
            }

            System.out.println("DUMP: " + node);
            node = levels[node.parent];

        }
    }

    public String toString() {
        return "floor " + floor + ", left " + left + ", right " + right + ", distTo " + distTo + ", parent " + parent;
    }
}
