package controller;

import model.AccountType;
import filemanager.LoginInfoFileManager;
import filemanager.LoginManager;
import filemanager.StorageManager;
import model.LoginInfo;
import view.LoginUi;
import exception.WrongAccessPeriodException;
import exception.WrongLoginInfoException;

import java.io.FileNotFoundException;

public class LoginController {
    private AccountType accountType;
    private LoginUi loginUi;
    private LoginInfo providedLoginInfo;
    private LoginInfoFileManager loginInfoFileManager;
    private LoginManager loginManager;
    private StorageManager storageManager;

    public LoginController() {
        loginUi = new LoginUi();
        loginInfoFileManager = new LoginInfoFileManager();
        storageManager = new StorageManager();
        loginManager = new LoginManager(loginInfoFileManager, storageManager);
    }

    public AccountType run() {
        while (true) {
            accountType = loginUi.getAccountType();
            providedLoginInfo = loginUi.getLoginInfo();

            try {
                loginManager.verifyLoginInfo(accountType, providedLoginInfo);
                break;
            } catch (WrongAccessPeriodException | WrongLoginInfoException | FileNotFoundException e) {
                loginUi.printErrorMessage(e.getMessage());
            }
        }

        return accountType;
    }

    public String getUserId() {
        assert providedLoginInfo != null : "Error: providedLoginInfo is not initialized!";
        return providedLoginInfo.getUserId();
    }

}
