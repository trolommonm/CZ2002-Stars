package exception;

import errormessage.ErrorMessage;

public class NoVacancyException extends Exception {
    public NoVacancyException() {
        super(ErrorMessage.NO_VACANCY);
    }
}
