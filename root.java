import java.util.*;
public class root {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to Encrypt, 2 to Decrypt, 3 to Exit:");
        int number = sc.nextInt();
        if (number == 3) {
            System.out.println("Exiting the program.");
            sc.close();
            return;
        }
        System.out.print("Enter a prime number (q): ");
        int q = sc.nextInt();

        System.out.print("Enter a primitive root of q (pri): ");
        int pri = sc.nextInt();
        int xa;
        while (true) {
            System.out.print("Enter private key (xa), which should be less than " + (q - 1) + ": ");
            xa = sc.nextInt();
            if (xa < q - 1) {
                break;
            }
            System.out.println("Invalid input: private key (xa) should be less than q - 1.");
        }
        int ya = 1;
        for (int i = 0; i < xa; i++) {
            ya = (ya * pri) % q;
        }
        System.out.println("Public key (ya): " + ya);
        if (number == 1) {
            int message;
            while (true) {
                System.out.print("Enter a message to encrypt (as a number less than " + q + "): ");
                message = sc.nextInt();
                if (message < q) {
                    break;
                }
                System.out.println("Invalid input: message should be less than q.");
            }
            Random rand = new Random();
            int k = rand.nextInt(q - 1) + 1;
            System.out.println("Random number less than q (k): " + k);
            int c1 = 1;
            for (int i = 0; i < k; i++) {
                c1 = (c1 * pri) % q;
            }
            int K = 1;
            for (int i = 0; i < k; i++) {
                K = (K * ya) % q;
            }
            int c2 = (message * K) % q;
            System.out.println("Encrypted message:");
            System.out.println("c1: " + c1);
            System.out.println("c2: " + c2);
        } else if (number == 2) {
            System.out.print("Enter c1: ");
            int c1 = sc.nextInt();
            System.out.print("Enter c2: ");
            int c2 = sc.nextInt();
            int K = 1;
            for (int i = 0; i < xa; i++) {
                K = (K * c1) % q;
            }
            int K_inverse = modInverse(K, q);
            int decryptedMessage = (c2 * K_inverse) % q;
            System.out.println("Decrypted message: " + decryptedMessage);
        } else {
            System.out.println("Invalid input. Choose 1 for encryption, 2 for decryption, or 3 to exit.");
        }
        sc.close();
    }
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
}
