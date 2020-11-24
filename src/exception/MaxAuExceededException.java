package exception;

import errormessage.ErrorMessage;

public class MaxAuExceededException extends Exception {
    public MaxAuExceededException() {
        super(ErrorMessage.MAX_AU_EXCEEDED);
    }
}
