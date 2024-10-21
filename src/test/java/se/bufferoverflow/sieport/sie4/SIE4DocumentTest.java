package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Flagga.SET;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Flagga.UNSET;

class SIE4DocumentTest {
    private Path sie4SampleFile;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void getIdentificationItems_whenItemsAreNull_ShouldNotFail() {
        SIE4Document doc = SIE4Document.builder().build();

        IdentificationItems result = doc.getIdentificationItems();

        // Check result
        assertEquals(UNSET.flag(), result.flag());
        assertNull(result.programWithVersion());
        assertThat(result.generatedAt()).isToday();
        assertNull(result.sieType());
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
    void getIdentificationItems_fromSample() {
        SIE4Items content = SIE4.parse(sie4SampleFile);
        SIE4Document doc = new SIE4Document(content);

        IdentificationItems result = doc.getIdentificationItems();

        var expectedPeriods = List.of(
            new FinancialYear(LocalDate.of(2021,1,1), LocalDate.of(2021, 12, 31)),
            new FinancialYear(LocalDate.of(2020,1,1), LocalDate.of(2020, 12, 31))
        );

        assertEquals(SET.flag(), result.flag());
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
    void getItems() {
        SIE4Items content = SIE4.parse(sie4SampleFile);

        SIE4Document doc = new SIE4Document(content);

        assertThat(doc.getItems()).containsExactlyInAnyOrderElementsOf(content.items());
    }
}
