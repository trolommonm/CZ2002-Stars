package Exception;

import ErrorMessage.ErrorMessage;

public class PeerClashingWaitListedIndexNumberException extends Exception {
    public PeerClashingWaitListedIndexNumberException() {
        super(ErrorMessage.PEER_WAITLISTED_CLASHING_INDEX);
    }
}
