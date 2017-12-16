package com.elf.practice;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Byron Nevins
 */
public class Elevator2 {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("USAGE: #floors, #start, #end");
        } else {
            try {
                Elevator2.initializeProblem(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } catch (Exception e) {

            }
        }
    }

        int floor;
        int parent;
        int left;
        int right;
        int distTo;
    static Elevator2[] levels;
    private static int numFloors;
    private static int BOTTOM_FLOOR = 1;
    private static int startingFloor;
    private static int endingFloor;

    public static void initializeProblem(int theNumFloors, int theStartingFloor, int theEndingFloor) {
        numFloors = theNumFloors;
        startingFloor = theStartingFloor;
        endingFloor = theEndingFloor;
        levels = new Elevator2[numFloors + 1];
        setup(startingFloor, 0);
    }

    public Elevator2() {
        floor = parent = left = right = distTo = -1;
    }

    static void setup(int currFloor, int currParent) {

        if (levels[currFloor] == null) {
            // never saw this floor before
            levels[currFloor] = new Elevator2();

            Elevator2 currNode = levels[currFloor];

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
        Elevator2 currNode = levels[currFloor];
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

    public static void dump(Elevator2 node) {
        Deque<Elevator2> stack = new ArrayDeque<Elevator2>();
        
        while (true) {
            if (node.parent <= 0) {
               stack.push(node);
               break;
            }

            //System.out.println("DUMP: " + node);
            stack.push(node);
            node = levels[node.parent];
        }
        
        int ctr = 0;
        StringBuilder sb = new StringBuilder();
        
        while(stack.size() > 0) {
            Elevator2 elevator = stack.pop();
            
            if(ctr != 0)
                sb.append(elevator.floor).append('\n');
            
            if(stack.size() > 0)
                sb.append("Move #").append(++ctr).append(": ").append(elevator.floor).append("  -->  ");
            
        }
        System.out.println(sb.toString());
    
    }

    public String toString() {
        return "floor " + floor + ", left " + left + ", right " + right + ", distTo " + distTo + ", parent " + parent;
    }
}
