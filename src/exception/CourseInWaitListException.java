package exception;

import errormessage.ErrorMessage;

public class CourseInWaitListException extends Exception {
    public CourseInWaitListException() {
        super(ErrorMessage.COURSE_ALREADY_IN_WAITLIST);
    }
}
