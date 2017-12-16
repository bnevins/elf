package elf.algorithms;

import elf.algorithms.stdlib.StdOut;
import elf.algorithms.stdlib.StdRandom;

/*************************************************************************
 *  Compilation:  javac SortCompare.java
 *  Execution:    java SortCompare alg1 alg2 N T
 *  
 *  Sort N random real numbers, T times using the two
 *  algorithms specified on the command line.
 * 
 *  % java SortCompare Insertion Selection 1000 100 
 *  For 1000 random Doubles 
 *    Insertion is 1.7 times faster than Selection
 *
 *************************************************************************/

public class SortCompare { 

    public static double time(String alg, Double[] a) { 
        Stopwatch sw = new Stopwatch(); 
        if (alg.equals("Insertion"))  Insertion.sort(a); 
        //if (alg.equals("InsertionX")) InsertionX.sort(a); 
        if (alg.equals("Selection"))  Selection.sort(a); 
        if (alg.equals("Shell"))      Shell.sort(a); 
        if (alg.equals("Merge"))      Merge.sort(a); 
        //if (alg.equals("MergeX"))     MergeX.sort(a); 
        if (alg.equals("MergeBU"))    MergeBU.sort(a); 
        if (alg.equals("Quick"))      Quick.sort(a); 
        if (alg.equals("Quick3way"))  Quick3way.sort(a); 
        //if (alg.equals("QuickX"))     QuickX.sort(a); 
        if (alg.equals("Heap"))       Heap.sort(a); 
        return sw.elapsedTime(); 
    } 

    // Use alg to sort T random arrays of length N. 
    public static double timeRandomInput(String alg, int N, int T)  {
        double total = 0.0; 
        Double[] a = new Double[N]; 
        // Perform one experiment (generate and sort an array). 
        for (int t = 0; t < T; t++) {
            for (int i = 0; i < N; i++) 
                a[i] = StdRandom.uniform(); 
            total += time(alg, a); 
        } 
        return total; 
    } 

    public static void main(String[] args) { 
        System.setProperty("ALG_VERBOSE", "true");
        
        if(args.length == 0) {
            usage();
        }
        String alg1 = args[0]; 
        String alg2 = args[1]; 
        int N = Integer.parseInt(args[2]); 
        int T = Integer.parseInt(args[3]); 
        double time1 = timeRandomInput(alg1, N, T); // Total for alg1. 
        double time2 = timeRandomInput(alg2, N, T); // Total for alg2. 
        StdOut.printf("For %d random Doubles\n    %s is", N, alg1); 
        StdOut.printf(" %.1f times faster than %s\n", time2/time1, alg2); 
    }

    private static void usage() {
        StdOut.printf("USAGE:  SortCompare alg-1  alg-2  NumberItemsToSort   Number of trials\n\n");
        System.exit(1);
    }
} 
