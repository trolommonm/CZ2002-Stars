package Controller;

import Enum.AccountType;
import FileManager.LoginInfoFileManager;
import FileManager.LoginManager;
import FileManager.StorageManager;
import Model.LoginInfo;
import View.LoginUi;
import Exception.WrongAccessPeriodException;
import Exception.WrongLoginInfoException;

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
