package google;

/**
 * (1) method that matches parenthesis
 * (2) Contrast/Compare pros and cons of having data spread around geographically
 *   versus centralized
 * (3)10,000 machines each running 1 service.  Need to collect & display monitoring
 *   data
 * @author wnevins
 */
public class Interview1 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkParenOutput(")");
        checkParenOutput("(");
        checkParenOutput("(()");
        checkParenOutput("()()()");
        checkParenOutput("()()())");
        checkParenOutput("(((((((((()))((((())))))))))))");
    }

    public static void checkParenOutput(String s) {
        System.out.println(s + " ==>> " + checkParen(s));
    }


    // Write a routine that determines
    // if a mathematical expression has properly formatted parentheses.
    public static boolean checkParen(String s) {

        char[] chars = s.toCharArray();
        int num_left = 0;

        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '(':
                    num_left++;
                    break;
                case ')':
                    if (--num_left < 0)
                        return false;
                    break;
                default:
                    break;
            }
        }
        return num_left == 0;
    }
}
