package se.bufferoverflow.sieport.sie4;

public class SIE4Exception extends RuntimeException {
    public SIE4Exception(String errorMessage) {
        super(errorMessage);
    }

    public SIE4Exception(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
