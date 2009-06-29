package net.orfjackal.experimental;


public class FizzBuzz {

    private static final int FIZZ = 3;
    private static final int BUZZ = 5;

    public static String textForNumber(int n) {
        if (multipleOf(FIZZ * BUZZ, n)) {
            return "FizzBuzz";
        }
        if (multipleOf(FIZZ, n)) {
            return "Fizz";
        }
        if (multipleOf(BUZZ, n)) {
            return "Buzz";
        }
        return Integer.toString(n);
    }

    private static boolean multipleOf(int multiplier, int n) {
        return n % multiplier == 0;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(textForNumber(i));
        }
    }
}
