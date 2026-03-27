package se.bufferoverflow.sieport.sie4;

import se.bufferoverflow.sieport.sie4.parser.InFieldMapper;
import se.bufferoverflow.sieport.sie4.validator.ValidationError;
import se.bufferoverflow.sieport.sie4.validator.Validator;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Static utility class for parsing and writing SIE4 files.
 *
 * <p>SIE4 is a standard format used in Swedish accounting software to exchange bookkeeping data.
 * This library supports both SIE 4E ({@code .se}, full export) and SIE 4I ({@code .si},
 * transaction import) variants. All SIE4 files are encoded in IBM Code Page 437
 * ({@link #SIE4_CHARSET}).
 *
 * <p>Typical usage:
 * <pre>{@code
 * // Parse a file
 * SIE4Document doc = SIE4.parse(Path.of("company.se"));
 *
 * // Build and write a new file
 * SIE4Document doc = SIE4Document.defaultBuilder()
 *         .program(new SIE4Item.Program("My App", "1.0"))
 *         .fnamn(new SIE4Item.Fnamn("Acme AB"))
 *         ...
 *         .buildAndValidate();
 * SIE4.write(Path.of("output.se"), doc);
 * }</pre>
 */
public class SIE4 {
    /** The character encoding used by SIE4 files (IBM Code Page 437). */
    public static final Charset SIE4_CHARSET = Charset.forName("IBM-437");

    /** Date formatter for the {@code yyyyMMdd} pattern used throughout the SIE4 format. */
    public static final DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final Logger LOG = Logger.getLogger(SIE4.class.getName());

    private SIE4() {
    }

    /**
     * Parses a SIE4 file at the given path.
     *
     * @param path path to the SIE4 file
     * @return the parsed document
     * @throws UncheckedIOException if an I/O error occurs
     * @throws SIE4Exception if the file is malformed
     */
    public static SIE4Document parse(Path path) {
        Objects.requireNonNull(path, "path must not be null");
        return parse(path.toFile());
    }

    /**
     * Parses a SIE4 file.
     *
     * @param file the SIE4 file
     * @return the parsed document
     * @throws UncheckedIOException if an I/O error occurs
     * @throws SIE4Exception if the file is malformed
     */
    public static SIE4Document parse(File file) {
        Objects.requireNonNull(file, "file must not be null");
        try (var is = new FileInputStream(file)) {
            return parse(is);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Parses SIE4 data from an input stream. The stream is <em>not</em> closed by this method;
     * the caller is responsible for closing it.
     *
     * @param inputStream the stream to read from; must be encoded in {@link #SIE4_CHARSET}
     * @return the parsed document
     * @throws UncheckedIOException if an I/O error occurs
     * @throws SIE4Exception if the data is malformed
     */
    public static SIE4Document parse(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream must not be null");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, SIE4_CHARSET));
            List<String> verBuffer = new ArrayList<>();
            List<SIE4Item> result = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.strip();

                if (trimmedLine.isEmpty()) {
                    continue;
                }

                if (trimmedLine.startsWith("#VER")) {
                    verBuffer.add(trimmedLine);
                } else if (trimmedLine.startsWith("}")) {
                    result.add(InFieldMapper.toModel(verBuffer));
                    verBuffer.clear();
                } else if (!trimmedLine.startsWith("{")) {
                    if (!verBuffer.isEmpty()) {
                        verBuffer.add(trimmedLine);
                    } else {
                        SIE4Item item = InFieldMapper.toModel(trimmedLine);
                        if (item instanceof SIE4Item.Transaction) {
                            LOG.warning("Skipping transaction item outside VER block: " + trimmedLine);
                        } else if (item != null) {
                            result.add(item);
                        }
                    }
                }
            }

            if (!verBuffer.isEmpty()) {
                throw new SIE4Exception("Unclosed VER block at end of file: " + verBuffer.getFirst());
            }

            return SIE4Document.from(result);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Writes SIE4 items to a file, sorted in the order required by the SIE4 specification.
     * Validation is performed before the file is opened, so an existing file is never
     * truncated if validation fails.
     *
     * @param destination the output file
     * @param items the items to write
     * @param options optional {@link WriteOptions}
     * @throws SIE4Exception if validation fails
     * @throws UncheckedIOException if an I/O error occurs
     */
    public static void write(Path destination, List<SIE4Item> items, WriteOptions... options) {
        Objects.requireNonNull(destination, "destination must not be null");
        Objects.requireNonNull(items, "items must not be null");
        write(destination.toFile(), items, options);
    }

    /**
     * Writes SIE4 items to a file, sorted in the order required by the SIE4 specification.
     * Validation is performed before the file is opened, so an existing file is never
     * truncated if validation fails.
     *
     * @param file the output file
     * @param items the items to write
     * @param options optional {@link WriteOptions}
     * @throws SIE4Exception if validation fails
     * @throws UncheckedIOException if an I/O error occurs
     */
    public static void write(File file, List<SIE4Item> items, WriteOptions... options) {
        Objects.requireNonNull(file, "file must not be null");
        Objects.requireNonNull(items, "items must not be null");
        validateItems(items, options);
        // Write to a temp file in the same directory, then atomically move it to the destination.
        // This ensures the destination is never left in a partial state if the write fails mid-way.
        Path destination = file.toPath();
        Path parent = destination.getParent();
        Path tmp = null;
        try {
            tmp = Files.createTempFile(parent != null ? parent : Path.of("."), ".sie4-", ".tmp");
            try (var os = new FileOutputStream(tmp.toFile())) {
                write(os, items, WriteOptions.SKIP_VALIDATION);
            }
            Files.move(tmp, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            tmp = null;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            if (tmp != null) {
                try { Files.deleteIfExists(tmp); } catch (IOException ignored) {}
            }
        }
    }

    /**
     * Writes a {@link SIE4Document} to a file.
     *
     * @see #write(Path, List, WriteOptions...)
     */
    public static void write(Path destination, SIE4Document doc, WriteOptions... options) {
        write(destination, doc.getItems(), options);
    }

    /**
     * Writes a {@link SIE4Document} to a file.
     *
     * @see #write(File, List, WriteOptions...)
     */
    public static void write(File file, SIE4Document doc, WriteOptions... options) {
        write(file, doc.getItems(), options);
    }

    /**
     * Writes a {@link SIE4Document} to an output stream.
     *
     * @see #write(OutputStream, List, WriteOptions...)
     */
    public static void write(OutputStream outputStream, SIE4Document doc, WriteOptions... options) {
        write(outputStream, doc.getItems(), options);
    }

    /**
     * Writes SIE4 items to an output stream, sorted in the order required by the SIE4
     * specification. The caller is responsible for closing the stream.
     *
     * @param outputStream the stream to write to; will be written using {@link #SIE4_CHARSET}
     * @param items the items to write
     * @param options optional {@link WriteOptions}
     * @throws SIE4Exception if validation fails
     * @throws UncheckedIOException if an I/O error occurs during writing
     */
    public static void write(OutputStream outputStream, List<SIE4Item> items, WriteOptions... options) {
        Objects.requireNonNull(outputStream, "outputStream must not be null");
        Objects.requireNonNull(items, "items must not be null");
        validateItems(items, options);

        PrintWriter writer = new PrintWriter(outputStream, true, SIE4_CHARSET);
        items.stream()
                .sorted(Comparator.comparingInt(item -> item.itemType().ordinal()))
                .forEach(item -> writer.println(OutFieldMapper.toFileString(item)));
        if (writer.checkError()) {
            throw new UncheckedIOException(new IOException("I/O error occurred while writing SIE4 data"));
        }
    }

    /**
     * Validates a {@link SIE4Document} without writing, returning any validation errors found.
     *
     * @param doc the document to validate
     * @param options optional {@link WriteOptions}; use {@link WriteOptions#SIE4I} to validate
     *                against SIE 4I rules instead of the default SIE 4E rules
     * @return a list of validation errors, or an empty list if the document is valid
     */
    public static List<ValidationError> validate(SIE4Document doc, WriteOptions... options) {
        Objects.requireNonNull(doc, "doc must not be null");
        return validate(doc.getItems(), options);
    }

    /**
     * Validates a list of SIE4 items without writing, returning any validation errors found.
     *
     * @param items the items to validate
     * @param options optional {@link WriteOptions}; use {@link WriteOptions#SIE4I} to validate
     *                against SIE 4I rules instead of the default SIE 4E rules
     * @return a list of validation errors, or an empty list if the items are valid
     */
    public static List<ValidationError> validate(List<SIE4Item> items, WriteOptions... options) {
        Objects.requireNonNull(items, "items must not be null");
        List<WriteOptions> opts = Arrays.asList(options);
        if (opts.contains(WriteOptions.SKIP_VALIDATION)) {
            return List.of();
        }
        return opts.contains(WriteOptions.SIE4I)
                ? Validator.validateSie4i(items)
                : Validator.validateSie4e(items);
    }

    private static void validateItems(List<SIE4Item> items, WriteOptions... options) {
        List<WriteOptions> opts = Arrays.asList(options);
        if (!opts.contains(WriteOptions.SKIP_VALIDATION)) {
            List<ValidationError> errors = opts.contains(WriteOptions.SIE4I)
                    ? Validator.validateSie4i(items)
                    : Validator.validateSie4e(items);
            if (!errors.isEmpty()) {
                String message = "Validation failed: " + errors.stream().map(Object::toString)
                        .collect(Collectors.joining(", "));
                throw new SIE4Exception(message);
            }
        }
    }

    public enum WriteOptions {
        /**
         * Write a SIE 4I transaction file (for importing transaction data to a reporting program),
         * opposed to a 4E export file (for exporting data from a reporting program) which is the default.
         */
        SIE4I,
        /**
         * Skip checks performed to ensure the data to write follows the SIE standard.
         */
        SKIP_VALIDATION
    }
}
