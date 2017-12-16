/*
 */
package google;

/**
 *
 * @author wnevins
 */
public class Fibonacci {
    /**
     * @param args the command line arguments
     */



    public static void main(String[] args) {
        int n =20;

        cache[0] = 0;
        cache[1] = 1;

        for(int i = 2; i <= n; i++) {
            cache[i] = cache[i-1] + cache[i-2];
        }
        System.out.println("ANSWER = " + goodRecursiveFib(n));
        System.out.println("ANSWER = " + cache[n]);

    }
    public static void main3(String[] args) {
        int i = 150;
        long start = System.nanoTime();
        long answer = 100;
        counter = 0;
        long end = System.nanoTime();
        answer = goodRecursiveFib(i);

        System.out.printf("Good recursion: %d nanoseconds, %d func calls, answer= %d\n",
                (end - start), counter, answer);
    }
/*
    public static void main2(String[] args) {
        for (int i = 0; i < 30; i++) {
            System.out.print("Recursive Fib: " + i + " -->> " + badRecursiveFib(i));
            System.out.println(" Counter:" + counter);
            counter = 0;
            cache = new int[1000];
            System.out.print("Good Recursive Fib: " + i + " -->> " + goodRecursiveFib(i));
            System.out.println(" Counter:" + counter);
            counter = 0;
        }
    }
*/
    static int badRecursiveFib(int n) {
        ++counter;
        if (n <= 1)
            return n;
        return badRecursiveFib(n - 1) + badRecursiveFib(n - 2);
    }

    static long goodRecursiveFib(long n) {
        ++counter;
        if (n <= 1)
            return n;

        if (cache[(int)n] == 0)
            cache[(int)n] = goodRecursiveFib(n - 1) + goodRecursiveFib(n - 2);

        return cache[(int)n];
    }
    static long counter;
    static long cache[] = new long[1000];
}
