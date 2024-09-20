package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.SIE4Exception;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.Label;

abstract class AbstractFieldWriter<T extends SIE4Item> {
    protected static String quoted(String s) {
        if (s.indexOf(' ') > -1) {
            return "\"" + s + "\"";
        }
        return s;
    }

    public String writeItem(Label label, SIE4Item item) {
        if (label == null || item == null) {
            throw new SIE4Exception("label or fileItem cannot be null");
        }
        return "#%s %s".formatted(label, writeFields((T) item));
    }

    abstract String writeFields(T field);
}
