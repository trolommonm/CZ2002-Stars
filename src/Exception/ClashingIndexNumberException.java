package Exception;

import ErrorMessage.ErrorMessage;

public class ClashingIndexNumberException extends Exception {
    public ClashingIndexNumberException() {
        super(ErrorMessage.CLASHING_INDEX_NUMBER);
    }
}
