/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.practice;

import java.util.ArrayList;

/**
 *
 * @author bnevins
 */
public class Building {

    private final static int TOP_FLOOR = 50;
    private static int BOTTOM_FLOOR = 1;
    private static final int STARTING_FLOOR = 32;
    private static final int ENDING_FLOOR = 33;

    public static void main(String[] args) {
        System.out.println("Hi");
        new Node(STARTING_FLOOR, null);
    }

    private static class Node {

        // 1-based
        private static boolean[] map = new boolean[51];
        private int floor;
        private Node right;
        private Node left;
        private ArrayList<Node> parents = new ArrayList<Node>(); 
        private static final int UP = 11;
        private static final int DOWN = -6;
        private boolean ok = true;
        private Node getNode(int floor) {
            return null;
        }
        
        Node(int floor, Node parent) {
            if (map[floor]) {
                System.out.println("Floor " + floor + " was already visited.  Skipping...");
                getNode(floor).parents.add(parent);
                ok = false;
            } else {
                map[floor] = true;
                parents.add(parent);
            }

            int rfloor = floor + UP;
            int lfloor = floor + DOWN;
            System.out.println("Creating Node for " + floor);

            if (rfloor <= Building.TOP_FLOOR) {
                if (map[rfloor]) {
                    right = null;
                } else {
                    right = new Node(rfloor, this);
                    if (!right.ok) {
                        right = null;
                    }
                }
            } else {
                right = null;
            }

            if (lfloor >= Building.BOTTOM_FLOOR) {
                if (!map[lfloor]) {
                    left = new Node(lfloor, this);

                    if (!left.ok) {
                        left = null;
                    } else {
                        left = null;
                    }
                }

                if (floor == ENDING_FLOOR) {
                    System.out.println("DONE!!!!  floor " + floor);
                    System.out.println("XXXXXXXX");
                    for (Node node : parents)
                        System.out.println("PARENT: " + node.floor);
                    System.exit(0);
                }
            }
        }
    }
}
