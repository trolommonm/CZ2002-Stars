package Exception;

import ErrorMessage.ErrorMessage;

public class WrongLoginInfoException extends Exception {
    public WrongLoginInfoException() {
        super(ErrorMessage.WRONG_LOGIN_INFO);
    }
}
