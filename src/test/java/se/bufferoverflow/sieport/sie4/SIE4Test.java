package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.bufferoverflow.sieport.sie4.validator.ValidationError;
import se.bufferoverflow.sieport.sie4.validator.ValidationError.MissingMandatoryItems;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SIE4Test {

    private InputStream sie4Sample;
    private Path sie4SampleFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4Sample = SIE4Test.class.getClassLoader().getResourceAsStream("./SIE4-sample.SE");
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void readSample() {
        List<SIE4Item> items = SIE4.parse(sie4Sample).getItems();

        assertThat(items).hasSize(2160);
        assertThat(items.stream()).filteredOn(it -> it instanceof SIE4Item.Ver).hasSize(295);
    }

    @Test
    void writeFile() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);
        Path outputFile = tempDir.resolve(UUID.randomUUID() + ".se");
        SIE4.write(outputFile, doc);

        assertThat(outputFile)
                .exists()
                .content()
                .startsWith("#FLAGGA 0");
    }

    @Test
    void readAndWriteSample() {
        List<SIE4Item> parsedItems = SIE4.parse(sie4SampleFile).getItems();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4.write(baos, parsedItems);

        // Write sorts items by SIE4ItemType ordinal; re-parse and compare item sets
        List<SIE4Item> reparsedItems = SIE4.parse(new ByteArrayInputStream(baos.toByteArray())).getItems();
        assertThat(reparsedItems).containsExactlyInAnyOrderElementsOf(parsedItems);
    }

    @Test
    void writeIncorrectDataWithValidation() {
        List<SIE4Item> items = List.of(SIE4Item.Flagga.SET);
        assertThatThrownBy(() -> SIE4.write(tempDir.resolve(UUID.randomUUID() + ".se"), items))
                .isInstanceOf(SIE4Exception.class)
                .hasMessageContaining(MissingMandatoryItems.class.getSimpleName());
    }

    @Test
    void parse_unknownLabel_shouldBeIgnored() {
        String input = "#FLAGGA 0\n#KSUMMA\n#FNAMN TestCompany\n#UNKNOWN some data\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes(SIE4.SIE4_CHARSET));

        List<SIE4Item> items = SIE4.parse(stream).getItems();

        assertThat(items).hasSize(2);
        assertThat(items.get(0)).isEqualTo(SIE4Item.Flagga.UNSET);
        assertThat(items.get(1)).isEqualTo(new SIE4Item.Fnamn("TestCompany"));
    }

    @Test
    void parse_transactionAtTopLevel_shouldBeIgnored() {
        String input = "#FLAGGA 0\n#TRANS 1930 {} 100.00\n#RTRANS 1920 {} -100.00\n#FNAMN TestCompany\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes(SIE4.SIE4_CHARSET));

        List<SIE4Item> items = SIE4.parse(stream).getItems();

        assertThat(items).hasSize(2);
        assertThat(items).noneMatch(item -> item instanceof SIE4Item.Transaction);
    }

    @Test
    void write_outOfOrderItems_areSortedByItemType() {
        // Items passed in wrong order: FNAMN (ordinal 17) before FLAGGA (ordinal 0)
        List<SIE4Item> items = List.of(
                new SIE4Item.Fnamn("Acme"),
                SIE4Item.Flagga.UNSET
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4.write(baos, items, SIE4.WriteOptions.SKIP_VALIDATION);
        String output = baos.toString(SIE4.SIE4_CHARSET);

        assertThat(output).startsWith("#FLAGGA");
    }

    @Test
    void writeIncorrectDataWithoutValidation() {
        List<SIE4Item> items = List.of(SIE4Item.Flagga.UNSET);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4.write(baos, items, SIE4.WriteOptions.SKIP_VALIDATION);
        String output = baos.toString(SIE4.SIE4_CHARSET);

        assertThat(output).isEqualTo("#FLAGGA 0\n");
    }

    @Test
    void parse_unclosedVerBlock_shouldThrow() {
        // A VER block without a closing } must not silently drop the voucher
        String input = "#FLAGGA 0\n#VER A 1 20211125\n{\n   #TRANS 1930 {} -100.00\n   #TRANS 1920 {} 100.00\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes(SIE4.SIE4_CHARSET));

        assertThatThrownBy(() -> SIE4.parse(stream))
                .isInstanceOf(SIE4Exception.class)
                .hasMessageContaining("Unclosed VER block");
    }

    @Test
    void parse_blankLines_shouldBeSkipped() {
        String input = "#FLAGGA 0\n\n#FNAMN TestCompany\n\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes(SIE4.SIE4_CHARSET));

        List<SIE4Item> items = SIE4.parse(stream).getItems();

        assertThat(items).hasSize(2);
        assertThat(items.get(0)).isEqualTo(SIE4Item.Flagga.UNSET);
        assertThat(items.get(1)).isEqualTo(new SIE4Item.Fnamn("TestCompany"));
    }

    @Test
    void parse_inputStream_shouldNotCloseStream() {
        var closed = new boolean[]{false};
        InputStream trackingStream = new ByteArrayInputStream("#FLAGGA 0\n".getBytes(SIE4.SIE4_CHARSET)) {
            @Override
            public void close() throws IOException {
                closed[0] = true;
                super.close();
            }
        };

        SIE4.parse(trackingStream);

        assertThat(closed[0]).isFalse();
    }

    @Test
    void write_validationFailure_shouldNotTruncateExistingFile() throws IOException {
        Path existingFile = tempDir.resolve("existing.se");
        Files.writeString(existingFile, "original content");

        List<SIE4Item> invalidItems = List.of(new SIE4Item.Flagga(0)); // missing mandatory items for SIE4E

        assertThatThrownBy(() -> SIE4.write(existingFile.toFile(), invalidItems))
                .isInstanceOf(SIE4Exception.class);
        assertThat(Files.readString(existingFile)).isEqualTo("original content");
    }

    @Test
    void write_ioErrorDuringWrite_shouldThrow() {
        OutputStream failingStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Simulated disk full");
            }
        };
        List<SIE4Item> items = List.of(new SIE4Item.Flagga(0));

        assertThatThrownBy(() -> SIE4.write(failingStream, items, SIE4.WriteOptions.SKIP_VALIDATION))
                .isInstanceOf(UncheckedIOException.class);
    }

    @Test
    void write_atomicWrite_noTempFilesLeftAfterMoveFails(@TempDir Path tempDir) throws IOException {
        // Destination is a directory — Files.move will fail, temp file must be cleaned up
        Path destination = tempDir.resolve("subdir");
        Files.createDirectory(destination);

        List<SIE4Item> items = SIE4.parse(sie4SampleFile).getItems();

        assertThatThrownBy(() -> SIE4.write(destination.toFile(), items, SIE4.WriteOptions.SKIP_VALIDATION))
                .isInstanceOf(UncheckedIOException.class);

        try (var stream = Files.list(tempDir)) {
            assertThat(stream.filter(p -> p.getFileName().toString().endsWith(".tmp"))).isEmpty();
        }
    }

    @Test
    void validate_skipValidation_returnsEmptyList() {
        List<SIE4Item> incompleteItems = List.of(SIE4Item.Flagga.UNSET); // missing mandatory items

        List<ValidationError> errors = SIE4.validate(incompleteItems, SIE4.WriteOptions.SKIP_VALIDATION);

        assertThat(errors).isEmpty();
    }

    @Test
    void validate_sie4Document_returnsErrors() {
        SIE4Document doc = SIE4Document.builder().fnamn("Test").build();

        List<ValidationError> errors = SIE4.validate(doc);

        assertThat(errors).isNotEmpty();
    }

    @Test
    void validate_invalidItems_returnsErrors() {
        List<SIE4Item> items = List.of(SIE4Item.Flagga.UNSET);

        List<ValidationError> errors = SIE4.validate(items);

        assertThat(errors).isNotEmpty();
        assertThat(errors).hasAtLeastOneElementOfType(MissingMandatoryItems.class);
    }

    @Test
    void validate_validSie4iItems_returnsNoErrors() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        List<ValidationError> errors = SIE4.validate(doc.getItems());

        assertThat(errors).isEmpty();
    }

    @Test
    void validate_sie4iMode_rejectsBalanceItems() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        List<ValidationError> errors = SIE4.validate(doc.getItems(), SIE4.WriteOptions.SIE4I);

        assertThat(errors).isNotEmpty();
    }

    @Test
    void parse_nullPath_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> SIE4.parse((Path) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void parse_nullFile_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> SIE4.parse((java.io.File) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void parse_nullInputStream_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> SIE4.parse((InputStream) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void write_nullPath_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> SIE4.write((Path) null, List.of()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void write_nullOutputStream_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> SIE4.write((OutputStream) null, List.of()))
                .isInstanceOf(NullPointerException.class);
    }
}
