/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

/**
 *
 * @author bnevins
 */
public class Parenthesis {

    public static void main(String[] args) {
        String[] tests = new String[] {
            ")", "))))", "(((((((((()))))))))", "(((((((((())))))))))",
            "((((( "
        };
        
        for(String s : tests) {
            System.out.printf("%b ==> %s%n", ok(s), s);
        }
    }

    private static final boolean ok(String s) {
        int numLeft = 0;
        char[] cc = s.toCharArray();
        for (char c : cc) {
            switch (c) {
                case '(':
                    ++numLeft;
                    break;
                case ')':
                    if(--numLeft < 0) {
                        System.out.println("Too many right parenthesis");
                        return false;
                    }
                default:
                    break;
            }
        }
        if(numLeft > 0)
            System.out.println("Missing " + numLeft + " right parenthesis.");
        return numLeft == 0;
    }
}
