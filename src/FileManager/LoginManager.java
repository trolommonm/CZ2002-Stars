package FileManager;

import Enum.AccountType;
import Model.LoginInfo;
import Exception.WrongLoginInfoException;
import Exception.WrongAccessPeriodException;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoginManager {
    private LoginInfoFileManager loginInfoFileManager;
    private StorageManager storageManager;

    public LoginManager(LoginInfoFileManager loginInfoFileManager, StorageManager storageManager) {
        this.loginInfoFileManager = loginInfoFileManager;
        this.storageManager = storageManager;
    }

    public void verifyLoginInfo(AccountType accountType, LoginInfo providedLoginInfo)
        throws FileNotFoundException, WrongLoginInfoException, WrongAccessPeriodException {
        switch (accountType) {
            case ADMIN:
                verifyAdminLoginInfo(providedLoginInfo);
                break;
            case STUDENT:
                verifyStudentLoginInfo(providedLoginInfo);
                break;
            default:
                assert false : "Invalid account type!";
        }
    }

    private void verifyStudentLoginInfo(LoginInfo providedLoginInfo)
            throws WrongAccessPeriodException, WrongLoginInfoException, FileNotFoundException {
        ArrayList<LoginInfo> loginInfoList = loginInfoFileManager.retrieveStudentLoginInfoList();
        for (LoginInfo loginInfo: loginInfoList) {
            if (loginInfo.equals(providedLoginInfo)) {
                if (isWithinAccessTimeForStudent(loginInfo.getUserId())) {
                    return;
                }

                throw new WrongAccessPeriodException(storageManager.getStudent(loginInfo.getUserId()).getAccessTime());
            }
        }

        throw new WrongLoginInfoException();
    }

    private void verifyAdminLoginInfo(LoginInfo providedLoginInfo) throws FileNotFoundException, WrongLoginInfoException {
        ArrayList<LoginInfo> loginInfoList = loginInfoFileManager.retrieveAdminLoginInfoList();
        for (LoginInfo loginInfo: loginInfoList) {
            if (loginInfo.equals(providedLoginInfo)) {
                return;
            }
        }

        throw new WrongLoginInfoException();
    }

    private boolean isWithinAccessTimeForStudent(String userId) {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return storageManager.getStudent(userId).isWithinAccessTime(dateTimeNow);
    }
}
