import java.math.BigInteger;
import java.security.SecureRandom;
public class ElGamal {
    private BigInteger p; // Large prime
    private BigInteger g; // Primitive root modulo p
    private BigInteger privateKey; // Private key
    private BigInteger publicKey; // Public key
    public ElGamal(int bitLength) {
        SecureRandom random = new SecureRandom();
        // Generate a large prime number p
        p = BigInteger.probablePrime(bitLength, random);
        // Generate a primitive root modulo p
        g = BigInteger.valueOf(2); // Simplified choice for g
        // Generate private key (1 < privateKey < p-1)
        privateKey = new BigInteger(bitLength - 1, random).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
        // Generate public key: publicKey = g^privateKey mod p
        publicKey = g.modPow(privateKey, p);
    }
    public BigInteger[] encrypt(BigInteger message) {
        SecureRandom random = new SecureRandom();
        // Generate random k (1 < k < p-1)
        BigInteger k = new BigInteger(p.bitLength() - 1, random).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);      
        // Calculate c1 = g^k mod p
        BigInteger c1 = g.modPow(k, p);   
        // Calculate c2 = message * (publicKey^k mod p) mod p
        BigInteger c2 = message.multiply(publicKey.modPow(k, p)).mod(p);
        return new BigInteger[]{c1, c2};
    }
    public BigInteger decrypt(BigInteger[] cipher) {
        BigInteger c1 = cipher[0];
        BigInteger c2 = cipher[1];   
        // Calculate shared secret: s = c1^privateKey mod p
        BigInteger s = c1.modPow(privateKey, p);
        // Calculate message: message = (c2 * s^-1) mod p
        BigInteger sInverse = s.modInverse(p);
        return c2.multiply(sInverse).mod(p);
    }
    public BigInteger getPublicKey() {
        return publicKey;
    }
    public BigInteger getP() {
        return p;
    }
    public BigInteger getG() {
        return g;
    }
    public static void main(String[] args) {
        // Initialize the ElGamal system with a bit length for prime number
        int bitLength = 512;
        ElGamal elGamal = new ElGamal(bitLength);
        // Display public parameters
        System.out.println("Public parameters:");
        System.out.println("Prime (p): " + elGamal.getP());
        System.out.println("Generator (g): " + elGamal.getG());
        System.out.println("Public Key: " + elGamal.getPublicKey());
        // Message to encrypt (as a number)
        BigInteger message = new BigInteger("123456789");
        System.out.println("\nOriginal Message: " + message);
        // Encrypt the message
        BigInteger[] cipher = elGamal.encrypt(message);
        System.out.println("\nEncrypted Message:");
        System.out.println("c1: " + cipher[0]);
        System.out.println("c2: " + cipher[1]);
        // Decrypt the message
        BigInteger decryptedMessage = elGamal.decrypt(cipher);
        System.out.println("\nDecrypted Message: " + decryptedMessage);
    }
}

8.          Write a program to implement Rabin Miller Primality Test to check whether given number is prime or composite.
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
