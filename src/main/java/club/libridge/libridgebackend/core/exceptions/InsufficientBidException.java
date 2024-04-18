package club.libridge.libridgebackend.core.exceptions;

@SuppressWarnings("serial")
public class InsufficientBidException extends RuntimeException {
    public InsufficientBidException() {
        super("Your bid must supersede the last bid.");
    }
}
