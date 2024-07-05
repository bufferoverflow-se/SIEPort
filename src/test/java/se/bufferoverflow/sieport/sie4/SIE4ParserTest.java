package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SIE4ParserTest {

    private InputStream sie4Sample;

    @BeforeEach
    void setUp() {
        sie4Sample = SIE4ParserTest.class.getClassLoader().getResourceAsStream("./SIE4-sample.SE");
    }

    @Test
    void readSample() {
        List<FileItem> fileItems = SIE4Parser.parse(sie4Sample);

        assertThat(fileItems).hasSize(2160);
        assertThat(fileItems.stream()).filteredOn(it -> it instanceof FileItem.Ver).hasSize(295);
    }
}
