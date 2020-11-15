package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginInfo {
    private AccountType accountType;
    private String userId;
    private String password;

    public LoginInfo(AccountType accountType, String userId, String password) {
        this.accountType = accountType;
        this.userId = userId;
        this.password = hash(password);
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
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
                    getPassword().equals(loginInfoObj.getPassword()) &&
                    getAccountType().equals(loginInfoObj.getAccountType());
        }

        return false;
    }
}
