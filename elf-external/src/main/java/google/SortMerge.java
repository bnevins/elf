/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author bnevins
 */
public class SortMerge<N extends Comparable> extends Sort {

    private static final String PHONE_FILENAME = "c:/temp/10mphonenumbers.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //new PhoneNumberGenerator("c:/temp/10mphonenumbers.txt");
        if (!new File(PHONE_FILENAME).isFile()) {
            new PhoneNumberGenerator(PHONE_FILENAME);
        }

        SortMerge<Integer> sorter = new SortMerge<>(new Integer[]{3, 1, 9, 2});
        //SortMerge<Integer> sorter = new SortMerge<>(makeIntegerArray(25, 1000));
        //SortMerge<Character> sorter = new SortMerge<>(makeCharacterArray(25, 1000000));
        //sorter.sort();
    }
    Comparable[] aux;

    public SortMerge(Comparable[] vals) {
        super(vals);
        aux = Arrays.copyOf(vals, vals.length);
    }

    @Override
    void sort() {
        dump();
        sort(vals, 0, vals.length - 1);
        dump();
    }

    void sort(Comparable[] theVals, int low, int high) {
        System.out.printf("sort(%d, %d)\n", low, high);

        if (high <= low) {
            return;
        }

        int mid = low + (high - low) / 2;

        sort(theVals, low, mid);
        sort(theVals, mid + 1, high);
        merge(theVals, low, mid, high);
    }

    // lo..mid is sorted, mid+1 .. high is independently sorted already. 
    // Merge those 2 bad boys.
    // 
    private void merge(Comparable[] theVals, int lo, int mid, int high) {
        System.out.printf("merge(%d, %d, %d)\n", lo, mid, high);
        int left = lo;
        int right = mid + 1;

        for (int i = lo; i <= high; i++) {
            aux[i] = theVals[i];
        }

        for (int i = lo; i <= high; i++) {
            if (left > mid) {
                theVals[i] = aux[right++];
            } else if (right > high) {
                theVals[i] = aux[left++];
            } else if (aux[left].compareTo(aux[right]) <= 0) {
                theVals[i] = aux[left++];
            } else {
                theVals[i] = aux[right++];
            }
        }
    }

    private static class PhoneNumberGenerator {

        private static final int NUM_PHONE_NUMS = 10 * 1000 * 1000;

        public PhoneNumberGenerator(String fname) {
            FileWriter fw = null;
            Random random = new Random();
            try {
                fw = new FileWriter(fname, false);
                for (int i = 0; i < NUM_PHONE_NUMS; i++) {
                    int num = 1000 * 1000;
                    num += random.nextInt(9000 * 1000);
                    fw.write(Integer.toString(num));
                    fw.write("\n");
                }
            }
            catch (Exception e) {
            }
        }
    }
}
