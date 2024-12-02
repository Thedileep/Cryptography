import java.util.*;
public class playfair_cipher {
    private static char[][] keyMatrix = new char[5][5];
    // Generate the key matrix
    private static void generateKeyMatrix(String keyword) {
        boolean[] used = new boolean[26];
        used['j' - 'a'] = true; // Treat 'j' as 'i'
        keyword = keyword.toLowerCase().replaceAll("j", "i");
        int row = 0, col = 0;
        for (char ch : keyword.toCharArray()) {
            if (!used[ch - 'a']) {
                keyMatrix[row][col++] = ch;
                used[ch - 'a'] = true;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (!used[ch - 'a']) {
                keyMatrix[row][col++] = ch;
                used[ch - 'a'] = true;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
        }
    }
    // Find the position of a character in the key matrix
    private static int[] findPosition(char ch) {
        if (ch == 'j') ch = 'i'; // Treat 'j' as 'i'
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (keyMatrix[row][col] == ch) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }
    // Encrypt plaintext
    private static String encrypt(String plaintext, String keyword) {
        if (!plaintext.matches("[a-z]+")) {
            throw new IllegalArgumentException("Plaintext must only contain lowercase letters.");
        }
        if (!keyword.matches("[a-z]+")) {
            throw new IllegalArgumentException("Keyword must only contain lowercase letters.");
        }
        plaintext = plaintext.replaceAll("j", "i");
        StringBuilder cleanedText = new StringBuilder();
        // Add 'x' between repeated letters and at the end if odd length
        for (int i = 0; i < plaintext.length(); i++) {
            cleanedText.append(plaintext.charAt(i));
            if (i < plaintext.length() - 1 && plaintext.charAt(i) == plaintext.charAt(i + 1)) {
                cleanedText.append('x');
            }
        }
        if (cleanedText.length() % 2 != 0) {
            cleanedText.append('x');
        }
        generateKeyMatrix(keyword);
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < cleanedText.length(); i += 2) {
            char first = cleanedText.charAt(i);
            char second = cleanedText.charAt(i + 1);
            int[] pos1 = findPosition(first);
            int[] pos2 = findPosition(second);
            if (pos1[0] == pos2[0]) { // Same row
                ciphertext.append(keyMatrix[pos1[0]][(pos1[1] + 1) % 5]);
                ciphertext.append(keyMatrix[pos2[0]][(pos2[1] + 1) % 5]);
            } else if (pos1[1] == pos2[1]) { // Same column
                ciphertext.append(keyMatrix[(pos1[0] + 1) % 5][pos1[1]]);
                ciphertext.append(keyMatrix[(pos2[0] + 1) % 5][pos2[1]]);
            } else { // Rectangle
                ciphertext.append(keyMatrix[pos1[0]][pos2[1]]);
                ciphertext.append(keyMatrix[pos2[0]][pos1[1]]);
            }
        }
        return ciphertext.toString().toUpperCase();
    }
    // Decrypt ciphertext
    private static String decrypt(String ciphertext, String keyword) {
        if (!ciphertext.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Ciphertext must only contain uppercase letters.");
        }
        if (!keyword.matches("[a-z]+")) {
            throw new IllegalArgumentException("Keyword must only contain lowercase letters.");
        }
        ciphertext = ciphertext.toLowerCase();
        generateKeyMatrix(keyword);
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char first = ciphertext.charAt(i);
            char second = ciphertext.charAt(i + 1);
            int[] pos1 = findPosition(first);
            int[] pos2 = findPosition(second);
            if (pos1[0] == pos2[0]) { // Same row
                plaintext.append(keyMatrix[pos1[0]][(pos1[1] + 4) % 5]);
                plaintext.append(keyMatrix[pos2[0]][(pos2[1] + 4) % 5]);
            } else if (pos1[1] == pos2[1]) { // Same column
                plaintext.append(keyMatrix[(pos1[0] + 4) % 5][pos1[1]]);
                plaintext.append(keyMatrix[(pos2[0] + 4) % 5][pos2[1]]);
            } else { // Rectangle
                plaintext.append(keyMatrix[pos1[0]][pos2[1]]);
                plaintext.append(keyMatrix[pos2[0]][pos1[1]]);
            }
        }
        return plaintext.toString().replaceAll("x$", ""); // Remove filler
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice == 1) { // Encryption
                System.out.println("Enter plaintext (lowercase only): ");
                String plaintext = scanner.nextLine();
                System.out.println("Enter keyword (lowercase only): ");
                String keyword = scanner.nextLine();
                try {
                    String ciphertext = encrypt(plaintext, keyword);
                    System.out.println("Encrypted text: " + ciphertext);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (choice == 2) { // Decryption
                System.out.println("Enter ciphertext (uppercase only): ");
                String ciphertext = scanner.nextLine();
                System.out.println("Enter keyword (lowercase only): ");
                String keyword = scanner.nextLine();
                try {
                    String plaintext = decrypt(ciphertext, keyword);
                    System.out.println("Decrypted text: " + plaintext);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else if (choice == 3) { // Exit
                System.out.println("Exiting...");
                break;

            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}
