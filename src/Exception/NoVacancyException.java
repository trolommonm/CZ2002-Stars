package Exception;

import ErrorMessage.ErrorMessage;

public class NoVacancyException extends Exception {
    public NoVacancyException() {
        super(ErrorMessage.NO_VACANCY);
    }
}
