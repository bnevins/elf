/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elf.practice;

import java.util.Arrays;

/**
 *
 * @author bnevins
 */
public class PipeCutting {

    private final static int price[] = {1, 5, 8, 9, 10, 17, 17, 20, 24, 30, 32, 36, 39, 44, 47, 52, 55, 60, 63, 66, 70, 73, 76, 79, 82, 87, 91, 95, 98, 101, 106, 111, 115, 119};
    private static LengthPrice priceTable[] = new LengthPrice[price.length];
    private final static int numCalls[] = new int[price.length];
    private final static int memo[] = new int[price.length];

    private static int numRecursive = 0;

    static {
        Arrays.fill(memo, 0); // redundant
        priceTable[0] = new LengthPrice(0, 0);
        for (int i = 1; i < price.length; i++) {
            priceTable[i] = new LengthPrice(i, price[i - 1]);
        }
        Arrays.fill(numCalls, 0);

    }

    public static void main(String[] args) {
        //for (int i = 0; i < 32; i++) {

        doPlainRecursive(price.length - 1);

        Arrays.fill(numCalls, 0);
        numRecursive = 0;
        doMemoizedRecursive(price.length - 1);

        numRecursive = 0;
        doBottomUp(price.length - 1);
    }

    public static void doPlainRecursive(int num) {
        //System.out.println("cutRod finally returned: " + (cutRod(47)));
        //System.out.println("cutRod finally returned: " + (cutRod(8)));
        long start = System.currentTimeMillis();
        System.out.print("cutRod finally returned: " + (cutRod(num)) + " for num= " + num);
        System.out.println(",  Time: " + (System.currentTimeMillis() - start) + " msec");
        System.out.println(Arrays.toString(numCalls));
    }

    public static void doBottomUp(int num) {
        long start = System.currentTimeMillis();
        System.out.print("cutRodBottomUp eventually returned: " + (cutRodBottomUp(num)) + " for num= " + num);
        System.out.print(",  number of recursive calls: " + numRecursive);
        System.out.println(",  Time: " + (System.currentTimeMillis() - start) + " msec");
        System.out.println(Arrays.toString(numCalls));
    }

    public static void doMemoizedRecursive(int num) {
        //System.out.println("cutRod finally returned: " + (cutRod(47)));
        //System.out.println("cutRod finally returned: " + (cutRod(8)));
        long start = System.nanoTime();
        System.out.print("cutRod finally returned: " + (cutRodMemoized(num)) + " for num= " + num);
        System.out.print(",  number of recursive calls: " + numRecursive);
        System.out.println(",  Time: " + ((System.nanoTime() - start) / 1000) + " usec");
        //System.out.println("cutRod finally returned: " + (cutRod(10)));
        System.out.println(Arrays.toString(numCalls));
    }

    /**
     */
    public static int cutRodBottomUp(int length) {
        int numCompare = 0;
        int[] revenue = new int[length + 1]; // one-based
        int[] bestFirstCut = new int[length + 1]; // one-based
        revenue[0] = 0;
        bestFirstCut[0] = 0;

        for (int currentPieceLength = 1; currentPieceLength <= length; currentPieceLength++) {
            int bestSoFar = 0;

            for (int firstCutLength = 1; firstCutLength <= currentPieceLength; firstCutLength++) {
                int firstCutPrice = getPrice(firstCutLength);
                int leftoverInches = currentPieceLength - firstCutLength;

                int currentRevenue = firstCutPrice + revenue[leftoverInches];

                ++numCompare;
                if (bestSoFar < currentRevenue) {
                    bestSoFar = currentRevenue;
                    bestFirstCut[currentPieceLength] = firstCutLength;
                }
            }
            ++numCompare;
            if (bestSoFar >= 0) {
                revenue[currentPieceLength] = bestSoFar;
            } else {
                revenue[currentPieceLength] = 0;
            }
            System.out.printf("Set revenue[%d] to %d\n", currentPieceLength, bestSoFar);
        }
        System.out.println("Revenue is ==> " + Arrays.toString(revenue));
        System.out.println("Best first cut is ==> " + Arrays.toString(bestFirstCut));
        System.out.println("Number of comparisons: " + numCompare);
        return revenue[length];
    }

    public static int cutRod(int length) {

        if (length == 0) {
            return 0;
        }

        ++numCalls[length - 1];

        //System.out.printf("Before Loop --cutRod(%d)\n", length);
        int q = Integer.MIN_VALUE;
        ++numRecursive;

        for (int i = 1; i <= length; i++) {
            q = Math.max(q, price[i - 1] + cutRod(length - i));
            //System.out.printf(" In Loop -- price:  %d, length = %d, i = %d, q = %d\n", price[i-1], length, i, q);
        }
        return q == Integer.MIN_VALUE ? 0 : q;
    }

    private static int cutRodMemoized(int length) {
        if (length == 0) {
            return 0;
        }

        if (memo[length - 1] > 0) {
            return memo[length - 1];
        }

        ++numCalls[length - 1];

        //System.out.printf("Before Loop --cutRod(%d)\n", length);
        int q = Integer.MIN_VALUE;
        ++numRecursive;

        for (int i = 1; i <= length; i++) {
            q = Math.max(q, price[i - 1] + cutRodMemoized(length - i));
            //System.out.printf(" In Loop -- price:  %d, length = %d, i = %d, q = %d\n", price[i-1], length, i, q);
        }

        q = (q == Integer.MIN_VALUE) ? 0 : q;
        memo[length - 1] = q;
        System.out.println("MEMO => " + Arrays.toString(memo));
        return q;
    }

    static int getPrice(int len) {
        for (LengthPrice lp : priceTable) {
            if (lp.length == len) {
                return lp.price;
            }
        }
        throw new RuntimeException("error");
    }

    private static class LengthPrice {

        int length, price;

        public LengthPrice(int length, int price) {
            this.length = length;
            this.price = price;
        }
        // would not use this in the final!!
    }
}
