/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package google;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author bnevins
 */
public abstract class Sort<N extends Comparable> {
   N[] vals;
   
   Sort(N[] vals) {
       this.vals = vals;
   }
   
   final void exch(int one, int two) {
        N temp = vals[one];
        vals[one] = vals[two];
        vals[two] = temp;
    }
    final void dump() {
        System.out.println(Arrays.toString(vals));
    }
    static Integer[] makeIntegerArray(int n, int max) {
        Integer[] array = new Integer[n];
        Random r = new Random();
        
        for(int i = 0; i < n; i++) {
            array[i] = r.nextInt(max);
        }
        
        return array;
    }
    static Integer[] makeSortedIntegerArray(int n) {
        Integer[] array = new Integer[n];
        for(int i = 0; i < n; i++) {
            array[i] = i;
        }
        
        return array;
    }

    static Integer[] makeReverseSortedIntegerArray(int n) {
        Integer[] array = new Integer[n];
        int i = 0;
        int num = n - 1;
        while(i < n) {
            array[i] = num;
            --num;
            ++i;
        }
        
        return array;
    }
    abstract void sort();
}
