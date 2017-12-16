package google;

import java.io.File;

/**
 *
 * @author bnevins
 */
public class Dice {
    private static int results[];
    private static int numDice;
    private static boolean debug = false;
    private static long numRolls = 0;
    private static long maxValue = 1;
    private static final String HISTOGRAM;
    private static int HISTOGRAM_LENGTH = 146;

    static {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < HISTOGRAM_LENGTH; i++) {
            sb.append('X');
        }

        HISTOGRAM = sb.toString();
    }

    public static void main(String[] args) {
        File[] ff = File.listRoots();
        for(File f : ff) {
            System.out.println(f);
        }
        
        try {
            numDice = Integer.valueOf(args[0]);
        }
        catch (Exception e) {
            numDice = 4;
        }
        results = new int[numDice * 6 + 1];
        //recurseDice();
        iterativeDice();
        print();
    }

    private static void print() {
        for (int i = numDice; i <= numDice * 6; i++) {
            StringBuilder sb = new StringBuilder();
            int histoLength = (int) ((results[i] * HISTOGRAM_LENGTH) / maxValue);
            sb.append(HISTOGRAM, 0, histoLength);
            System.out.println(sb.toString());
        }
        System.out.println("Total Rolls: " + numRolls);
        System.out.println("Max Value: " + maxValue);
    }

    private static void recurseDice() {
        recurseDice(numDice, 0);
    }

    private static void checkMax(int num) {
        if (num > maxValue) {
            maxValue = num;
        }
    }

    private static void recurseDice(int thisDie, int sumSoFar) {
        if (thisDie == 0) {
            ++results[sumSoFar];
            ++numRolls;

            // remember the largest...
            checkMax(results[sumSoFar]);

            if (debug) {
                System.out.printf("%d", numRolls);
            }
        }
        else {
            for (int i = 1; i <= 6; i++) {
                recurseDice(thisDie - 1, sumSoFar + i);
            }
        }
    }

    // does not work
    private static void iterativeDice() {
        //for (int i = 0; i < numDice; i++) {
            for (int d1 = 1; d1 <= 6; ++d1) {
                for (int d2 = 1; d2 <= 6; ++d2) {
                    for (int d3 = 1; d3 <= 6; ++d3) {
                        for (int d4 = 1; d4 <= 6; ++d4) {
                            for (int d5 = 1; d5 <= 6; ++d5) {
                                for (int d6 = 1; d6 <= 6; ++d6) {
                                    //++results[d1 + d2 + d3 + d4 + d5 + d6];
                                  int sum = d1 + d2 + d3 + d4 + d5 + d6;
                                    System.out.println("SUM: " + sum);
                                }
                            }
                        }
                    }
                }
            }
        //}
        System.out.println("OUTER");
    }
}
