import java.util.Scanner;
public class generateKey {
    // Encrypt function
    public static String encrypt(String plaintext, String key) {
        if (!plaintext.matches("[a-z ]+")) {
            throw new IllegalArgumentException("Plaintext must only contain lowercase letters and spaces.");
        }
        if (!key.matches("[a-z]+")) {
            throw new IllegalArgumentException("Key must only contain lowercase letters.");
        }
        plaintext = plaintext.replaceAll(" ", ""); // Remove spaces
        StringBuilder ciphertext = new StringBuilder();
        key = generateFullKey(plaintext, key);
        for (int i = 0; i < plaintext.length(); i++) {
            char p = plaintext.charAt(i);
            char k = key.charAt(i);
            char c = (char) ((p - 'a' + k - 'a') % 26 + 'A'); // Encrypt using Vigenère formula
            ciphertext.append(c);
        }
        return ciphertext.toString();
    }
    // Decrypt function
    public static String decrypt(String ciphertext, String key) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        if (!key.matches("[a-z]+")) {
            throw new IllegalArgumentException("Key must only contain lowercase letters.");
        }
        StringBuilder plaintext = new StringBuilder();
        key = generateFullKey(ciphertext.toLowerCase(), key);
        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            char k = key.charAt(i);
            char p = (char) ((c - 'A' - (k - 'a') + 26) % 26 + 'a'); // Decrypt using Vigenère formula
            plaintext.append(p);
        }
        return plaintext.toString();
    }
    // Generate full key matching the length of plaintext
    private static String generateFullKey(String text, String key) {
        StringBuilder fullKey = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            fullKey.append(key.charAt(i % key.length())); // Repeat key
        }
        return fullKey.toString();
    }
    // Brute force attack to find key
    public static void bruteForce(String ciphertext, String knownPlaintext) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        if (!knownPlaintext.matches("[a-z]+")) {
            throw new IllegalArgumentException("Known plaintext must only contain lowercase letters.");
        }
        System.out.println("Brute force results:");
        for (int keyLength = 1; keyLength <= ciphertext.length(); keyLength++) {
            System.out.println("Trying key length: " + keyLength);
            for (int start = 0; start < keyLength; start++) {
                StringBuilder possibleKey = new StringBuilder();
                for (int i = start; i < ciphertext.length(); i += keyLength) {
                    char c = ciphertext.charAt(i);
                    char p = knownPlaintext.charAt(i);
                    char k = (char) ((c - 'A' - (p - 'a') + 26) % 26 + 'a');
                    possibleKey.append(k);
                }
                System.out.println("Possible key: " + possibleKey);
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
            if (choice == 1) { // Encrypt
                System.out.println("Enter plaintext (lowercase letters and spaces only): ");
                String plaintext = scanner.nextLine();
                System.out.println("Enter key (lowercase letters only): ");
                String key = scanner.nextLine();
                try {
                    String ciphertext = encrypt(plaintext, key);
                    System.out.println("Encrypted text: " + ciphertext);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (choice == 2) { // Decrypt
                System.out.println("Enter ciphertext (uppercase letters only): ");
                String ciphertext = scanner.nextLine();
                System.out.println("Enter key (lowercase letters only): ");
                String key = scanner.nextLine();
                try {
                    String plaintext = decrypt(ciphertext, key);
                    System.out.println("Decrypted text: " + plaintext);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (choice == 3) { // Brute Force
                System.out.println("Enter ciphertext (uppercase letters only): ");
                String ciphertext = scanner.nextLine();
                System.out.println("Enter known plaintext (lowercase letters only): ");
                String knownPlaintext = scanner.nextLine();
                try {
                    bruteForce(ciphertext, knownPlaintext);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (choice == 4) { // Exit
                System.out.println("Exiting the program. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }
}
