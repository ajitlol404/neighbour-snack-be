import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class JwtSecretEncoderApp {

    private static final int REQUIRED_BYTES = 32; // 256 bits

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🔐 JWT HS256 Secret Generator (from user input)");
        System.out.print("Enter your strong secret string: ");
        String userInput = scanner.nextLine();

        try {
            // Hash the input to get a 256-bit key
            byte[] hash = sha256(userInput);

            if (hash.length < REQUIRED_BYTES) {
                System.out.println("❌ Error: Could not derive 256-bit key.");
                return;
            }

            // Encode to Base64
            String base64Key = Base64.getEncoder().encodeToString(hash);

            System.out.println("\n✅ Base64-encoded 256-bit secret key:");
            System.out.println(base64Key);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("❌ SHA-256 not available: " + e.getMessage());
        }

        scanner.close();
    }

    private static byte[] sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes(StandardCharsets.UTF_8));
    }
}
