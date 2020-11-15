package filemanager;

import exception.WrongAccessPeriodException;
import exception.WrongLoginInfoException;
import model.LoginInfo;

public interface ILoginable {
    void verifyLoginInfo(LoginInfo providedLoginInfo) throws WrongLoginInfoException, WrongAccessPeriodException;
}
