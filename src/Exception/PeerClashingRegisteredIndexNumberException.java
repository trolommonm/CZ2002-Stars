package Exception;

import ErrorMessage.ErrorMessage;

public class PeerClashingRegisteredIndexNumberException extends Exception {
    public PeerClashingRegisteredIndexNumberException() {
        super(ErrorMessage.PEER_REGISTERED_CLASHING_INDEX);
    }
}
