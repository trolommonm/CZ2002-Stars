package exception;

import errormessage.ErrorMessage;

public class SameIndexNumberSwapException extends Exception {
    public SameIndexNumberSwapException() {
        super(ErrorMessage.SAME_INDEX_SWAP);
    }
}
