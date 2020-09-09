/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.util.sort;

import com.elf.algorithms.stdlib.StdOut;

/**
 *
 * @author bnevns
 */
public class MinPQ<Key> extends PQ<Key> implements Iterable<Key> {

    public MinPQ() {
        maxSort = false;
    }

    public Key delMin() {
        return super.delMax();
    }

    public static void main(String[] args) {
        MinPQ<String> pq = new MinPQ<String>();
        String[] input = new String[]{"aaaa", "bbb", "ffff", "eeee", "qqqqq", "oooo", "zzzz", "xxxx"};

        for (String item : input) {
            //while (!StdIn.isEmpty()) {
            //String item = StdIn.readString();
            if (!item.equals("-")) {
                pq.insert(item);
            } else if (!pq.isEmpty()) {
                System.out.println(pq.delMax() + " ");
            }
        }
        System.out.println("(" + pq.size() + " left on pq)");
        while (!pq.isEmpty()) {
            System.out.print(pq.delMax() + " ");
        }
    }

}
