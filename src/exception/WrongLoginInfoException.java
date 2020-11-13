package exception;

import errormessage.ErrorMessage;

public class WrongLoginInfoException extends Exception {
    public WrongLoginInfoException() {
        super(ErrorMessage.WRONG_LOGIN_INFO);
    }
}
