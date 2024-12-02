import java.util.Scanner;
public class affine_cipher {
    // Function to calculate modular inverse
    public static int modInverse(int a, int modulo) {
        for (int i = 1; i < modulo; i++) {
            if ((a * i) % modulo == 1) {
                return i;
            }
        }
        throw new IllegalArgumentException("First key does not have a modular inverse with modulo " + modulo);
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
    public static String encrypt(String plaintext, int key1, int key2) {
        if (!plaintext.matches("[a-z]+")) {
            throw new IllegalArgumentException("Plaintext must only contain lowercase letters.");
        }
        if (!isCoprime(key1, 26)) {
            throw new IllegalArgumentException("First key must be coprime with 26.");
        }
        StringBuilder ciphertext = new StringBuilder();
        for (char ch : plaintext.toCharArray()) {
            int shifted = (key1 * (ch - 'a') + key2) % 26;
            ciphertext.append((char) ('A' + shifted));
        }
        return ciphertext.toString();
    }
    // Decrypt function
    public static String decrypt(String ciphertext, int key1, int key2) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        if (!isCoprime(key1, 26)) {
            throw new IllegalArgumentException("First key must be coprime with 26.");
        }
        int inverseKey1 = modInverse(key1, 26);
        StringBuilder plaintext = new StringBuilder();
        for (char ch : ciphertext.toCharArray()) {
            int shifted = ((ch - 'A' - key2) * inverseKey1) % 26;
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
        for (int key1 = 1; key1 < 26; key1++) {
            if (isCoprime(key1, 26)) {
                for (int key2 = 0; key2 < 26; key2++) {
                    try {
                        String plaintext = decrypt(ciphertext, key1, key2);
                        System.out.println("Key1 " + key1 + ", Key2 " + key2 + ": " + plaintext);
                    } catch (Exception e) {
                        // Ignore invalid keys during brute force
                    }
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
                    System.out.println("Enter first key (coprime with 26): ");
                    int key1 = scanner.nextInt();
                    System.out.println("Enter second key (0 to 25): ");
                    int key2 = scanner.nextInt();
                    if (key2 < 0 || key2 > 25) {
                        throw new IllegalArgumentException("Second key must be in the range 0 to 25.");
                    }
                    System.out.println("Encrypted text: " + encrypt(plaintext, key1, key2));
                } else if (choice == 2) { // Decrypt
                    System.out.println("Enter ciphertext (uppercase only): ");
                    String ciphertext = scanner.nextLine();
                    System.out.println("Enter first key (coprime with 26): ");
                    int key1 = scanner.nextInt();
                    System.out.println("Enter second key (0 to 25): ");
                    int key2 = scanner.nextInt();
                    if (key2 < 0 || key2 > 25) {
                        throw new IllegalArgumentException("Second key must be in the range 0 to 25.");
                    }
                    System.out.println("Decrypted text: " + decrypt(ciphertext, key1, key2));
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
