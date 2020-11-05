package Exception;

import ErrorMessage.ErrorMessage;

public class ClashingWaitListedIndexNumberException extends Exception {
    public ClashingWaitListedIndexNumberException() {
        super(ErrorMessage.CLASHING_WAITLISTED_INDEX_NUMBER);
    }
}
