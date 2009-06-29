package net.orfjackal.experimental;


public class FizzBuzz {

    private static final int FIZZ = 3;
    private static final int BUZZ = 5;

    public static String textForNumber(int n) {
        if (n % (FIZZ * BUZZ) == 0) {
            return "FizzBuzz";
        }
        if (n % FIZZ == 0) {
            return "Fizz";
        }
        if (n % BUZZ == 0) {
            return "Buzz";
        }
        return Integer.toString(n);
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            System.out.println(textForNumber(i));
        }
    }
}
