package controller;

import filemanager.ILoginable;
import model.AccountType;
import model.LoginInfo;
import view.LoginUi;
import exception.WrongAccessPeriodException;
import exception.WrongLoginInfoException;

public class LoginController {
    private LoginUi loginUi;
    private ILoginable loginManager;

    public LoginController(ILoginable loginManager) {
        loginUi = new LoginUi();
        this.loginManager = loginManager;
    }

    public LoginInfo run() {
        LoginInfo providedLoginInfo;
        while (true) {
            AccountType accountType = loginUi.getAccountType();
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
