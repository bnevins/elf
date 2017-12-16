package com.elf.practice;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Problem Statement: On a positive integer, you can perform any one of the
 * following 3 steps. 1.) Subtract 1 from it. ( n = n - 1 ) , 2.) If its
 * divisible by 2, divide by 2. ( if n % 2 == 0 , then n = n / 2 ) , 3.) If its
 * divisible by 3, divide by 3. ( if n % 3 == 0 , then n = n / 3 ). Now the
 * question is, given a positive integer n, find the minimum number of steps
 * that takes n to 1 eg: 1.)For n = 1 , output: 0 2.) For n = 4 , output: 2 ( 4
 * /2 = 2 /2 = 1 ) 3.) For n = 7 , output: 3 ( 7 -1 = 6 /3 = 2 /2 = 1 )
 *
 * @author Byron Nevins
 */
public class MinStepsToOne {
    
    // Returns true if there is a subset of set[] with sum
        // equal to given sum
    static boolean isSubsetSum(int set[], int n, int sum)
    {
        System.out.printf("Sum: %d, N: %d\n", sum, n);
       // Base Cases
       if (sum == 0)
         return true;
       if (n == 0 && sum != 0)
         return false;
      
       // If last element is greater than sum, then ignore it
       if (set[n-1] > sum)
         return isSubsetSum(set, n-1, sum);
      
       /* else, check if sum can be obtained by any of the following
          (a) including the last element
          (b) excluding the last element   */
       return isSubsetSum(set, n-1, sum) || 
                                   isSubsetSum(set, n-1, sum-set[n-1]);
    }
    /* Driver program to test above function */
    public static void main (String args[])
    {
          int set[] = {3, 34, 4, 12, 5, 2};
          int sum = 13;
          int n = set.length;
          if (isSubsetSum(set, n, sum) == true)
             System.out.println("Found a subset with given sum");
          else
             System.out.println("No subset with given sum");
    }
/* 

    
    private static int getMinSteps(int n) {
        System.out.printf("getMinSteps(%d)\n", n);
        if (n == 1) {
            return 0;
        }

        if (n >= 0 && memo[n] != -1) {
            return memo[n];
        }

        int r = getMinSteps(n - 1);  // -1 step

        if (r % 2 == 0) {
            r = Math.min(r, 1 + getMinSteps(n / 2));
        }

        if (r % 3 == 0) {
            r = Math.min(r, 1 + getMinSteps(n / 3));
        }
        memo[n] = r;
        return r;
    }
*/
    private static void usage() {
        System.out.println("USAGE:  java com.elf.practice.WordMutatorRunner <number of letters in the words>");
    }
}
