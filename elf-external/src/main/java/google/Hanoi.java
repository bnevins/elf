/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 *
 * @author bnevins
 */
public class Hanoi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Hanoi();
    }
    private static final int NUM_DISKS = 64;

    Deque<Integer> A = new ArrayDeque<>();
    Deque<Integer> B = new ArrayDeque<>();
    Deque<Integer> C = new ArrayDeque<>();

    Hanoi() {

        for (int i = NUM_DISKS; i > 0; i--) {
            A.push(i);
        }
        move();
        
        if(C.peek().equals(1) && C.size() == NUM_DISKS)
            System.out.println("CORRECT!!");
    }

    private void move() {
        move(A, C, B, NUM_DISKS);
    }

    private void move(Deque<Integer> from, Deque<Integer> to, Deque<Integer> working, int ht) {
        System.out.println("move called.");
        if (ht < 1) {
            return;
        }

        move(from, working, to, ht - 1);
        to.push(from.pop());
        move(working, to, from, ht - 1);
    }

}
