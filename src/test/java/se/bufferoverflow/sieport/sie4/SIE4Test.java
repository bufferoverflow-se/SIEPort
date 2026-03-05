package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.bufferoverflow.sieport.sie4.validator.ValidationError.MissingMandatoryItems;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
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
        List<SIE4Item> items = SIE4.parse(sie4Sample).items();

        assertThat(items).hasSize(2160);
        assertThat(items.stream()).filteredOn(it -> it instanceof SIE4Item.Ver).hasSize(295);
    }

    @Test
    void writeFile() {
        List<SIE4Item> items = SIE4.parse(sie4SampleFile).items();
        Path outputFile = tempDir.resolve(UUID.randomUUID() + ".se");
        SIE4.write(outputFile, items);

        assertThat(outputFile)
                .exists()
                .content()
                .startsWith("#FLAGGA 0");
    }

    @Test
    void readAndWriteSample() {
        List<SIE4Item> parsedItems = SIE4.parse(sie4SampleFile).items();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4.write(baos, parsedItems);

        // Write sorts items by SIE4ItemType ordinal; re-parse and compare item sets
        List<SIE4Item> reparsedItems = SIE4.parse(new ByteArrayInputStream(baos.toByteArray())).items();
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

        List<SIE4Item> items = SIE4.parse(stream).items();

        assertThat(items).hasSize(2);
        assertThat(items.get(0)).isEqualTo(SIE4Item.Flagga.UNSET);
        assertThat(items.get(1)).isEqualTo(new SIE4Item.Fnamn("TestCompany"));
    }

    @Test
    void parse_transactionAtTopLevel_shouldBeIgnored() {
        String input = "#FLAGGA 0\n#TRANS 1930 {} 100.00\n#RTRANS 1920 {} -100.00\n#FNAMN TestCompany\n";
        InputStream stream = new ByteArrayInputStream(input.getBytes(SIE4.SIE4_CHARSET));

        List<SIE4Item> items = SIE4.parse(stream).items();

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
}
