package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.FileItem;
import se.bufferoverflow.sieport.sie4.Label;

abstract class AbstractFieldWriter<T extends FileItem> {
    protected static String quoted(String s) {
        if (s.indexOf(' ') > -1) {
            return "\"" + s + "\"";
        }
        return s;
    }

    public String writeItem(Label label, FileItem fileItem) {
        if (label == null || fileItem == null) {
            throw new IllegalArgumentException("label or fileItem cannot be null");
        }
        return "#%s %s".formatted(label, writeFields((T) fileItem));
    }

    abstract String writeFields(T field);
}
