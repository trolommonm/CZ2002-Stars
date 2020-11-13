package exception;

import errormessage.ErrorMessage;

public class NoVacancySwapException extends Exception {
    public NoVacancySwapException() {
        super(ErrorMessage.NO_VACANCY_SWAP);
    }
}
