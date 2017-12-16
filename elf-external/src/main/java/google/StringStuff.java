/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import java.util.Arrays;

/**
 *
 * @author bnevins
 */
public class StringStuff {

    public static void main(String[] args) {
        String s1 = "total";
        String s2 = "aaabcdefbcdefbcdefAgA";
        System.out.printf("A = %d, a = %d%n", (int) 'A', (int) 'a');
        System.out.printf("First non-repeat in [%s] is %c%n", s1, findFirstNoRepeated(s1));
        System.out.printf("First non-repeat in [%s] is %c%n", s2, findFirstNoRepeated(s2));
    }

    private static Character findFirstNoRepeated(String s) {
        char[] cc = s.toCharArray();
        int[] count = new int[128];
        //Arrays.fill(ascii, 0);

        // first pass -- create data structure
        for (char c : cc) {
            int offset = (int)c;// - (int)'A';
            ++count[c];
        }
        // second pass find the FIRST one that == 1
        for (char c : cc) {
            if (count[c] == 1) {
                return c;
            }
        }
        return null;
    }
}
