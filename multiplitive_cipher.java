import java.util.Scanner;
public class multiplitive_cipher {
    // Function to calculate modular inverse
    public static int modInverse(int key, int modulo) {
        for (int i = 1; i < modulo; i++) {
            if ((key * i) % modulo == 1) {
                return i;
            }
        }
        throw new IllegalArgumentException("Key does not have a modular inverse with modulo " + modulo);
    }
    // Function to check if two numbers are coprime
    public static boolean isCoprime(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a == 1;
    }
    // Encrypt function
    public static String encrypt(String plaintext, int key) {
        if (!plaintext.matches("[a-z]+")) {
            throw new IllegalArgumentException("Plaintext must only contain lowercase letters.");
        }
        if (!isCoprime(key, 26)) {
            throw new IllegalArgumentException("Key must be coprime with 26.");
        }
        StringBuilder ciphertext = new StringBuilder();
        for (char ch : plaintext.toCharArray()) {
            int shifted = (ch - 'a') * key % 26;
            ciphertext.append((char) ('A' + shifted));
        }
        return ciphertext.toString();
    }
    // Decrypt function
    public static String decrypt(String ciphertext, int key) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        if (!isCoprime(key, 26)) {
            throw new IllegalArgumentException("Key must be coprime with 26.");
        }
        int inverseKey = modInverse(key, 26);
        StringBuilder plaintext = new StringBuilder();
        for (char ch : ciphertext.toCharArray()) {
            int shifted = (ch - 'A') * inverseKey % 26;
            if (shifted < 0) shifted += 26; // Handle negative values
            plaintext.append((char) ('a' + shifted));
        }
        return plaintext.toString();
    }
    // Brute force function
    public static void bruteForce(String ciphertext) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        System.out.println("Brute force results:");
        for (int key = 1; key < 26; key++) {
            if (isCoprime(key, 26)) {
                try {
                    String plaintext = decrypt(ciphertext, key);
                    System.out.println("Key " + key + ": " + plaintext);
                } catch (Exception e) {
                    // Ignore invalid keys during brute force
                }
            }
        }
    }
    // Main function
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Brute Force");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            try {
                if (choice == 1) { // Encrypt
                    System.out.println("Enter plaintext (lowercase only): ");
                    String plaintext = scanner.nextLine();
                    System.out.println("Enter key (coprime with 26): ");
                    int key = scanner.nextInt();
                    System.out.println("Encrypted text: " + encrypt(plaintext, key));
                } else if (choice == 2) { // Decrypt
                    System.out.println("Enter ciphertext (uppercase only): ");
                    String ciphertext = scanner.nextLine();
                    System.out.println("Enter key (coprime with 26): ");
                    int key = scanner.nextInt();
                    System.out.println("Decrypted text: " + decrypt(ciphertext, key));
                } else if (choice == 3) { // Brute force
                    System.out.println("Enter ciphertext (uppercase only): ");
                    String ciphertext = scanner.nextLine();
                    bruteForce(ciphertext);
                } else if (choice == 4) { // Exit
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
        scanner.close();
    }
}
