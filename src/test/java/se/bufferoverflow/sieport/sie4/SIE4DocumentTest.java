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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Flagga.UNSET;
import static se.bufferoverflow.sieport.sie4.SIE4Item.Sietyp.SIE_4;
import static se.bufferoverflow.sieport.sie4.SIE4.FileOptions.SIE4I;
import static se.bufferoverflow.sieport.sie4.YearNumber.CURRENT_YEAR;
import static se.bufferoverflow.sieport.sie4.YearNumber.PREV_YEAR;

class SIE4DocumentTest {
    private Path sie4SampleFile;

    @BeforeEach
    void setUp() throws URISyntaxException {
        sie4SampleFile = Path.of(SIE4Test.class.getClassLoader().getResource("./SIE4-sample.SE").toURI());
    }

    @Test
    void defaultBuilder_hasStandardDefaults() {
        SIE4Document doc = SIE4Document.defaultBuilder().build();

        IdentificationItems result = doc.getIdentificationItems();

        assertThat(result.flag()).isEqualTo(UNSET.flag());
        assertThat(result.generatedAt()).isToday();
        assertThat(result.sieType()).isEqualTo(SIE_4.typeNo());
        assertThat(result.programWithVersion()).isNull();
        assertThat(result.comment()).isEmpty();
        assertThat(result.kptyp()).isEmpty();
        assertThat(result.currencyCode()).isEmpty();
        assertThat(result.taxYear()).isEmpty();
        assertTrue(result.financialYears().isEmpty());
        assertThat(result.contactPerson()).isEmpty();
        assertThat(result.address()).isEmpty();
        assertThat(result.phoneNumber()).isEmpty();
        assertThat(result.sniCode()).isEmpty();
        assertThat(result.companyType()).isEmpty();
        assertThat(result.companyName()).isNull();
        assertThat(result.organizationNumber()).isEmpty();
    }

    @Test
    void builder_hasNoDefaults() {
        SIE4Document doc = SIE4Document.builder().build();

        assertThat(doc.getFlagga()).isNull();
        assertThat(doc.getFormat()).isNull();
        assertThat(doc.getGen()).isNull();
        assertThat(doc.getSietyp()).isNull();
    }

