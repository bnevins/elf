package com.elf.practice;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Byron Nevins
 */
public class WordMutatorRunner {

    public static void main(String[] args) {
        int numChars;

        if (args.length == 0) {
            numChars = 6;
        } else {
            numChars = Integer.parseInt(args[0]);
        }

        List<String> subset = Words.getSubset(numChars);
        int len = subset.size();
        Random random = new Random(len);
        System.out.printf("There is a subset of %d words.\n", len);
        int numPaths = 0;
        while (numPaths < 100) {
            Words.clear();
            String src = subset.get(random.nextInt(len));
            String target = subset.get(random.nextInt(len));

            if (src.equals(target)) {
                continue;
            }

            WordMutator wm = new WordMutator(src, target);
            wm.mutate();

            if (wm.wasSuccessful()) {
                numPaths++;
            }
            //else
              //  System.err.printf("%s -> %s NO SOLUTION\n", src, target);

        }
    }

    private static void usage() {
        System.out.println("USAGE:  java com.elf.practice.WordMutatorRunner <number of letters in the words>");
    }
}
