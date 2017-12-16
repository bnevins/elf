/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import static google.Sort.makeIntegerArray;
import java.util.Arrays;

/**
 *
 * @author bnevins
 */
public class SortInsertion<N extends Comparable> extends Sort {

    public static void main(String[] args) {
        //String[] vals = new String[]{"ccc", "aaa", "qq", "d", "ffff", "zzz", "bbb"};
        //SortInsertion<Integer> sorter = new SortInsertion<>(makeIntegerArray(40000, 100000));
        SortInsertion<Integer> sorter = new SortInsertion<>(makeSortedIntegerArray(40000));
        sorter.sort();
    }
    private boolean verbose = false;

    public SortInsertion(N[] vals) {
        super(vals);
        dump();
    }

    @Override
    void sort() {
        //Comparable[] vals2 = Arrays.copyOf(vals, vals.length);

        for (int i = 1; i < vals.length; i++) {
            for (int j = i; j > 0 && vals[j - 1].compareTo(vals[j]) > 0; j--) {
                exch(j - 1, j);
            }
        }
        if (verbose) {
            dump();
        }
    }
}
