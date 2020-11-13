package exception;

import errormessage.ErrorMessage;

public class InvalidLessonTypeException extends Exception {
    public InvalidLessonTypeException() {
        super(ErrorMessage.INVALID_LESSON_TYPE);
    }
}