    @Test
    void getIdentificationItems_fromSample() {
        SIE4Document doc = SIE4.parse(sie4SampleFile);

        IdentificationItems result = doc.getIdentificationItems();

        var expectedPeriods = List.of(
            new FinancialYear(CURRENT_YEAR, LocalDate.of(2021,1,1), LocalDate.of(2021, 12, 31)),
            new FinancialYear(PREV_YEAR, LocalDate.of(2020,1,1), LocalDate.of(2020, 12, 31))
        );

        assertThat(result.flag()).isEqualTo(UNSET.flag());
        assertThat(result.programWithVersion()).isEqualTo("Visma Administration 2000 med Visma Integration, 2022.2");
        assertThat(result.generatedAt()).isEqualTo(LocalDate.of(2023, 8, 22));
        assertThat(result.sieType()).isEqualTo(4);
        assertThat(result.comment()).isEmpty();
        assertThat(result.kptyp()).hasValue("EUBAS97");
        assertThat(result.currencyCode()).hasValue("SEK");
        assertThat(result.taxYear()).hasValue(2022);
        assertThat(result.financialYears()).isEqualTo(expectedPeriods);
        assertThat(result.contactPerson()).hasValue("Siw Eriksson");
        assertThat(result.address()).hasValue("Box 1 123 45 STORSTAD");
        assertThat(result.phoneNumber()).hasValue("012-34 56 78");
        assertThat(result.sniCode()).isEmpty();
        assertThat(result.companyType()).isEmpty();
        assertThat(result.companyName()).isEqualTo("Övningsbolaget AB");
        assertThat(result.organizationNumber()).hasValue("555555-5555");
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
    void builder_addKonto_accumulates() {
        SIE4Item.Konto k1 = new SIE4Item.Konto(1910, "Cash");
        SIE4Item.Konto k2 = new SIE4Item.Konto(1920, "Bank");
        SIE4Item.Konto k3 = new SIE4Item.Konto(1930, "Bank");

        SIE4Document doc = SIE4Document.builder()
                .konto(List.of(k1, k2))
                .addKonto(k3)
                .build();

        assertThat(doc.getKonto()).containsExactly(k1, k2, k3);
    }

    @Test
    void toBuilder_preservesAllFieldsAndAllowsOverride() {
        SIE4Document original = SIE4.parse(sie4SampleFile);
        SIE4Item.Fnamn newFnamn = new SIE4Item.Fnamn("Changed Company");

        SIE4Document copy = original.toBuilder().fnamn(newFnamn).build();

        assertThat(copy.getFnamn()).isEqualTo(newFnamn);
        assertThat(copy.getFlagga()).isEqualTo(original.getFlagga());
        assertThat(copy.getVer()).isEqualTo(original.getVer());
        assertThat(copy.getKonto()).isEqualTo(original.getKonto());
    }

    @Test
    void getItem_throwsWithFriendlyLabelName() {
        SIE4Exception ex = assertThrows(SIE4Exception.class,
                () -> SIE4Document.from(List.of(new SIE4Item.Flagga(0), new SIE4Item.Flagga(1))));
        assertThat(ex.getMessage()).contains("#FLAGGA").contains("2");
    }

    @Test
    void defaultBuilder_hasStandardDefaults_flagIsSet() {
        SIE4Document doc = SIE4Document.defaultBuilder().build();
        assertThat(doc.getFlagga()).isNotNull();
    }

    @Test
    void getFlagga_returnsNull_whenAbsent() {
        SIE4Item.Flagga flagga = SIE4Document.builder().build().getFlagga();
        assertThat(flagga).isNull();
    }

    @Test
    void identificationItems_absentMandatoryFieldIsNull() {
        String companyName = SIE4Document.builder().build().getIdentificationItems().companyName();
        assertThat(companyName).isNull();
    }

    @Test
    void builder_stringOverloads_setCorrectFields() {
        SIE4Document doc = SIE4Document.builder()
                .prosa("a comment")
                .valuta("EUR")
                .fnr("FNR123")
                .kptyp("BAS2010")
                .build();
        assertThat(doc.getProsa().orElseThrow().comment()).isEqualTo("a comment");
        assertThat(doc.getValuta().orElseThrow().currencyCode()).isEqualTo("EUR");
        assertThat(doc.getFnr().orElseThrow().companyId()).isEqualTo("FNR123");
        assertThat(doc.getKptyp().orElseThrow().type()).isEqualTo("BAS2010");
    }

    @Test
    void builder_addVer_varargs_accumulates() {
        var tx = List.<SIE4Item.Transaction>of(
                SIE4Item.Transaction.Trans.of(1910, java.math.BigDecimal.ONE),
                SIE4Item.Transaction.Trans.of(1920, java.math.BigDecimal.ONE.negate()));
        SIE4Item.Ver v1 = SIE4Item.Ver.of(LocalDate.now(), "v1", tx);
        SIE4Item.Ver v2 = SIE4Item.Ver.of(LocalDate.now(), "v2", tx);
        SIE4Item.Ver v3 = SIE4Item.Ver.of(LocalDate.now(), "v3", tx);

        SIE4Document doc = SIE4Document.builder().addVer(v1, v2, v3).build();

        assertThat(doc.getVer()).containsExactly(v1, v2, v3);
    }

    @Test
    void from_buildsDocumentFromItemList() {
        List<SIE4Item> items = List.of(new SIE4Item.Flagga(0), new SIE4Item.Fnamn("Acme AB"));
        SIE4Document doc = SIE4Document.from(items);
        assertThat(doc.getFlagga()).isNotNull();
        assertThat(doc.getFnamn().companyName()).isEqualTo("Acme AB");
    }

    @Test
    void buildAndValidate_throwsWhenMandatoryItemsMissing() {
        assertThrows(SIE4Exception.class, () ->
                SIE4Document.builder().buildAndValidate());
    }

    @Test
    void buildAndValidate_returnsDocumentWhenValid() {
        SIE4Document doc = SIE4Document.builder()
                .flagga(UNSET)
                .program(new SIE4Item.Program("Test", "1.0"))
                .format(SIE4Item.Format.pc8())
                .gen(SIE4Item.Gen.now())
                .sietyp(SIE4Item.Sietyp.SIE_4)
                .fnamn("Test Company")
                .buildAndValidate(SIE4I);
        assertThat(doc.getFnamn().companyName()).isEqualTo("Test Company");
    }

    @Test
    void buildAndValidate_throwsWhenForbiddenItemsPresent() {
        assertThrows(SIE4Exception.class, () ->
                SIE4Document.builder()
                        .flagga(UNSET)
                        .program(new SIE4Item.Program("Test", "1.0"))
                        .format(SIE4Item.Format.pc8())
                        .gen(SIE4Item.Gen.now())
                        .sietyp(SIE4Item.Sietyp.SIE_4)
                        .fnamn("Test Company")
                        .addIb(new SIE4Item.Ib(YearNumber.CURRENT_YEAR, 1000, java.math.BigDecimal.ZERO, null))
                        .buildAndValidate(SIE4I));
    }
}
