import java.util.Scanner;
public class digital_Signature {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. ElGamal Digital Signature");
            System.out.println("2. Encryption");
            System.out.println("3. Decryption");
            System.out.println("4. Exit");
            int choice = 0;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine(); // Consume the invalid input
                continue;
            }
            switch (choice) {
                case 1:
                    // ElGamal Digital Signature
                    elGamalDigitalSignature(sc);
                    break;
                case 2:
                    // Encryption
                    encryption(sc);
                    break;
                case 3:
                    // Decryption
                    decryption(sc);
                    break;
                case 4:
                    // Exit the program
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid input. Please choose a valid option.");
            }
        }
        sc.close();
    }
    public static void elGamalDigitalSignature(Scanner sc) {
        System.out.println("\n--- ElGamal Digital Signature ---");
        // Step 1: Key Generation
        int p = getValidPrime(sc);
        int g = getValidPrimitiveRoot(sc, p);
        // Private key x
        int x = getValidPrivateKey(sc, p);
        // Public key y = g^x % p
        int y = modExponentiation(g, x, p);
        System.out.println("Public Key (y): " + y);
        System.out.println("Private Key (x): " + x);
        // Step 2: Sign the message
        System.out.println("Enter the message to sign:");
        String message = sc.nextLine();
        int messageHash = message.hashCode() % p;  // Simplified hash
        System.out.println("Message Hash (H(m)): " + messageHash);
        // Choose a random number k
        int k = getValidK(sc, p);
        // Compute r = g^k % p
        int r = modExponentiation(g, k, p);
        // Compute the modular inverse of k
        int kInverse = modInverse(k, p - 1);
        // Compute s = k^(-1) * (H(m) - x * r) % (p-1)
        int s = (kInverse * (messageHash - x * r % (p - 1))) % (p - 1);
        // Step 3: Display the signature
        System.out.println("Signature: (r, s) = (" + r + ", " + s + ")");
        // Step 4: Verify the signature
        verifySignature(sc, r, s, messageHash, p, g, y);
    }
    public static void encryption(Scanner sc) {
        System.out.println("\n--- Encryption ---");
        sc.nextLine(); // Consume the newline character after choosing the option
        System.out.print("Enter the message to encrypt: ");
        String message = sc.nextLine();
        System.out.print("Enter a shift value for encryption: ");
        int shift = sc.nextInt();
        String encryptedMessage = caesarCipherEncrypt(message, shift);
        System.out.println("Encrypted message: " + encryptedMessage);
    }
    public static void decryption(Scanner sc) {
        System.out.println("\n--- Decryption ---");
        sc.nextLine(); // Consume the newline character after choosing the option
        System.out.print("Enter the message to decrypt: ");
        String message = sc.nextLine();
        System.out.print("Enter the shift value used for encryption: ");
        int shift = sc.nextInt();
        String decryptedMessage = caesarCipherDecrypt(message, shift);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
    public static String caesarCipherEncrypt(String message, int shift) {
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c + shift);
                encryptedMessage.append(shiftedChar);
            } else {
                encryptedMessage.append(c);
            }
        }
        return encryptedMessage.toString();
    }
    public static String caesarCipherDecrypt(String message, int shift) {
        StringBuilder decryptedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (Character.isLetter(c)) {
                char shiftedChar = (char) (c - shift);
                decryptedMessage.append(shiftedChar);
            } else {
                decryptedMessage.append(c);
            }
        }
        return decryptedMessage.toString();
    }
    // Method to calculate modular exponentiation (a^b % m)
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
    // Method to calculate modular inverse (a^-1 mod m)
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
    // Method to verify the signature
    public static void verifySignature(Scanner sc, int r, int s, int messageHash, int p, int g, int y) {
        System.out.println("\nVerifying the Signature...");
        System.out.print("Enter the signature (r): ");
        int rVerify = sc.nextInt();
        System.out.print("Enter the signature (s): ");
        int sVerify = sc.nextInt();
        boolean isVerified = verifySignature(rVerify, sVerify, messageHash, p, g, y);
        if (isVerified) {
            System.out.println("Signature verified successfully: The message is authentic.");
        } else {
            System.out.println("Signature verification failed: The message may have been tampered.");
        }
    }
    // Method to verify the signature
    public static boolean verifySignature(int r, int s, int messageHash, int p, int g, int y) {
        // Check if r and s are in the valid range
        if (r <= 0 || r >= p || s <= 0 || s >= p) {
            return false;
        }
        // Compute v1 = g^H(m) % p
        int v1 = modExponentiation(g, messageHash, p);
        // Compute v2 = y^r * r^s % p
        int v2 = (modExponentiation(y, r, p) * modExponentiation(r, s, p)) % p;
        // If v1 == v2, the signature is valid
        return v1 == v2;
    }
    // Helper methods to get valid inputs
    public static int getValidPrime(Scanner sc) {
        int p;
        while (true) {
            System.out.print("Enter a large prime number p: ");
            p = sc.nextInt();
            if (p > 2) {
                break;
            }
            System.out.println("Invalid prime number. Please enter a number greater than 2.");
        }
        return p;
    }
    public static int getValidPrimitiveRoot(Scanner sc, int p) {
        int g;
        while (true) {
            System.out.print("Enter a primitive root g modulo p: ");
            g = sc.nextInt();
            if (g > 1 && g < p) {
                break;
            }
            System.out.println("Invalid primitive root. It should be between 1 and " + (p - 1));
        }
        return g;
    }
    public static int getValidPrivateKey(Scanner sc, int p) {
        int x;
        while (true) {
            System.out.print("Enter the private key x (1 < x < p-1): ");
            x = sc.nextInt();
            if (x > 1 && x < p - 1) {
                break;
            }
            System.out.println("Invalid input: private key (x) should be between 1 and p-1.");
        }
        return x;
    }
    public static int getValidK(Scanner sc, int p) {
        int k;
        while (true) {
            System.out.print("Enter a random number k (1 < k < p-1): ");
            k = sc.nextInt();
            if (k > 1 && k < p - 1) {
                break;
            }
            System.out.println("Invalid input: k should be between 1 and p-1.");
        }
        return k;
    }
}
