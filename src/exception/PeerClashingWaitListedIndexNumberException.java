package exception;

import errormessage.ErrorMessage;

public class PeerClashingWaitListedIndexNumberException extends Exception {
    public PeerClashingWaitListedIndexNumberException() {
        super(ErrorMessage.PEER_WAITLISTED_CLASHING_INDEX);
    }
}
