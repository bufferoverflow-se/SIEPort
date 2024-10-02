package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SIE4ContentTest {
    private Path sie4SampleFile;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void getAllItems() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        List<SIE4Item.Rar> items = content.getItems(SIE4Item.Rar.class);

        assertThat(items).hasSize(2);
    }

    @Test
    void getSingleItem() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        Optional<SIE4Item.OrgNr> item = content.getItem(SIE4Item.OrgNr.class);

        assertThat(item).isPresent();
    }

    @Test
    void getSingleNonExistingItem() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        Optional<SIE4Item.Omfattn> item = content.getItem(SIE4Item.Omfattn.class);

        assertThat(item).isNotPresent();
    }

    @Test
    void getSingleItemThrowsIfMany() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        assertThrows(SIE4Exception.class, () -> content.getItem(SIE4Item.Ver.class));
    }

    @Test
    void shouldGetAllVerificationsFromContent() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        List<SIE4Item.Ver> verifications = content.getVerifications();

        assertThat(verifications).hasSize(295);
    }

    @Test
    void shouldGetOrgInfoFromContent() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        SIE4Content.OrgInfo orgInfo = content.getOrgInfo();

        assertThat(orgInfo).isEqualTo(new SIE4Content.OrgInfo(
                "Siw Eriksson",
                "Box 1 123 45 STORSTAD",
                "012-34 56 78",
                null,
                "Övningsbolaget AB",
                null,
                "555555-5555"
        ));
    }
}
