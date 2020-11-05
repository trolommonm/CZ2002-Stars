package Exception;

import ErrorMessage.ErrorMessage;

public class PeerDoesNotTakeCourseException extends Exception {
    public PeerDoesNotTakeCourseException() {
        super(ErrorMessage.PEER_DOES_NOT_TAKE_COURSE);
    }
}
