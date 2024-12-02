import java.util.Scanner;
public class shift_cipher {
    // Encrypt function
    public static String encrypt(String plaintext, int key) {
        StringBuilder ciphertext = new StringBuilder();
        for (char ch : plaintext.toCharArray()) {
            ciphertext.append((char) ('A' + (ch - 'a' + key) % 26));
        }
        return ciphertext.toString();
    }
    // Decrypt function
    public static String decrypt(String ciphertext, int key) {
        StringBuilder plaintext = new StringBuilder();
        for (char ch : ciphertext.toCharArray()) {
            plaintext.append((char) ('a' + (ch - 'A' - key + 26) % 26));
        }
        return plaintext.toString();
    }
    // Brute force function
    public static void bruteForce(String ciphertext) {
        System.out.println("Brute force results:");
        for (int key = 0; key < 26; key++) {
            System.out.println("Key " + key + ": " + decrypt(ciphertext, key));
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
                if (choice == 1) {
                    System.out.println("Enter plaintext (lowercase only): ");
                    String plaintext = scanner.nextLine();
                    if (!plaintext.matches("[a-z]+")) throw new IllegalArgumentException("Invalid plaintext.");

                    System.out.println("Enter shift key (integer): ");
                    int key = scanner.nextInt();

                    System.out.println("Encrypted text: " + encrypt(plaintext, key));

                } else if (choice == 2) {
                    System.out.println("Enter ciphertext (uppercase only): ");
                    String ciphertext = scanner.nextLine();
                    if (!ciphertext.matches("[A-Z]+")) throw new IllegalArgumentException("Invalid ciphertext.");

                    System.out.println("Enter decryption key (integer): ");
                    int key = scanner.nextInt();

                    System.out.println("Decrypted text: " + decrypt(ciphertext, key));
                } else if (choice == 3) {
                    System.out.println("Enter ciphertext (uppercase only): ");
                    String ciphertext = scanner.nextLine();
                    if (!ciphertext.matches("[A-Z]+")) throw new IllegalArgumentException("Invalid ciphertext.");
                    bruteForce(ciphertext);
                } else if (choice == 4) {
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
