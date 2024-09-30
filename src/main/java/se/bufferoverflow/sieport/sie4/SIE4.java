package se.bufferoverflow.sieport.sie4;

import se.bufferoverflow.sieport.sie4.parser.InFieldMapper;
import se.bufferoverflow.sieport.sie4.writer.OutFieldMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SIE4 {
    public static final Charset SIE4_CHARSET = Charset.forName("IBM-437");

    private SIE4() {
    }

    public static SIE4Content parse(Path path) {
        return parse(path.toFile());
    }

    public static SIE4Content parse(File file) {
        try (var is = new FileInputStream(file)) {
            return parse(is);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static SIE4Content parse(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, SIE4_CHARSET));

        try {
            List<String> verBuffer = new ArrayList<>();
            List<SIE4Item> result = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.strip();

                if (trimmedLine.startsWith("#VER")) {
                    verBuffer.add(trimmedLine);
                } else if (trimmedLine.startsWith("}")) {
                    result.add(InFieldMapper.toModel(verBuffer));
                    verBuffer.clear();
                } else if (!trimmedLine.startsWith("{")) {
                    if (!verBuffer.isEmpty()) {
                        verBuffer.add(trimmedLine);
                    } else {
                        result.add(InFieldMapper.toModel(trimmedLine));
                    }
                }
            }

            return new SIE4Content(result);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


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
        PrintWriter writer = new PrintWriter(outputStream, true, SIE4_CHARSET);
        items.forEach(item -> writer.println(OutFieldMapper.toFileString(item)));
    }
}
