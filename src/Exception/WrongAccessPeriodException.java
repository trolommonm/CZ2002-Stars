package Exception;

import ErrorMessage.ErrorMessage;
import Model.AccessTime;

public class WrongAccessPeriodException extends Exception {
    public WrongAccessPeriodException(AccessTime accessTime) {
        super(ErrorMessage.WRONG_ACCESS_PERIOD + "\n" + accessTime.toString());
    }
}
