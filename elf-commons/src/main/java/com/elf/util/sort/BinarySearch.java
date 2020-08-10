/*
 * BinarySearch.java
 *
 * Created on November 9, 2007, 10:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.elf.util.sort;

/**
 *
 * @author bnevins
 */
public class BinarySearch {

    public BinarySearch(int[] a, int elem) {
        this.a = a;
        this.elem = elem;
    }

    public boolean search() {
        return search(0, a.length - 1, elem);
    }

    private static int[] getSortedArray(int num) {
        int[] a = new int[num];
        for (int i = 0; i < num; i++) {
            a[i] = i;
        }

        return a;
    }

    private boolean search(int lo, int hi, int elem) {
        ++numSearches;
        System.out.println("Lo:Hi " + lo + " : " + hi);
        if (lo > hi) {
            System.out.println("ERROR");
        }

        if (lo == hi) {
            debug("Search found (lo == high) for " + elem + " at element #" + lo);
            return a[lo] == elem;
        }

        int mid = (lo + hi) >>> 1;

        if (elem > a[mid]) {
            return search(mid + 1, hi, elem);
        }
        if (elem < a[mid]) {
            return search(lo, mid - 1, elem);
        } else {
            debug("Match found (mid) for " + elem + " at element #" + mid);
            return true;
        }
    }

    public static void main(String[] args) {
        final int num = 200 * 1000 * 1000;
        System.out.println("NUM " + num);
        int elem = 200 * 1000 * 1000 + 1;

        BinarySearch search = new BinarySearch(getSortedArray(num), elem);
        boolean found = search.search();
        if (!found) {
            System.out.println("Can't find item!");
        } else {
            System.out.println("Found item!");
        }
    }

    private void debug(String string) {
        if (debug) {
            System.out.println(string);
        }
    }
    private int[] a;
    private int elem;
    private int numSearches = 0;

    // set this to true to debug...
    private boolean debug = true;

}
