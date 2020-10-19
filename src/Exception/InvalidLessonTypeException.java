package Exception;

import ErrorMessage.ErrorMessage;

public class InvalidLessonTypeException extends Exception {
    public InvalidLessonTypeException() {
        super(ErrorMessage.INVALID_LESSON_TYPE);
    }
}
