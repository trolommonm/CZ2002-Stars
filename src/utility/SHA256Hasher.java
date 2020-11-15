package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hasher {
    public static String hash(String password) {
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            int length = bytes.length;
            for (int i = 0; i < length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 32).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }
}
