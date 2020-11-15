package controller;

import filemanager.ILoginable;
import filemanager.IStorageManager;
import model.AccountType;
import model.LoginInfo;
import view.LoginUi;
import exception.WrongAccessPeriodException;
import exception.WrongLoginInfoException;

public class LoginController {
    private AccountType accountType;
    private LoginUi loginUi;
    private ILoginable loginManager;
    private IStorageManager storageManager;

    public LoginController(IStorageManager storageManager, ILoginable loginManager) {
        loginUi = new LoginUi();
        this.storageManager = storageManager;
        this.loginManager = loginManager;
    }

    public LoginInfo run() {
        LoginInfo providedLoginInfo;
        while (true) {
            accountType = loginUi.getAccountType();
            providedLoginInfo = loginUi.getLoginInfo(accountType);

            try {
                loginManager.verifyLoginInfo(providedLoginInfo);
                break;
            } catch (WrongAccessPeriodException | WrongLoginInfoException e) {
                loginUi.printErrorMessage(e.getMessage());
            }
        }

        return providedLoginInfo;
    }

}
