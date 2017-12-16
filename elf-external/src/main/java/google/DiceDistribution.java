package google;

/**
 * Prints the distribution sum of scores of given number of dice. Each score is
 * s.t. 1<=score<=6.
 */
import java.util.*;

class DiceDistribution {
    private static Map<Integer, Set<String>> dist = new HashMap<>();
    private static int totalHashSets = 0;

    private static void printd(int sum, String d, int rest) {
        //sum: sum so far
        //d: distribution so far, something like "14"
        //rest: number of dice remaining to be thrown
        if (rest == 0) {
            if (dist.get(sum) == null) {
                dist.put(sum, new HashSet<String>());
                ++totalHashSets;
            }
            dist.get(sum).add(d);
        }
        else {
            for (int i = 0; i <= 5; i++) {
                printd(sum - i, d + i, rest - 1);
            }
        }
    }

    public static void main(String args[]) {
        int n;
        try {
            n = Integer.valueOf(args[0]);
        }
        catch (Exception e) {
            n = 3;
        }
        int max = n * 6;
        printd(max, "", n);
        for (int sum : dist.keySet()) {
            System.out.println(sum + ": " + dist.get(sum).size());
        }
        System.out.println("Total Number of Hash Sets created: " + totalHashSets);
    }
}
