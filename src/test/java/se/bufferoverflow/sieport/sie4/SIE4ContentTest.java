package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
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

    @Test
    void shouldGetFileInfoFromContent() {
        SIE4Content content = SIE4.parse(sie4SampleFile);
        SIE4Content.FileInfo fileInfo = content.getFileInfo();

        assertThat(fileInfo).isEqualTo(new SIE4Content.FileInfo(
            new SIE4Item.Flagga(1),
            new SIE4Item.Program("Visma Administration 2000 med Visma Integration", "2022.2"),
            new SIE4Item.Gen(LocalDate.of(2023, 8, 22), Optional.empty()),
            new SIE4Item.Kptyp("EUBAS97"),
            new SIE4Item.Valuta("SEK"),
            List.of(
                new SIE4Item.Rar(YearNumber.CURRENT_YEAR, LocalDate.of(2021,1,1), LocalDate.of(2021, 12, 31)),
                new SIE4Item.Rar(YearNumber.PREV_YEAR, LocalDate.of(2020,1,1), LocalDate.of(2020, 12, 31))
            )
        ));
    }
}
