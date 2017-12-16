package google;

/**
 *
 * @author wnevins
 */
public class StringReverser {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //StringReverser rev = new StringReverser("x y    d.");

        //rev.reverseEasy();

        toInt("-500");
        toInt("-00");
        toInt("500");
        toInt("12345678");


    }

    private void reverseEasy() {
        String[] words = input.split(" +");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = words.length - 1; i >= 0; i--) {
            if (first)
                first = false;
            else
                sb.append(' ');

            sb.append(words[i]);
        }
        System.out.println("INPUT: [" + input + "]");
        System.out.println("OUTPUT: [" + sb.toString() + "]");
    }

    public StringReverser(String s) {
        input = s;
    }

    private static int toInt(String s) {
        char[] chars = s.toCharArray();
        int len = chars.length;
        int multiplier = 1;
        int sum = 0;
        System.out.print(s);
        for (int i = len - 1; i > 0; --i) {
            sum += (chars[i] - '0') * multiplier;
            multiplier *= 10;
        }
        if (chars[0] == '-')
            sum *= -1;
        else
            sum += (chars[0] - '0') * multiplier;


        System.out.println(" ==>> " + sum);
        return sum;
    }
    private String input;
}
