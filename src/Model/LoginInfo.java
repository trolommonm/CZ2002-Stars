package Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginInfo {

    private String userId;
    private String password;

    public LoginInfo(String userId, String password) {
        this.userId = userId;
        this.password = hash(password);
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    private String hash(String password) {
        String passwordToHash = password;
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordToHash.getBytes());
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LoginInfo) {
            LoginInfo loginInfoObj = (LoginInfo) obj;

            return getUserId().equals(loginInfoObj.getUserId()) &&
                    getPassword().equals(loginInfoObj.getPassword());
        }

        return false;
    }
}
