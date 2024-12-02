import java.util.Scanner;
public class RSA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to Sign Message, 2 to Verify Signature, 3 to Exit:");
        int option = sc.nextInt();
        if (option == 3) {
            System.out.println("Exiting the program.");
            sc.close();
            return;
        }
        // Step 1: Key Generation
        System.out.print("Enter a small prime number (p): ");
        int p = sc.nextInt();
        System.out.print("Enter another small prime number (q): ");
        int q = sc.nextInt();
        // Calculate n = p * q (modulus for both public and private keys)
        int n = p * q;
        // Calculate phi(n) = (p - 1) * (q - 1)
        int phi = (p - 1) * (q - 1);
        // Calculate public key e (e must be coprime with phi(n))
        int e = 3;
        while (gcd(e, phi) != 1) {
            e++;
        }
        // Calculate private key d (d is the modular inverse of e modulo phi(n))
        int d = modInverse(e, phi);
        System.out.println("Public Key (n, e): (" + n + ", " + e + ")");
        System.out.println("Private Key (n, d): (" + n + ", " + d + ")");
        if (option == 1) {
            // Alice signs the message
            System.out.println("Enter the message to sign:");
            // Take input message from the user
            sc.nextLine();  // Consume newline left by nextInt()
            String message = sc.nextLine();
            System.out.println("Signing message: '" + message + "'");
            // Step 2: Sign the message
            int signature = signMessage(message, d, n);
            System.out.println("Digital Signature: " + signature);
        } else if (option == 2) {
            // Bob verifies the signature
            System.out.print("Enter the digital signature to verify: ");
            int signature = sc.nextInt();
            // Step 3: Verify the signature
            sc.nextLine();  // Consume newline left by nextInt()
            System.out.println("Enter the message to verify:");
            String message = sc.nextLine();
            boolean isVerified = verifySignature(message, signature, e, n);
            if (isVerified) {
                System.out.println("Signature verification successful: Message is authentic.");
            } else {
                System.out.println("Signature verification failed: Message may have been tampered.");
            }
        }
        sc.close();
    }
    // Method to calculate GCD of two numbers
    public static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
    // Method to calculate modular inverse
    public static int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;
        if (m == 1)
            return 0;
        while (a > 1) {
            q = a / m;
            t = m;
            m = a % m;
            a = t;
            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }
        if (x1 < 0)
            x1 += m0;
        return x1;
    }
    // Method to sign the message (message is hashed, and then encrypted with private key)
    public static int signMessage(String message, int privateKey, int n) {
        // Hash the message (simple method: sum of ASCII values)
        int hash = 0;
        for (int i = 0; i < message.length(); i++) {
            hash += message.charAt(i);
        }
        hash = hash % n; // Take modulus with n for small hashes
        // Encrypt the hash with the private key to generate the signature
        return modExponentiation(hash, privateKey, n);
    }
    // Method to verify the signature (decrypt signature using public key and compare with hash)
    public static boolean verifySignature(String message, int signature, int publicKey, int n) {
        // Hash the message
        int hash = 0;
        for (int i = 0; i < message.length(); i++) {
            hash += message.charAt(i);
        }
        hash = hash % n; // Take modulus with n for small hashes
        // Decrypt the signature using the public key
        int decryptedHash = modExponentiation(signature, publicKey, n);
        // Compare the decrypted hash with the original hash
        return hash == decryptedHash;
    }
    // Method to calculate modular exponentiation (a^b % n)
    public static int modExponentiation(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
  }
            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }
        return result;
    }
}
