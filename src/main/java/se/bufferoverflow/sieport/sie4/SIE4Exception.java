package se.bufferoverflow.sieport.sie4;

/**
 * Thrown when a SIE4 parsing, writing, or validation operation fails.
 *
 * <p>This is an unchecked exception. Callers should expect it from {@link SIE4#parse}
 * and {@link SIE4#write} in response to malformed input or validation violations.
 */
public class SIE4Exception extends RuntimeException {
    public SIE4Exception(String errorMessage) {
        super(errorMessage);
    }

    public SIE4Exception(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
