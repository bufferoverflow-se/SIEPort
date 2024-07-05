package se.bufferoverflow.sieport.sie4;

import se.bufferoverflow.sieport.sie4.parser.FieldMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SIE4Parser {

    private SIE4Parser() {
    }

    public static Object parse(Path path) throws FileNotFoundException {
        return parse(path.toFile());
    }

    public static Object parse(File file) throws FileNotFoundException {
        return parse(new FileInputStream(file));
    }

    public static List<FileItem> parse(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Constants.SIE4_CHARSET));

        try {
            List<String> verBuffer = new ArrayList<>();
            List<FileItem> result = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.strip();

                if (trimmedLine.startsWith("#VER")) {
                    verBuffer.add(trimmedLine);
                } else if (trimmedLine.startsWith("}")) {
                    result.add(FieldMapper.toModel(verBuffer));
                    verBuffer.clear();
                } else if (!trimmedLine.startsWith("{")) {
                    if (!verBuffer.isEmpty()) {
                        verBuffer.add(trimmedLine);
                    } else {
                        result.add(FieldMapper.toModel(trimmedLine));
                    }
                }
            }

            return result;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
