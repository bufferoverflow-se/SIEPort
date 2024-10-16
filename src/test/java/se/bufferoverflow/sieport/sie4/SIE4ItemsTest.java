package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SIE4ItemsTest {
    private Path sie4SampleFile;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void getAllItems() {
        SIE4Items content = SIE4.parse(sie4SampleFile);
        List<SIE4Item.Ver> items = content.getItems(SIE4Item.Ver.class);

        assertThat(items).hasSize(295);
    }

    @Test
    void getSingleItem() {
        SIE4Items content = SIE4.parse(sie4SampleFile);
        Optional<SIE4Item.OrgNr> item = content.getItem(SIE4Item.OrgNr.class);

        assertThat(item).isPresent();
    }

    @Test
    void getSingleNonExistingItem() {
        SIE4Items content = SIE4.parse(sie4SampleFile);
        Optional<SIE4Item.Omfattn> item = content.getItem(SIE4Item.Omfattn.class);

        assertThat(item).isNotPresent();
    }

    @Test
    void getSingleItemThrowsIfMany() {
        SIE4Items content = SIE4.parse(sie4SampleFile);
        assertThrows(SIE4Exception.class, () -> content.getItem(SIE4Item.Ver.class));
    }
}
