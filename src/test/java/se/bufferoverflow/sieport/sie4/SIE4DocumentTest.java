package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Flagga.UNSET;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Sietyp.SIE_4;

class SIE4DocumentTest {
    private Path sie4SampleFile;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void newDocument_hasStandardDefaults() {
        SIE4Document doc = SIE4Document.newDocument().build();

        IdentificationItems result = doc.getIdentificationItems();

        assertEquals(UNSET.flag(), result.flag());
        assertThat(result.generatedAt()).isToday();
        assertEquals(SIE_4.typeNo(), result.sieType());
        assertNull(result.programWithVersion());
        assertNull(result.comment());
        assertNull(result.kptyp());
        assertNull(result.currencyCode());
        assertNull(result.taxYear());
        assertTrue(result.periods().isEmpty());
        assertNull(result.contactPerson());
        assertNull(result.address());
        assertNull(result.phoneNumber());
        assertNull(result.sniCode());
        assertNull(result.companyType());
        assertNull(result.companyName());
        assertNull(result.organizationNumber());
    }

    @Test
    void builder_hasNoDefaults() {
        SIE4Document doc = SIE4Document.builder().build();

        assertNull(doc.getFlagga());
        assertNull(doc.getFormat());
        assertNull(doc.getGen());
        assertNull(doc.getSietyp());
    }

    @Test
    void getIdentificationItems_fromSample() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        IdentificationItems result = doc.getIdentificationItems();

        var expectedPeriods = List.of(
            new FinancialYear(LocalDate.of(2021,1,1), LocalDate.of(2021, 12, 31)),
            new FinancialYear(LocalDate.of(2020,1,1), LocalDate.of(2020, 12, 31))
        );

        assertEquals(UNSET.flag(), result.flag());
        assertEquals("Visma Administration 2000 med Visma Integration, 2022.2", result.programWithVersion());
        assertEquals(LocalDate.of(2023, 8, 22), result.generatedAt());
        assertEquals(4, result.sieType());
        assertNull(result.comment());
        assertEquals("EUBAS97", result.kptyp());
        assertEquals("SEK", result.currencyCode());
        assertEquals(2022, result.taxYear());
        assertEquals(expectedPeriods, result.periods());
        assertEquals("Siw Eriksson", result.contactPerson());
        assertEquals("Box 1 123 45 STORSTAD", result.address());
        assertEquals("012-34 56 78", result.phoneNumber());
        assertNull(result.sniCode());
        assertNull(result.companyType());
        assertEquals("Övningsbolaget AB", result.companyName());
        assertEquals("555555-5555", result.organizationNumber());
    }

    @Test
    void getItems_fromSample() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        assertThat(doc.getItems()).hasSize(2160);
    }

    @Test
    void getItems_byType_returnsMatchingItems() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        List<SIE4Item.Ver> items = doc.getItems(SIE4Item.Ver.class);

        assertThat(items).hasSize(295);
    }

    @Test
    void getItem_returnsPresentItem() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        Optional<SIE4Item.OrgNr> item = doc.getItem(SIE4Item.OrgNr.class);

        assertThat(item).isPresent();
    }

    @Test
    void getItem_returnsEmptyForAbsentItem() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        Optional<SIE4Item.Omfattn> item = doc.getItem(SIE4Item.Omfattn.class);

        assertThat(item).isNotPresent();
    }

    @Test
    void getItem_throwsIfMultipleFound() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        assertThrows(SIE4Exception.class, () -> doc.getItem(SIE4Item.Ver.class));
    }

    @Test
    void getItem_throwsWithFriendlyLabelName() {
        SIE4Exception ex = assertThrows(SIE4Exception.class,
                () -> SIE4Document.from(List.of(new SIE4Item.Flagga(0), new SIE4Item.Flagga(1))));
        assertThat(ex.getMessage()).contains("#FLAGGA").contains("2");
    }
}
