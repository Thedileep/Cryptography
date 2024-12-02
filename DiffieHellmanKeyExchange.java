import java.math.BigInteger;
import java.util.Scanner;
public class DiffieHellmanKeyExchange {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Step 1: Public parameters (shared between User A and User B)
        System.out.println("Diffie-Hellman Key Exchange");
        System.out.print("Enter a large prime number (p): ");
        BigInteger p = scanner.nextBigInteger(); // Prime number
        System.out.print("Enter a primitive root modulo p (g): ");
        BigInteger g = scanner.nextBigInteger(); // Primitive root modulo p
        // Step 2: Private keys for User A and User B
        System.out.print("User A, enter your private key (a): ");
        BigInteger a = scanner.nextBigInteger();
        System.out.print("User B, enter your private key (b): ");
        BigInteger b = scanner.nextBigInteger();
        // Step 3: Compute public keys
        BigInteger A = g.modPow(a, p); // A = g^a mod p
        BigInteger B = g.modPow(b, p); // B = g^b mod p
        System.out.println("\nPublic keys exchanged:");
        System.out.println("User A's public key: " + A);
        System.out.println("User B's public key: " + B);
        // Step 4: Compute shared secret key
        BigInteger sharedKeyA = B.modPow(a, p); // (B^a) mod p
        BigInteger sharedKeyB = A.modPow(b, p); // (A^b) mod p
        System.out.println("\nShared secret key (computed by both users): " + sharedKeyA);
        if (!sharedKeyA.equals(sharedKeyB)) {
            System.out.println("Error: Shared keys do not match!");
            return;
        }
        // Step 5: Use the shared key for encryption and decryption
        BigInteger sharedKey = sharedKeyA; // Use the agreed shared key
        scanner.nextLine(); // Consume newline
        System.out.print("\nEnter a plaintext message to encrypt: ");
        String plaintext = scanner.nextLine();
        // Encrypt the message
        String encryptedMessage = encrypt(plaintext, sharedKey);
        System.out.println("Encrypted message: " + encryptedMessage);
        // Decrypt the message
        String decryptedMessage = decrypt(encryptedMessage, sharedKey);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
    // Encryption using shared key
    public static String encrypt(String plaintext, BigInteger key) {
        StringBuilder encrypted = new StringBuilder();
        int shift = key.intValue() % 26; // Use the key as a shift for simplicity
        for (char ch : plaintext.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                encrypted.append((char) ('a' + (ch - 'a' + shift) % 26));
            } else if (Character.isUpperCase(ch)) {
                encrypted.append((char) ('A' + (ch - 'A' + shift) % 26));
            } else {
                encrypted.append(ch); // Non-alphabetic characters remain unchanged
            }
        }
        return encrypted.toString();
    }
    // Decryption using shared key
    public static String decrypt(String ciphertext, BigInteger key) {
        StringBuilder decrypted = new StringBuilder();
        int shift = key.intValue() % 26; // Use the key as a shift for simplicity
        for (char ch : ciphertext.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                decrypted.append((char) ('a' + (ch - 'a' - shift + 26) % 26));
            } else if (Character.isUpperCase(ch)) {
                decrypted.append((char) ('A' + (ch - 'A' - shift + 26) % 26));
            } else {
                decrypted.append(ch); // Non-alphabetic characters remain unchanged
            }
        }
        return decrypted.toString();
    }
}
