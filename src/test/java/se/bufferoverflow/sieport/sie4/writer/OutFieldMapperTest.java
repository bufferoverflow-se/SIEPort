package se.bufferoverflow.sieport.sie4.writer;

import org.junit.jupiter.api.Test;
import se.bufferoverflow.sieport.sie4.CompanyType;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.ObjectReference;
import se.bufferoverflow.sieport.sie4.Period;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OutFieldMapperTest {
    @Test
    void testToFileString_Address() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Adress("Contact Name", "Box 21", "12345 MALMÖ", "123-456 789"));

        assertEquals("#ADRESS \"Contact Name\" \"Box 21\" \"12345 MALMÖ\" \"123-456 789\"", fieldString);
    }

    @Test
    void testToFileString_Bkod() {
        // This test verifies that the toFileString method creates the correct string for a Bkod.
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Bkod(1234));

        assertEquals("#BKOD 1234", fieldString);
    }

    @Test
    void testToFileString_Dim() {
        // This test verifies that the toFileString method creates the correct string for a Dim.
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Dim(1, "Name"));

        assertEquals("#DIM 1 Name", fieldString);
    }

    @Test
    void testToFileString_Enhet() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Enhet(1001, "Unit"));

        assertEquals("#ENHET 1001 Unit", fieldString);
    }

    @Test
    void testToFileString_Flagga() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Flagga(1));

        assertEquals("#FLAGGA 1", fieldString);
    }

    @Test
    void testToFileString_Fnamn() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Fnamn("Company Name"));

        assertEquals("#FNAMN \"Company Name\"", fieldString);
    }

    @Test
    void testToFileString_Fnr() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Fnr("Kalles"));

        assertEquals("#FNR Kalles", fieldString);
    }

    @Test
    void testToFileString_Format() {
        String fieldString = OutFieldMapper.toFileString(SIE4Item.Format.pc8());

        assertEquals("#FORMAT PC8", fieldString);
    }

    @Test
    void testToFileString_Ftyp() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Ftyp(CompanyType.AB));

        assertEquals("#FTYP AB", fieldString);
    }

    @Test
    void testToFileString_Gen() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Gen(LocalDate.of(2023, 10, 10), Optional.of("AN")));

        assertEquals("#GEN 20231010 AN", fieldString);
    }

    @Test
    void testToFileString_Ib() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Ib(YearNumber.of(0), 1930, new BigDecimal("5000.01"), Optional.empty()));

        assertEquals("#IB 0 1930 5000.01", fieldString);
    }

    @Test
    void testToFileString_Konto() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Konto(3001, "Main Account"));

        assertEquals("#KONTO 3001 \"Main Account\"", fieldString);
    }

    @Test
    void testToFileString_Kptyp() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Kptyp("EUBAS97"));

        assertEquals("#KPTYP EUBAS97", fieldString);
    }

    @Test
    void testToFileString_Ktyp() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Ktyp(5001, SIE4Item.Ktyp.AccountType.T));

        assertEquals("#KTYP 5001 T", fieldString);
    }

    @Test
    void testToFileString_Objekt() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Objekt(1, "0123", "Object Name"));

        assertEquals("#OBJEKT 1 0123 \"Object Name\"", fieldString);
    }

    @Test
    void testToFileString_Oib() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Oib(
                YearNumber.of(0),
                1510,
                ObjectReference.of(8, "A2345"),
                new BigDecimal("23780.78"),
                Optional.empty()));

        assertEquals("#OIB 0 1510 {8 A2345} 23780.78", fieldString);
    }

    @Test
    void testToFileString_Omfattn() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Omfattn(LocalDate.of(2024, 12, 31)));

        assertEquals("#OMFATTN 20241231", fieldString);
    }

    @Test
    void testToFileString_OrgNr() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.OrgNr("556334-3689", Optional.of(1), Optional.empty()));

        assertEquals("#ORGNR 556334-3689 1", fieldString);
    }

    @Test
    void testToFileString_Oub() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Oub(
                YearNumber.of(0),
                1510,
                ObjectReference.of(1, "0001"),
                new BigDecimal("7000.01"),
                Optional.empty()));

        assertEquals("#OUB 0 1510 {1 0001} 7000.01", fieldString);
    }

    @Test
    void testToFileString_Pbudget() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Pbudget(
                YearNumber.of(0),
                Period.of(2024, 12),
                3011,
                Optional.of(ObjectReference.of(1, "0001")),
                new BigDecimal("8000.01"),
                Optional.empty()));

        assertEquals("#PBUDGET 0 202412 3011 {1 0001} 8000.01", fieldString);

        fieldString = OutFieldMapper.toFileString(new SIE4Item.Pbudget(
                YearNumber.of(0),
                Period.of(2024, 12),
                5010,
                Optional.empty(),
                new BigDecimal("-1234.50"),
                Optional.of(new BigDecimal("-415"))));

        assertEquals("#PBUDGET 0 202412 5010 {} -1234.50 -415", fieldString);
    }

    @Test
    void testToFileString_Program() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Program("Program Name", "1.0"));

        assertEquals("#PROGRAM \"Program Name\" 1.0", fieldString);
    }

    @Test
    void testToFileString_Prosa() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Prosa("Some comment"));

        assertEquals("#PROSA \"Some comment\"", fieldString);
    }

    @Test
    void testToFileString_Psaldo() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Psaldo(
                YearNumber.of(0),
                Period.of(2008, 9),
                1910,
                Optional.empty(),
                new BigDecimal("9000.01"),
                Optional.of(new BigDecimal("123"))));

        assertEquals("#PSALDO 0 200809 1910 {} 9000.01 123", fieldString);

        fieldString = OutFieldMapper.toFileString(new SIE4Item.Psaldo(
                YearNumber.of(0),
                Period.of(2024, 12),
                5010,
                Optional.of(ObjectReference.of(1, "0001")),
                new BigDecimal("9000.01"),
                Optional.empty()));

        assertEquals("#PSALDO 0 202412 5010 {1 0001} 9000.01", fieldString);
    }

    @Test
    void testToFileString_Rar() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Rar(
                YearNumber.of(-1),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)));

        assertEquals("#RAR -1 20240101 20241231", fieldString);
    }

    @Test
    void testToFileString_Res() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Res(
                YearNumber.of(0), 3011, new BigDecimal("-23780.78"), Optional.empty()));

        assertEquals("#RES 0 3011 -23780.78", fieldString);
    }

    @Test
    void testToFileString_Sietyp() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Sietyp(4));

        assertEquals("#SIETYP 4", fieldString);
    }

    @Test
    void testToFileString_Sru() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Sru(1910, 2024));

        assertEquals("#SRU 1910 2024", fieldString);
    }

    @Test
    void testToFileString_Taxar() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Taxar(2024));

        assertEquals("#TAXAR 2024", fieldString);
    }

    @Test
    void testToFileString_Ub() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Ub(YearNumber.of(0), 2440, new BigDecimal("-2380.39"), Optional.empty()));

        assertEquals("#UB 0 2440 -2380.39", fieldString);
    }

    @Test
    void testToFileString_Underdim() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Underdim(21, "Sub-department", 1));

        assertEquals("#UNDERDIM 21 Sub-department 1", fieldString);
    }

    @Test
    void testToFileString_Valuta() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Valuta("USD"));

        assertEquals("#VALUTA USD", fieldString);
    }

    @Test
    void testToFileString_Ver() {
        String fieldString = OutFieldMapper.toFileString(new SIE4Item.Ver(
                LocalDate.of(2021, 11, 25),
                Optional.of("F"),
                Optional.of("11"),
                Optional.of("Lönekörning: 2021-11-25 - Ordinarie lön"),
                Optional.of(LocalDate.of(2021, 10, 30)),
                Optional.empty(),
                List.of(
                        new SIE4Item.Transaction.Trans(
                                1930,
                                new BigDecimal("-82651.00"),
                                List.of(),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty()),
                        new SIE4Item.Transaction.Trans(
                                7010,
                                new BigDecimal("26898.84"),
                                List.of(ObjectReference.of(1, "Nord")),
                                Optional.of(LocalDate.of(2021, 11, 25)),
                                Optional.empty(),
                                Optional.of(new BigDecimal(200)),
                                Optional.empty()),
                        new SIE4Item.Transaction.Trans(
                                7090,
                                new BigDecimal("-2334.87"),
                                List.of(ObjectReference.of(1, "Syd"),
                                        ObjectReference.of(6, "0001")),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty(),
                                Optional.empty())
                )
        ));

        String expectedResult = """
                #VER F 11 20211125 "Lönekörning: 2021-11-25 - Ordinarie lön" 20211030
                {
                   #TRANS 1930 {} -82651.00
                   #TRANS 7010 {1 Nord} 26898.84 20211125 "" 200
                   #TRANS 7090 {1 Syd 6 0001} -2334.87
                }""";

        assertThat(fieldString).isEqualTo(expectedResult);
    }

    @Test
    void testToFileString_Ver_withoutIdentifiers() {
        String fieldString = OutFieldMapper.toFileString(SIE4Item.Ver.of(
                LocalDate.of(2021, 11, 25),
                "Verifikattitel",
                List.of(
                        SIE4Item.Transaction.Trans.of(1930, new BigDecimal(-82000)),
                        SIE4Item.Transaction.Trans.of(7010, new BigDecimal(82000))
                )
        ));

        String expectedResult = """
                #VER "" "" 20211125 Verifikattitel
                {
                   #TRANS 1930 {} -82000
                   #TRANS 7010 {} 82000
                }""";

        assertThat(fieldString).isEqualTo(expectedResult);
    }
}
