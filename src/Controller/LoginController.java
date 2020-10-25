package Controller;

import Enum.AccountType;
import ErrorMessage.ErrorMessage;
import FileManager.LoginInfoFileManager;
import FileManager.StorageManager;
import Model.LoginInfo;
import View.LoginUi;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    private AccountType accountType;
    private LoginUi loginUi;
    private LoginInfo providedLoginInfo;
    private LoginInfoFileManager loginInfoFileManager;
    private StorageManager storageManager;

    public LoginController() {
        loginUi = new LoginUi();
        loginInfoFileManager = new LoginInfoFileManager();
        storageManager = new StorageManager();
    }

    public AccountType run() {
        do {
            accountType = loginUi.getAccountType();
            providedLoginInfo = loginUi.getLoginInfo();
        } while (!verifyLoginInfo());

        return accountType;
    }

    public String getUserId() {
        assert providedLoginInfo != null : "Error: providedLoginInfo is not initialized!";
        return providedLoginInfo.getUserId();
    }

    public static String hash(String password) {

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
        }

        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private boolean verifyLoginInfo() {
        try {
            ArrayList<LoginInfo> loginInfoList = accountType == AccountType.ADMIN ?
                    loginInfoFileManager.retrieveAdminLoginInfoList() : loginInfoFileManager.retrieveStudentLoginInfoList();

            for (LoginInfo loginInfo: loginInfoList) {
                if (hash(loginInfo.toString()).equals(hash(providedLoginInfo.toString()))) {
                    return verifyAccessPeriod(loginInfo.getUserId());
                }
            }

            loginUi.printErrorMessage(ErrorMessage.WRONG_LOGIN_INFO);
            return false;
        } catch (FileNotFoundException e) {
            if (accountType == AccountType.ADMIN) {
                loginUi.printErrorMessage(ErrorMessage.MISSING_ADMIN_LOGIN_INFO);
            } else if (accountType == AccountType.STUDENT) {
                loginUi.printErrorMessage(ErrorMessage.MISSING_STUDENT_LOGIN_INFO);
            }
            return false;
        }
    }

    private boolean verifyAccessPeriod(String userId) {
        assert accountType != null;

        if (accountType == AccountType.ADMIN) {
            return true;
        }

        if (!isWithinAccessTimeForStudent(userId)) {
            loginUi.printMessageWithDivider(
                    ErrorMessage.WRONG_ACCESS_PERIOD,
                    storageManager.getStudent(userId).getAccessTime().toString()
            );
            return false;
        }

        return true;
    }

    private boolean isWithinAccessTimeForStudent(String userId) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return storageManager.getStudent(userId).isWithinAccessTime(dateTimeNow);
    }

}
