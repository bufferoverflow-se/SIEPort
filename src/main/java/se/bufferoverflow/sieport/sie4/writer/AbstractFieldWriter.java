package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.SIE4Exception;
import se.bufferoverflow.sieport.sie4.SIE4Item;

abstract class AbstractFieldWriter<T extends SIE4Item> {
    static String quoted(String s) {
        if (s.indexOf(' ') > -1) {
            return "\"" + s + "\"";
        }
        return s;
    }

    String writeItem(SIE4Item item) {
        if (item == null) {
            throw new SIE4Exception("Item cannot be null");
        }
        String label = item.getClass().getSimpleName().toUpperCase();
        return "#%s %s".formatted(label, writeFields((T) item));
    }

    abstract String writeFields(T item);
}
