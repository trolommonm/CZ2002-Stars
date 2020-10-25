package Exception;

import ErrorMessage.ErrorMessage;

public class NoVacancySwapException extends Exception {
    public NoVacancySwapException() {
        super(ErrorMessage.NO_VACANCY_SWAP);
    }
}
