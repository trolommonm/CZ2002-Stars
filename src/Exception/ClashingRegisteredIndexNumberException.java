package Exception;

import ErrorMessage.ErrorMessage;

public class ClashingRegisteredIndexNumberException extends Exception {
    public ClashingRegisteredIndexNumberException() {
        super(ErrorMessage.CLASHING_REGISTERED_INDEX_NUMBER);
    }
}
