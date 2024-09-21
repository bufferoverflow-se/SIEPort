package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SIE4WriterTest {
    private Path sie4SampleFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4ParserTest.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void writeFile() {
        List<SIE4Item> items = SIE4Parser.parse(sie4SampleFile);
        Path outputFile = tempDir.resolve(UUID.randomUUID() + ".se");
        SIE4Writer.write(outputFile, items);

        assertThat(outputFile)
                .exists()
                .content()
                .startsWith("#FLAGGA 1");
    }

    @Test
    void readAndWriteSample() throws IOException {
        String expectedOutput = Files.readString(sie4SampleFile, Constants.SIE4_CHARSET);
        List<SIE4Item> parsedItems = SIE4Parser.parse(sie4SampleFile);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SIE4Writer.write(baos, parsedItems);
        String output = baos.toString(Constants.SIE4_CHARSET);

        // Both LF and CRLF are permitted according to SIE spec
        assertThat(output).isEqualToIgnoringNewLines(expectedOutput);
    }
}
