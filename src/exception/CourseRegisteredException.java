package exception;

import errormessage.ErrorMessage;

public class CourseRegisteredException extends Exception {
    public CourseRegisteredException() {
        super(ErrorMessage.COURSE_ALREADY_REGISTERED);
    }
}
