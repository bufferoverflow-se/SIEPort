package se.bufferoverflow.sieport.sie4;

import se.bufferoverflow.sieport.sie4.writer.OutFieldMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

public class SIE4Writer {
    private SIE4Writer() {}

    public static void write(Path destination, List<SIE4Item> items) {
        write(destination.toFile(), items);
    }

    public static void write(File file, List<SIE4Item> items) {
        try (var os = new FileOutputStream(file)) {
            write(os, items);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void write(OutputStream outputStream, List<SIE4Item> items) {
        PrintWriter writer = new PrintWriter(outputStream, true, Constants.SIE4_CHARSET);
        items.forEach(item -> writer.println(OutFieldMapper.toFileString(item)));
    }
}
