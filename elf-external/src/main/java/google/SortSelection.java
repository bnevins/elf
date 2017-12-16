/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.Arrays;
import java.util.Random;

public class SortSelection<N extends Comparable> extends Sort {

    public static void main(String[] args) {
        //String[] vals = new String[]{"ccc", "aaa", "qq", "d", "ffff", "zzz", "bbb"};
        //SlowSort<String> sorter = new SortSelection<>(vals);
        SortSelection<Integer> sorter = new SortSelection<>(makeIntegerArray(40000, 100000));
        sorter.sort();
    }
    private boolean doPrint = false;

    public SortSelection(N[] vals) {
        super(vals);
    }

    @Override
    void sort() {
        int chunk = 1 + (vals.length / 10);

        for (int left = 0; left < vals.length; left++) {
            for (int right = left + 1; right < vals.length; right++) {
                if (vals[right].compareTo(vals[left]) < 0) {
                    exch(left, right);
                }
            }
            if (left % chunk == 0  && doPrint) {
                System.out.println(Arrays.toString(vals));
            }
        }
    }
}
