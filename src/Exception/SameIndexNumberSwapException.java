package Exception;

import ErrorMessage.ErrorMessage;

public class SameIndexNumberSwapException extends Exception {
    public SameIndexNumberSwapException() {
        super(ErrorMessage.SAME_INDEX_SWAP);
    }
}
