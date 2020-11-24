package exception;

import errormessage.ErrorMessage;
 
public class ClashingRegisteredIndexNumberException extends Exception {
    public ClashingRegisteredIndexNumberException() {
        super(ErrorMessage.CLASHING_REGISTERED_INDEX_NUMBER);
    }
}
