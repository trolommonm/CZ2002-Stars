package Exception;

import ErrorMessage.ErrorMessage;

public class CourseRegisteredException extends Exception {
    public CourseRegisteredException() {
        super(ErrorMessage.COURSE_ALREADY_REGISTERED);
    }
}
