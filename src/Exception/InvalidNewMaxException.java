package Exception;

import ErrorMessage.ErrorMessage;

public class InvalidNewMaxException extends Exception {
    public InvalidNewMaxException() {
        super(ErrorMessage.INVALID_NEW_MAX_VACANCY);
    }
}
