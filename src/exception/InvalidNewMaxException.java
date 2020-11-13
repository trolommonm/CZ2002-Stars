package exception;

import errormessage.ErrorMessage;

public class InvalidNewMaxException extends Exception {
    public InvalidNewMaxException() {
        super(ErrorMessage.INVALID_NEW_MAX_VACANCY);
    }
}
