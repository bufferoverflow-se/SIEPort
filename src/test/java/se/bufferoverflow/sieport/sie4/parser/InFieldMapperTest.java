package se.bufferoverflow.sieport.sie4.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.bufferoverflow.sieport.sie4.CompanyType;
import se.bufferoverflow.sieport.sie4.ObjectReference;
import se.bufferoverflow.sieport.sie4.Period;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InFieldMapperTest {

    @Test
    void toModel_address() {
        String addressLine = "#ADRESS \"Sven Svensson\" \"Box 21\" \"211 20 MALMÖ\" \"040-123 45\"";
        SIE4Item.Adress expectedModel = new SIE4Item.Adress(
                "Sven Svensson",
                "Box 21",
                "211 20 MALMÖ",
                "040-123 45");
        SIE4Item model = InFieldMapper.toModel(addressLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_bkod() {
        String bkodLine = "#BKOD 82300";
        SIE4Item.Bkod expectedModel = new SIE4Item.Bkod(82300);
        SIE4Item model = InFieldMapper.toModel(bkodLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_dim() {
        String dimLine = "#DIM 2 Project";
        SIE4Item.Dim expectedModel = new SIE4Item.Dim(2, "Project");
        SIE4Item model = InFieldMapper.toModel(dimLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_enhet() {
        String enhetLine = "#ENHET 4010 litre";
        SIE4Item.Enhet expectedModel = new SIE4Item.Enhet(4010, "litre");
        SIE4Item model = InFieldMapper.toModel(enhetLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_flagga() {
        String flaggaLine = "#FLAGGA 0";
        SIE4Item model = InFieldMapper.toModel(flaggaLine);
        assertThat(model).isEqualTo(SIE4Item.Flagga.UNSET);
    }

    @Test
    void toModel_fnamn() {
        String fnamnLine = "#FNAMN \"ACME Corporation\"";
        SIE4Item.Fnamn expectedModel = new SIE4Item.Fnamn("ACME Corporation");
        SIE4Item model = InFieldMapper.toModel(fnamnLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_fnr() {
        String fnrLine = "#FNR Kalles";
        SIE4Item.Fnr expectedModel = new SIE4Item.Fnr("Kalles");
        SIE4Item model = InFieldMapper.toModel(fnrLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_format() {
        String formatLine = "#FORMAT PC8";
        SIE4Item.Format expectedModel = SIE4Item.Format.pc8();
        SIE4Item model = InFieldMapper.toModel(formatLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ftyp() {
        String ftypLine = "#FTYP AB";
        SIE4Item.Ftyp expectedModel = new SIE4Item.Ftyp(CompanyType.AB);
        SIE4Item model = InFieldMapper.toModel(ftypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_gen() {
        String genLine = "#GEN 20240508 AN";
        SIE4Item.Gen expectedModel = new SIE4Item.Gen(
                LocalDate.of(2024, 5, 8),
                Optional.of("AN"));
        SIE4Item model = InFieldMapper.toModel(genLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ib() {
        String ibLine = "#IB 0 4010 23780.78";
        SIE4Item.Ib expectedModel = new SIE4Item.Ib(YearNumber.of(0),
                4010,
                new BigDecimal("23780.78"),
                Optional.empty());
        SIE4Item model = InFieldMapper.toModel(ibLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_konto() {
        String kontoLine = "#KONTO 1510 \"Accounts receivable\"";
        SIE4Item.Konto expectedModel = new SIE4Item.Konto(1510, "Accounts receivable");
        SIE4Item model = InFieldMapper.toModel(kontoLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_kptyp() {
        String kptypLine = "#KPTYP EUBAS97";
        SIE4Item.Kptyp expectedModel = new SIE4Item.Kptyp("EUBAS97");
        SIE4Item model = InFieldMapper.toModel(kptypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ktyp() {
        String ktypLine = "#KTYP 1510 T";
        SIE4Item.Ktyp expectedModel = new SIE4Item.Ktyp(1510, SIE4Item.Ktyp.AccountType.T);
        SIE4Item model = InFieldMapper.toModel(ktypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_objekt() {
        String objektLine = "#OBJEKT 1 \"0123\" \"Service department\"";
        SIE4Item.Objekt expectedModel = new SIE4Item.Objekt(1, "0123", "Service department");
        SIE4Item model = InFieldMapper.toModel(objektLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_oib() {
        String oibLine = "#OIB 0 1520 {8 \"12345\"} 12345.00";
        SIE4Item.Oib expectedModel = new SIE4Item.Oib(
                YearNumber.of(0),
                1520,
                ObjectReference.of(8, "12345"),
                new BigDecimal("12345.00"),
                Optional.empty());
        SIE4Item model = InFieldMapper.toModel(oibLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_omfattn() {
        String omfattnLine = "#OMFATTN 20080630";
        SIE4Item.Omfattn expectedModel = new SIE4Item.Omfattn(LocalDate.of(2008, 6, 30));
        SIE4Item model = InFieldMapper.toModel(omfattnLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_orgnr() {
        String orgnrLine = "#ORGNR 212000-0340 1";
        SIE4Item.OrgNr expectedModel = new SIE4Item.OrgNr("212000-0340", Optional.of(1), Optional.empty());
        SIE4Item model = InFieldMapper.toModel(orgnrLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_oub() {
        String oubLine = "#OUB 0 1520 {8 \"12345\"} 12345.67";
        SIE4Item.Oub expectedModel = new SIE4Item.Oub(
                YearNumber.of(0),
                1520,
                ObjectReference.of(8, "12345"),
                new BigDecimal("12345.67"),
                Optional.empty());
        SIE4Item model = InFieldMapper.toModel(oubLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("pbudgetTestData")
    void toModel_pbudget(String pbudgetLine, SIE4Item.Pbudget expectedModel) {
        SIE4Item model = InFieldMapper.toModel(pbudgetLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> pbudgetTestData() {
        return Stream.of(
                Arguments.of("#PBUDGET 0 200801 3011 {} -1243.50 -415", new SIE4Item.Pbudget(
                        YearNumber.of(0),
                        Period.of(2008, 1),
                        3011,
                        Optional.empty(),
                        new BigDecimal("-1243.50"),
                        Optional.of(new BigDecimal(-415)))),

                Arguments.of("#PBUDGET -1 200701 5010 {1 \"0123\"} 3411.80", new SIE4Item.Pbudget(
                        YearNumber.of(-1),
                        Period.of(2007, 1),
                        5010,
                        Optional.of(ObjectReference.of(1, "0123")),
                        new BigDecimal("3411.80"),
                        Optional.empty()))
        );
    }

    @Test
    void toModel_program() {
        String programLine = "#PROGRAM \"Visma Compact\" 5.1";
        SIE4Item.Program expectedModel = new SIE4Item.Program("Visma Compact", "5.1");
        SIE4Item model = InFieldMapper.toModel(programLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_prosa() {
        String prosaLine = "#PROSA \"Exported using Visma Compact 080512\"";
        SIE4Item.Prosa expectedModel = new SIE4Item.Prosa("Exported using Visma Compact 080512");
        SIE4Item model = InFieldMapper.toModel(prosaLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("psaldoTestData")
    void toModel_psaldo(String psaldoLine, SIE4Item.Psaldo expectedModel) {
        SIE4Item model = InFieldMapper.toModel(psaldoLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> psaldoTestData() {
        return Stream.of(
                Arguments.of(
                        "#PSALDO 0 200808 1910 {} 1243.50 123",
                        new SIE4Item.Psaldo(
                                YearNumber.of(0),
                                Period.of(2008, 8),
                                1910,
                                Optional.empty(),
                                new BigDecimal("1243.50"),
                                Optional.of(new BigDecimal(123)))
                ),
                Arguments.of(
                        "#PSALDO 0 200809 5010 {1 \"0123\"} 3411.80",
                        new SIE4Item.Psaldo(
                                YearNumber.of(0),
                                Period.of(2008, 9),
                                5010,
                                Optional.of(ObjectReference.of(1, "0123")),
                                new BigDecimal("3411.80"),
                                Optional.empty())
                )
        );
    }

    @Test
    void toModel_rar() {
        String rarLine = "#RAR -1 20110101 20111231";
        SIE4Item.Rar expectedModel = new SIE4Item.Rar(
                YearNumber.of(-1),
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 12, 31));
        SIE4Item model = InFieldMapper.toModel(rarLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_res() {
        String resLine = "#RES 0 3011 -23780.78";
        SIE4Item.Res expectedModel = new SIE4Item.Res(YearNumber.of(0), 3011, new BigDecimal("-23780.78"), Optional.empty());
        SIE4Item model = InFieldMapper.toModel(resLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_sietyp() {
        String sietypLine = "#SIETYP 4";
        SIE4Item.Sietyp expectedModel = new SIE4Item.Sietyp(4);
        SIE4Item model = InFieldMapper.toModel(sietypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_sru() {
        String sruLine = "#SRU 1520 9420";
        SIE4Item.Sru expectedModel = new SIE4Item.Sru(1520, 9420);
        SIE4Item model = InFieldMapper.toModel(sruLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_taxar() {
        String taxarLine = "#TAXAR 2000";
        SIE4Item.Taxar expectedModel = new SIE4Item.Taxar(2000);
        SIE4Item model = InFieldMapper.toModel(taxarLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("transTestData")
    void toModel_trans(String transLine, SIE4Item.Transaction.Trans expectedModel) {
        SIE4Item model = InFieldMapper.toModel(transLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> transTestData() {
        return Stream.of(
                Arguments.of(
                        "#TRANS 1510 {} 150038.05",
                        new SIE4Item.Transaction.Trans(
                                1510,
                                new BigDecimal("150038.05"),
                                List.of(),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty()
                        )
                ),
                Arguments.of(
                        "#TRANS 1510 {1 Nord} -16875.00 20211002 \"Faktnr: 663, Namn: Billeverantören AB\"",
                        new SIE4Item.Transaction.Trans(
                                1510,
                                new BigDecimal("-16875.00"),
                                List.of(ObjectReference.of(1, "Nord")),
                                Optional.of(LocalDate.of(2021, 10, 2)),
                                Optional.of("Faktnr: 663, Namn: Billeverantören AB"),
                                Optional.empty(),
                                Optional.empty()
                        )
                ),
                Arguments.of(
                        "#TRANS 7010 {1 Nord 2 \"0010\"} 27564.20 20210325 \"\" 216",
                        new SIE4Item.Transaction.Trans(
                                7010,
                                new BigDecimal("27564.20"),
                                List.of(ObjectReference.of(1, "Nord"), ObjectReference.of(2, "0010")),
                                Optional.of(LocalDate.of(2021, 3, 25)),
                                Optional.empty(),
                                Optional.of(new BigDecimal(216)),
                                Optional.empty()
                        )
                )
        );
    }

    @Test
    void toModel_rtrans() {
        String rtransLine = "#RTRANS 1520 {} 500.00";
        SIE4Item.Transaction.Rtrans expectedModel = new SIE4Item.Transaction.Rtrans(
                1520,
                new BigDecimal("500.00"),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        SIE4Item model = InFieldMapper.toModel(rtransLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_btrans() {
        String btransLine = "#BTRANS 1520 {} 500.00";
        SIE4Item.Transaction.Btrans expectedModel = new SIE4Item.Transaction.Btrans(
                1520,
                new BigDecimal("500.00"),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        SIE4Item model = InFieldMapper.toModel(btransLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ub() {
        String ubLine = "#UB 0 2440 -2380.39";
        SIE4Item.Ub expectedModel = new SIE4Item.Ub(YearNumber.of(0), 2440, new BigDecimal("-2380.39"), Optional.empty());
        SIE4Item model = InFieldMapper.toModel(ubLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_underdim() {
        String underdimLine = "#UNDERDIM 21 \"Sub-department\" 1";
        SIE4Item.Underdim expectedModel = new SIE4Item.Underdim(21, "Sub-department", 1);
        SIE4Item model = InFieldMapper.toModel(underdimLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_valuta() {
        String valutaLine = "#VALUTA SEK";
        SIE4Item.Valuta expectedModel = new SIE4Item.Valuta("SEK");
        SIE4Item model = InFieldMapper.toModel(valutaLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("verTestData")
    void toModel_ver(List<String> verLine, SIE4Item.Ver expectedModel) {
        SIE4Item model = InFieldMapper.toModel(verLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> verTestData() {
        return Stream.of(
                Arguments.of(
                        """
                        #VER F 12 20211223 "Lönekörning: 2021-12-23 - Ordinarie lön" 20211130
                        #TRANS 1930 {} -10.00
                        #TRANS 1920 {} 10.00
                        """.lines().toList(),
                        new SIE4Item.Ver(
                                LocalDate.of(2021, 12, 23),
                                Optional.of("F"),
                                Optional.of("12"),
                                Optional.of("Lönekörning: 2021-12-23 - Ordinarie lön"),
                                Optional.of(LocalDate.of(2021, 11, 30)),
                                Optional.empty(),
                                List.of(
                                        SIE4Item.Transaction.Trans.of(1930, new BigDecimal("-10.00")),
                                        SIE4Item.Transaction.Trans.of(1920, new BigDecimal("10.00"))
                                ))
                ),
                Arguments.of("""
                        #VER "" "" 20081216 "Postage"
                        #TRANS 1930 {} -10.00
                        #TRANS 1920 {} 10.00
                        """.lines().toList(),
                        new SIE4Item.Ver(
                                LocalDate.of(2008, 12, 16),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.of("Postage"),
                                Optional.empty(),
                                Optional.empty(),
                                List.of(
                                        SIE4Item.Transaction.Trans.of(1930, new BigDecimal("-10.00")),
                                        SIE4Item.Transaction.Trans.of(1920, new BigDecimal("10.00"))
                                ))
                )
        );
    }

}
