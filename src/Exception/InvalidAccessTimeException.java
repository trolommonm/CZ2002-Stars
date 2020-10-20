package Exception;

import ErrorMessage.ErrorMessage;

public class InvalidAccessTimeException extends IllegalArgumentException {
    public InvalidAccessTimeException() {
        super(ErrorMessage.INVALID_ACCESS_TIME);
    }
}
