package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        List<SIE4Item> items = SIE4.parse(sie4Sample);

        assertThat(items).hasSize(2160);
        assertThat(items.stream()).filteredOn(it -> it instanceof SIE4Item.Ver).hasSize(295);
    }

    @Test
    void writeFile() {
        List<SIE4Item> items = SIE4.parse(sie4SampleFile);
        Path outputFile = tempDir.resolve(UUID.randomUUID() + ".se");
        SIE4.write(outputFile, items);

        assertThat(outputFile)
                .exists()
                .content()
                .startsWith("#FLAGGA 1");
    }

    @Test
    void readAndWriteSample() throws IOException {
        String expectedOutput = Files.readString(sie4SampleFile, SIE4.SIE4_CHARSET);
        List<SIE4Item> parsedItems = SIE4.parse(sie4SampleFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4.write(baos, parsedItems);
        String output = baos.toString(SIE4.SIE4_CHARSET);

        // Both LF and CRLF are permitted according to SIE spec
        assertThat(output).isEqualToIgnoringNewLines(expectedOutput);
    }
}
