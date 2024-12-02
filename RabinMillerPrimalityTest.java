import java.util.Scanner;
import java.util.Random;
public class RabinMillerPrimalityTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Input number to test
        System.out.print("Enter a number to check if it is prime: ");
        long number = scanner.nextLong();
        // Input number of iterations for the test
        System.out.print("Enter the number of iterations for the test: ");
        int iterations = scanner.nextInt();
        // Check primality
        if (isPrime(number, iterations)) {
            System.out.println(number + " is probably prime.");
        } else {
            System.out.println(number + " is composite.");
        }
    }
    // Rabin-Miller Primality Test
    public static boolean isPrime(long n, int k) {
        if (n <= 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0) return false;

        // Write n as (2^s * d) + 1 with d odd
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        // Perform k iterations of the test
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            long a = 2 + random.nextLong() % (n - 4); // Random number in range [2, n-2]
            if (!millerTest(a, d, s, n)) {
                return false; // Composite
            }
        }
        return true; // Probably prime
    }
    // Miller Test
    private static boolean millerTest(long a, long d, int s, long n) {
        // Compute a^d % n
        long x = modularExponentiation(a, d, n);
        if (x == 1 || x == n - 1) return true;
        // Keep squaring x while it doesn't become n-1
        for (int r = 1; r < s; r++) {
            x = (x * x) % n;
            if (x == n - 1) return true;
        }
        return false; // Composite
    }
    // Modular exponentiation: (base^exp) % mod
    private static long modularExponentiation(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) { // If exp is odd
                result = (result * base) % mod;
            }
            exp = exp >> 1; // Divide exp by 2
            base = (base * base) % mod;
        }
        return result;
    }
}
