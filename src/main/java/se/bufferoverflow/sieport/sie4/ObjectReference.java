package se.bufferoverflow.sieport.sie4;

public record ObjectReference(int dimensionNo, String objectNo) {
    public static ObjectReference of(int dimensionNo, String objectNo) {
        return new ObjectReference(dimensionNo, objectNo);
    }

    @Override
    public String toString() {
        return "{%d %s}".formatted(dimensionNo, objectNo);
    }
}
