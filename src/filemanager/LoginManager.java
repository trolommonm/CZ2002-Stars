package filemanager;

import model.LoginInfo;
import exception.WrongLoginInfoException;
import exception.WrongAccessPeriodException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoginManager implements ILoginable {
    private ILoginInfoFileManager loginInfoFileManager;
    private IStorageManager storageManager;

    public LoginManager(ILoginInfoFileManager loginInfoFileManager, IStorageManager storageManager) {
        this.loginInfoFileManager = loginInfoFileManager;
        this.storageManager = storageManager;
    }

    @Override
    public void verifyLoginInfo(LoginInfo providedLoginInfo)
        throws WrongLoginInfoException, WrongAccessPeriodException {
        switch (providedLoginInfo.getAccountType()) {
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
            throws WrongAccessPeriodException, WrongLoginInfoException {
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

    private void verifyAdminLoginInfo(LoginInfo providedLoginInfo) throws WrongLoginInfoException {
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
