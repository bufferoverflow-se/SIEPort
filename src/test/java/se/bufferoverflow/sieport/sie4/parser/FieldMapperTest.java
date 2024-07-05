package se.bufferoverflow.sieport.sie4.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.bufferoverflow.sieport.sie4.CompanyType;
import se.bufferoverflow.sieport.sie4.FileItem;
import se.bufferoverflow.sieport.sie4.ObjectReference;
import se.bufferoverflow.sieport.sie4.Period;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FieldMapperTest {

    @Test
    void toModel_address() {
        String addressLine = "#ADRESS \"Sven Svensson\" \"Box 21\" \"211 20 MALMÖ\" \"040-123 45\"";
        FileItem.Adress expectedModel = new FileItem.Adress(
                "Sven Svensson",
                "Box 21",
                "211 20 MALMÖ",
                "040-123 45");
        FileItem model = FieldMapper.toModel(addressLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_bkod() {
        String bkodLine = "#BKOD 82300";
        FileItem.Bkod expectedModel = new FileItem.Bkod(82300);
        FileItem model = FieldMapper.toModel(bkodLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_dim() {
        String dimLine = "#DIM 2 Project";
        FileItem.Dim expectedModel = new FileItem.Dim(2, "Project");
        FileItem model = FieldMapper.toModel(dimLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_enhet() {
        String enhetLine = "#ENHET 4010 litre";
        FileItem.Enhet expectedModel = new FileItem.Enhet(4010, "litre");
        FileItem model = FieldMapper.toModel(enhetLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_flagga() {
        String flaggaLine = "#FLAGGA 0";
        FileItem.Flagga expectedModel = new FileItem.Flagga(0);
        FileItem model = FieldMapper.toModel(flaggaLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_fnamn() {
        String fnamnLine = "#FNAMN \"ACME Corporation\"";
        FileItem.Fnamn expectedModel = new FileItem.Fnamn("ACME Corporation");
        FileItem model = FieldMapper.toModel(fnamnLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_fnr() {
        String fnrLine = "#FNR Kalles";
        FileItem.Fnr expectedModel = new FileItem.Fnr("Kalles");
        FileItem model = FieldMapper.toModel(fnrLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_format() {
        String formatLine = "#FORMAT PC8";
        FileItem.Format expectedModel = new FileItem.Format("PC8");
        FileItem model = FieldMapper.toModel(formatLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ftyp() {
        String ftypLine = "#FTYP AB";
        FileItem.Ftyp expectedModel = new FileItem.Ftyp(CompanyType.AB);
        FileItem model = FieldMapper.toModel(ftypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_gen() {
        String genLine = "#GEN 20240508 AN";
        FileItem.Gen expectedModel = new FileItem.Gen(
                LocalDate.of(2024, 5, 8),
                Optional.of("AN"));
        FileItem model = FieldMapper.toModel(genLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ib() {
        String ibLine = "#IB 0 4010 23780.78";
        FileItem.Ib expectedModel = new FileItem.Ib(YearNumber.of(0),
                4010,
                new BigDecimal("23780.78"),
                Optional.empty());
        FileItem model = FieldMapper.toModel(ibLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_konto() {
        String kontoLine = "#KONTO 1510 \"Accounts receivable\"";
        FileItem.Konto expectedModel = new FileItem.Konto(1510, "Accounts receivable");
        FileItem model = FieldMapper.toModel(kontoLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_kptyp() {
        String kptypLine = "#KPTYP EUBAS97";
        FileItem.Kptyp expectedModel = new FileItem.Kptyp("EUBAS97");
        FileItem model = FieldMapper.toModel(kptypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ktyp() {
        String ktypLine = "#KTYP 1510 T";
        FileItem.Ktyp expectedModel = new FileItem.Ktyp(1510, FileItem.Ktyp.AccountType.T);
        FileItem model = FieldMapper.toModel(ktypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_objekt() {
        String objektLine = "#OBJEKT 1 \"0123\" \"Service department\"";
        FileItem.Objekt expectedModel = new FileItem.Objekt(1, "0123", "Service department");
        FileItem model = FieldMapper.toModel(objektLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_oib() {
        String oibLine = "#OIB 0 1520 {8 \"12345\"} 12345.00";
        FileItem.Oib expectedModel = new FileItem.Oib(
                YearNumber.of(0),
                1520,
                ObjectReference.of(8, "12345"),
                new BigDecimal("12345.00"),
                Optional.empty());
        FileItem model = FieldMapper.toModel(oibLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_omfattn() {
        String omfattnLine = "#OMFATTN 20080630";
        FileItem.Omfattn expectedModel = new FileItem.Omfattn(LocalDate.of(2008, 6, 30));
        FileItem model = FieldMapper.toModel(omfattnLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_orgnr() {
        String orgnrLine = "#ORGNR 212000-0340 1";
        FileItem.OrgNr expectedModel = new FileItem.OrgNr("212000-0340", Optional.of(1), Optional.empty());
        FileItem model = FieldMapper.toModel(orgnrLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_oub() {
        String oubLine = "#OUB 0 1520 {8 \"12345\"} 12345.67";
        FileItem.Oub expectedModel = new FileItem.Oub(
                YearNumber.of(0),
                1520,
                ObjectReference.of(8, "12345"),
                new BigDecimal("12345.67"),
                Optional.empty());
        FileItem model = FieldMapper.toModel(oubLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("pbudgetTestData")
    void toModel_pbudget(String pbudgetLine, FileItem.Pbudget expectedModel) {
        FileItem model = FieldMapper.toModel(pbudgetLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> pbudgetTestData() {
        return Stream.of(
                Arguments.of("#PBUDGET 0 200801 3011 {} -1243.50 -415", new FileItem.Pbudget(
                        YearNumber.of(0),
                        Period.of(2008, 1),
                        3011,
                        Optional.empty(),
                        new BigDecimal("-1243.50"),
                        Optional.of(new BigDecimal(-415)))),

                Arguments.of("#PBUDGET -1 200701 5010 {1 \"0123\"} 3411.80", new FileItem.Pbudget(
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
        FileItem.Program expectedModel = new FileItem.Program("Visma Compact", "5.1");
        FileItem model = FieldMapper.toModel(programLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_prosa() {
        String prosaLine = "#PROSA \"Exported using Visma Compact 080512\"";
        FileItem.Prosa expectedModel = new FileItem.Prosa("Exported using Visma Compact 080512");
        FileItem model = FieldMapper.toModel(prosaLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("psaldoTestData")
    void toModel_psaldo(String psaldoLine, FileItem.Psaldo expectedModel) {
        FileItem model = FieldMapper.toModel(psaldoLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> psaldoTestData() {
        return Stream.of(
                Arguments.of(
                        "#PSALDO 0 200808 1910 {} 1243.50 123",
                        new FileItem.Psaldo(
                                YearNumber.of(0),
                                Period.of(2008, 8),
                                1910,
                                Optional.empty(),
                                new BigDecimal("1243.50"),
                                Optional.of(new BigDecimal(123)))
                ),
                Arguments.of(
                        "#PSALDO 0 200809 5010 {1 \"0123\"} 3411.80",
                        new FileItem.Psaldo(
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
        FileItem.Rar expectedModel = new FileItem.Rar(
                YearNumber.of(-1),
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 12, 31));
        FileItem model = FieldMapper.toModel(rarLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_res() {
        String resLine = "#RES 0 3011 -23780.78";
        FileItem.Res expectedModel = new FileItem.Res(YearNumber.of(0), 3011, new BigDecimal("-23780.78"), Optional.empty());
        FileItem model = FieldMapper.toModel(resLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_sietyp() {
        String sietypLine = "#SIETYP 4";
        FileItem.Sietyp expectedModel = new FileItem.Sietyp(4);
        FileItem model = FieldMapper.toModel(sietypLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_sru() {
        String sruLine = "#SRU 1520 9420";
        FileItem.Sru expectedModel = new FileItem.Sru(1520, 9420);
        FileItem model = FieldMapper.toModel(sruLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_taxar() {
        String taxarLine = "#TAXAR 2000";
        FileItem.Taxar expectedModel = new FileItem.Taxar(2000);
        FileItem model = FieldMapper.toModel(taxarLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("transTestData")
    void toModel_trans(String transLine, FileItem.Transaction.Trans expectedModel) {
        FileItem model = FieldMapper.toModel(transLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> transTestData() {
        return Stream.of(
                Arguments.of(
                        "#TRANS 1510 {} 150038.05",
                        new FileItem.Transaction.Trans(
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
                        new FileItem.Transaction.Trans(
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
                        new FileItem.Transaction.Trans(
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
        FileItem.Transaction.Rtrans expectedModel = new FileItem.Transaction.Rtrans(
                1520,
                new BigDecimal("500.00"),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        FileItem model = FieldMapper.toModel(rtransLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_btrans() {
        String btransLine = "#BTRANS 1520 {} 500.00";
        FileItem.Transaction.Btrans expectedModel = new FileItem.Transaction.Btrans(
                1520,
                new BigDecimal("500.00"),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        FileItem model = FieldMapper.toModel(btransLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_ub() {
        String ubLine = "#UB 0 2440 -2380.39";
        FileItem.Ub expectedModel = new FileItem.Ub(YearNumber.of(0), 2440, new BigDecimal("-2380.39"), Optional.empty());
        FileItem model = FieldMapper.toModel(ubLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_underdim() {
        String underdimLine = "#UNDERDIM 21 \"Sub-department\" 1";
        FileItem.Underdim expectedModel = new FileItem.Underdim(21, "Sub-department", 1);
        FileItem model = FieldMapper.toModel(underdimLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @Test
    void toModel_valuta() {
        String valutaLine = "#VALUTA SEK";
        FileItem.Valuta expectedModel = new FileItem.Valuta("SEK");
        FileItem model = FieldMapper.toModel(valutaLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    @ParameterizedTest
    @MethodSource("verTestData")
    void toModel_ver(List<String> verLine, FileItem.Ver expectedModel) {
        FileItem model = FieldMapper.toModel(verLine);
        assertThat(model).isEqualTo(expectedModel);
    }

    private static Stream<Arguments> verTestData() {
        return Stream.of(
                Arguments.of(
                        List.of("#VER F 12 20211223 \"Lönekörning: 2021-12-23 - Ordinarie lön\" 20211130"),
                        new FileItem.Ver(
                                LocalDate.of(2021, 12, 23),
                                Optional.of("F"),
                                Optional.of("12"),
                                Optional.of("Lönekörning: 2021-12-23 - Ordinarie lön"),
                                Optional.of(LocalDate.of(2021, 11, 30)),
                                Optional.empty(),
                                List.of())
                ),
                Arguments.of(List.of("#VER \"\" \"\" 20081216 \"Postage\""),
                        new FileItem.Ver(
                                LocalDate.of(2008, 12, 16),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.of("Postage"),
                                Optional.empty(),
                                Optional.empty(),
                                List.of())
                )
        );
    }

}
