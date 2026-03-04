package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.SIE4Exception;
import se.bufferoverflow.sieport.sie4.SIE4Item;

abstract class AbstractFieldWriter<T extends SIE4Item> {
    static String quoted(String s) {
        String escaped = s.replace("\"", "\\\"");
        if (s.indexOf(' ') > -1 || s.indexOf('"') > -1) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    @SuppressWarnings("unchecked")
    String writeItem(SIE4Item item) {
        if (item == null) {
            throw new SIE4Exception("Item cannot be null");
        }
        String label = item.itemType().name();
        return "#%s %s".formatted(label, writeFields((T) item));
    }

    abstract String writeFields(T item);
}
