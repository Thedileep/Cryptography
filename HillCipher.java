import java.util.Scanner;
public class HillCipher {
    // Function to perform modular arithmetic
    private static int mod26(int x) {
        return (x % 26 + 26) % 26;
    }
    // Function to find the determinant of a 2x2 matrix
    private static int findDeterminant(int[][] matrix) {
        return mod26(matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
    }
    // Function to find modular multiplicative inverse of a number under modulo 26
    private static int modInverse(int a) {
        for (int i = 1; i < 26; i++) {
            if ((a * i) % 26 == 1) {
                return i;
            }
        }
        return -1; // No modular inverse exists
    }
    // Function to find the inverse of a 2x2 matrix
    private static int[][] findInverseMatrix(int[][] matrix) {
        int determinant = findDeterminant(matrix);
        int determinantInverse = modInverse(determinant);
        if (determinantInverse == -1) {
            throw new IllegalArgumentException("Matrix is not invertible.");
        }
        // Calculate adjugate matrix and multiply by determinant's modular inverse
        int[][] inverse = new int[2][2];
        inverse[0][0] = mod26(determinantInverse * matrix[1][1]);
        inverse[0][1] = mod26(determinantInverse * -matrix[0][1]);
        inverse[1][0] = mod26(determinantInverse * -matrix[1][0]);
        inverse[1][1] = mod26(determinantInverse * matrix[0][0]);
        return inverse;
    }
    // Function to encrypt a plaintext using the Hill Cipher
    private static String encrypt(String plaintext, int[][] keyMatrix) {
        plaintext = plaintext.toLowerCase().replaceAll("[^a-z]", "");
        if (plaintext.length() % 2 != 0) {
            plaintext += "x"; // Add padding if necessary
        }
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            int[] vector = {plaintext.charAt(i) - 'a', plaintext.charAt(i + 1) - 'a'};
            int[] encryptedVector = new int[2];
            for (int row = 0; row < 2; row++) {
                encryptedVector[row] = mod26(keyMatrix[row][0] * vector[0] + keyMatrix[row][1] * vector[1]);
            }
            ciphertext.append((char) (encryptedVector[0] + 'A'));
            ciphertext.append((char) (encryptedVector[1] + 'A'));
        }
        return ciphertext.toString();
    }
    // Function to decrypt a ciphertext using the Hill Cipher
    private static String decrypt(String ciphertext, int[][] inverseKeyMatrix) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            int[] vector = {ciphertext.charAt(i) - 'A', ciphertext.charAt(i + 1) - 'A'};
            int[] decryptedVector = new int[2];
            for (int row = 0; row < 2; row++) {
                decryptedVector[row] = mod26(inverseKeyMatrix[row][0] * vector[0] + inverseKeyMatrix[row][1] * vector[1]);
            }
            plaintext.append((char) (decryptedVector[0] + 'a'));
            plaintext.append((char) (decryptedVector[1] + 'a'));
        }
        return plaintext.toString();
    }
    // Utility function to display a matrix
    private static void displayMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a 2x2 key matrix (row-wise): ");
        int[][] keyMatrix = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                keyMatrix[i][j] = scanner.nextInt();
            }
        }
        System.out.println("Key Matrix:");
        displayMatrix(keyMatrix);

        int[][] inverseKeyMatrix = null;
        try {
            inverseKeyMatrix = findInverseMatrix(keyMatrix);
            System.out.println("Inverse Key Matrix:");
            displayMatrix(inverseKeyMatrix);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        scanner.nextLine(); // Consume the newline
        System.out.print("Enter plaintext (lowercase letters only): ");
        String plaintext = scanner.nextLine();
        String ciphertext = encrypt(plaintext, keyMatrix);
        System.out.println("Ciphertext: " + ciphertext);
        String decryptedText = decrypt(ciphertext, inverseKeyMatrix);
        System.out.println("Decrypted Text: " + decryptedText);
        scanner.close();
    }
}
