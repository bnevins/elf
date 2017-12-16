/*
 Here are the 38 numbers that sum up to 2,000,000 (believe me!):

 14175, 15055, 16616, 17495, 18072, 19390, 19731, 22161, 23320, 23717, 26343, 28725, 29127, 32257, 40020, 41867, 43155, 46298, 56734, 57176, 58306, 61848, 65825, 66042, 68634, 69189, 72936, 74287, 74537, 81942, 82027, 82623, 82802, 82988, 90467, 97042, 97507, 99564

 We want to find out exactly 19 of these that sum up to 1,000,000. If there are no such numbers we should say so.

 (Hint: There are such 19 numbers in the list).
 */
package com.elf.practice;

import java.util.Arrays;

/**
 *
 * @author bnevins
 */
public class SetOfSums {

    private final static int teenyArray[] = {3, 2, 7}; // find 6 
    private final static int tinyArray[] = {3, 2, 7, 1}; // find 6 
    // http://algorithms.tutorialhorizon.com/dynamic-programming-subset-sum-problem/

    private final static int smallArray[] = {2, 6, 11, 7, 19, 4, 44, 23, 77}; // find 4 that sum to 24 == 2,7,11,4
    private final static int bigArray[] = {
        14175, 15055, 16616, 17495, 18072, 19390, 19731, 22161, 23320, 23717,
        26343, 28725, 29127, 32257, 40020, 41867, 43155, 46298, 56734, 57176,
        58306, 61848, 65825, 66042, 68634, 69189, 72936, 74287, 74537, 81942,
        82027, 82623, 82802, 82988, 90467, 97042, 97507, 99564};
    private static int NUM_IN_SUBSET = 0;
    private final static int memo[] = new int[bigArray.length];
    private static long numCalls = 0;
    private static int[] solutionsCopy;
    private static boolean debug = false;
    private static long start = System.currentTimeMillis();

    public static void main(String[] args) {

        //bruteForce();
        //int set[] = {3, 34, 4, 12, 5, 2};
        // int sum = 9;
        //        //if (isSubsetSum(set, n, sum) == true) {
        int[] theArray = bigArray;
        int theSum = 1000000;

        System.out.println("NumCalls (): " + (numCalls));

        int solutions[] = new int[theArray.length];
        numCalls = 0;
        NUM_IN_SUBSET = 19;
        find(theArray, 0, 0, theSum, solutions);
        System.out.printf("\nfind had %d  method calls and it returned: %s\n", numCalls, Arrays.toString(solutions));

        for (int i = 0; i < solutionsCopy.length; i++) {
            if (solutionsCopy[i] == 1) {
                System.out.print(" X ");
            } else {
                System.out.print(" - ");
            }
        }

    }

    public static void find(final int[] A, int currSum, int index, final int sum, final int[] solution) {
        ++numCalls;

        if (debug) {
            System.out.printf("%2d  %8d  %8d\n", numCalls, currSum, index);
        }

        if (currSum == sum) {
            int numInThisSet = dumpSolution(A, solution);

            if (calcSum(A, solution) != sum) {
                System.out.println("ERROR !!!!");
                System.exit(1);
            }

            if (NUM_IN_SUBSET != numInThisSet) {
                return; // keep looking!
            }

            System.out.println("GOT IT!!!! in " + (System.currentTimeMillis() - start) + " mseconds");
            System.exit(0);

        } else if (index < A.length) {
            solution[index] = 1;// select the element
            currSum += A[index];
            find(A, currSum, index + 1, sum, solution);
            currSum -= A[index];
            solution[index] = 0;// do not select the element
            find(A, currSum, index + 1, sum, solution);
        }
    }

    private static int dumpSolution(int[] nums, int[] sol) {
        int numInSet = 0;

        for (int i = 0; i < sol.length; i++) {
            if (sol[i] == 1) {
                ++numInSet;
            }
        }

        if (numInSet != NUM_IN_SUBSET) {
            return numInSet;
        }

        System.out.printf("%4d:    ", numInSet);

        for (int i = 0; i < sol.length; i++) {
            if (sol[i] == 1) {
                System.out.printf(" %d ", nums[i]);
            }
        }
        System.out.println("");
        return numInSet;
    }

    private static int calcSum(int[] A, int[] sol) {
        int actualSum = 0;

        for (int i = 0; i < sol.length; i++) {
            if (sol[i] == 1) {
                actualSum += A[i];
            }
        }
        return actualSum;
    }

    public static boolean subSetDP(int[] A, int sum) {
        boolean[][] solution = new boolean[A.length + 1][sum + 1];
        // if sum is not zero and subset is 0, we can't make it 
        for (int i = 1; i <= sum; i++) {
            solution[0][i] = false;
        }
        // if sum is 0 the we can make the empty subset to make sum 0
        for (int i = 0; i <= A.length; i++) {
            solution[i][0] = true;
        }
        //
        for (int i = 1; i <= A.length; i++) {
            for (int j = 1; j <= sum; j++) {
                //first copy the data from the top
                solution[i][j] = solution[i - 1][j];

                //If solution[i][j]==false check if can be made
                if (solution[i][j] == false && j >= A[i - 1]) {
                    solution[i][j] = solution[i][j] || solution[i - 1][j - A[i - 1]];
                }
            }
        }
        return solution[A.length][sum];
    }

    // A recursive solution for subset sum problem
    // Returns true if there is a subset of set[] with sum
    // equal to given sum
    static boolean isSubsetSum(int[] array, int n, int sum) {
        // Base Cases
        ++numCalls;
        System.out.printf("call# %d, n = %d, sum = %d\n", numCalls, n, sum);
//System.out.println("isSubsetSum(" + n + "), sum=" + sum);
        if (sum == 0) {
            return true;
        } else if (n == 0) { // && sum != 0) {
            return false;
        }

        // If last element is greater than sum, then ignore it
        if (array[n - 1] > sum) {
            return isSubsetSum(array, n - 1, sum);
        }

        /* else, check if sum can be obtained by any of the following
         (a) including the last element
         (b) excluding the last element   */
        //return isSubsetSum(array, n - 1, sum)  || isSubsetSum(array, n - 1, sum - array[n - 1]);
        boolean excluding = isSubsetSum(array, n - 1, sum);

        if (excluding) {
            System.out.println("");
            System.out.printf("excluding true, n = %d, sum = %d\n", n, sum);
            return true;
        }

        boolean including = isSubsetSum(array, n - 1, sum - array[n - 1]);

        if (including) {
            System.out.printf("including true, n = %d, sum = %d\n", n, sum);
        }
        return including;
    }
}
