package exception;

import errormessage.ErrorMessage;
import model.AccessTime;

public class WrongAccessPeriodException extends Exception {
    public WrongAccessPeriodException(AccessTime accessTime) {
        super(ErrorMessage.WRONG_ACCESS_PERIOD + "\n" + accessTime.toString());
    }
}
