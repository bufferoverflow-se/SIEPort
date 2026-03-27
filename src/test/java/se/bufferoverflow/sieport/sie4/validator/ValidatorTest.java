package se.bufferoverflow.sieport.sie4.validator;

import org.junit.jupiter.api.Test;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.SIE4ItemType;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    @Test
    void validateSie4i_missingMandatoryItems_containsOffendingTypes() {
        List<SIE4Item> items = List.of(new SIE4Item.Program("TestProgram", "1.0"));

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result)
                .filteredOn(e -> e instanceof ValidationError.MissingMandatoryItems)
                .first()
                .satisfies(e -> assertThat(e.offendingItems())
                        .contains(SIE4ItemType.FLAGGA, SIE4ItemType.FNAMN, SIE4ItemType.FORMAT));
    }

    @Test
    void validateSie4i_forbiddenItemsPresent_containsOffendingTypes() {
        List<SIE4Item> items = List.of(
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Bkod(12345678));

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result)
                .filteredOn(e -> e instanceof ValidationError.ForbiddenItemsPresent)
                .first()
                .satisfies(e -> assertThat(e.offendingItems())
                        .containsExactly(SIE4ItemType.BKOD));
    }

    @Test
    void validateSie4i_testWithNoErrors() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.UNSET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany")
        );

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result).isEmpty();
    }


    @Test
    void validateSie4e_testWithMissingMandatoryItems() {
        List<SIE4Item> items = List.of(new SIE4Item.Program("TestProgram", "1.0"));

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result).anyMatch(e -> e instanceof ValidationError.MissingMandatoryItems);
    }

    @Test
    void validateSie4e_completelyAbsentBalanceItems_reportedOnlyOnceMandatory() {
        // IB, UB, RES are fully absent — should appear in MissingMandatoryItems only,
        // not duplicated in MissingCurrentYearItems
        List<SIE4Item> items = List.of(new SIE4Item.Program("TestProgram", "1.0"));

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result).noneMatch(e -> e instanceof ValidationError.MissingCurrentYearItems);
    }

    @Test
    void validateSie4e_balanceItemsOnlyForPreviousYear_shouldFail() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.UNSET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Rar(YearNumber.CURRENT_YEAR, LocalDate.MIN, LocalDate.MAX),
                new SIE4Item.Konto(1930, "konto"),
                new SIE4Item.Ib(YearNumber.PREV_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Ub(YearNumber.PREV_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Res(YearNumber.PREV_YEAR, 1930, BigDecimal.ZERO, Optional.empty())
        );

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result).anyMatch(e -> e instanceof ValidationError.MissingCurrentYearItems);
    }

    @Test
    void validateSie4e_missingCurrentYearItems_containsOffendingTypes() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.UNSET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Rar(YearNumber.CURRENT_YEAR, LocalDate.MIN, LocalDate.MAX),
                new SIE4Item.Konto(1930, "konto"),
                new SIE4Item.Ib(YearNumber.PREV_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Ub(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Res(YearNumber.PREV_YEAR, 1930, BigDecimal.ZERO, Optional.empty())
        );

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result)
                .filteredOn(e -> e instanceof ValidationError.MissingCurrentYearItems)
                .first()
                .satisfies(e -> assertThat(e.offendingItems())
                        .containsExactlyInAnyOrder(SIE4ItemType.IB, SIE4ItemType.RES));
    }

    @Test
    void validateSie4i_flaggaSet_shouldFail() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.SET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany")
        );

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result).anyMatch(e -> e instanceof ValidationError.InvalidFlaggaValue);
    }

    @Test
    void validateSie4e_flaggaSet_shouldFail() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.SET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Rar(YearNumber.CURRENT_YEAR, LocalDate.MIN, LocalDate.MAX),
                new SIE4Item.Konto(1930, "konto"),
                new SIE4Item.Ib(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Ub(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Res(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty())
        );

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result).anyMatch(e -> e instanceof ValidationError.InvalidFlaggaValue);
    }

    @Test
    void validateSie4e_testWithNoErrors() {
        List<SIE4Item> items = List.of(
                SIE4Item.Flagga.UNSET,
                SIE4Item.Format.pc8(),
                new SIE4Item.Sietyp(4),
                new SIE4Item.Program("TestProgram", "1.0"),
                new SIE4Item.Gen(LocalDate.now(), Optional.empty()),
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Rar(YearNumber.CURRENT_YEAR, LocalDate.MIN, LocalDate.MAX),
                new SIE4Item.Konto(1930, "konto"),
                new SIE4Item.Ib(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Ub(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty()),
                new SIE4Item.Res(YearNumber.CURRENT_YEAR, 1930, BigDecimal.ZERO, Optional.empty())
        );

        List<ValidationError> result = Validator.validateSie4e(items);

        assertThat(result).isEmpty();
    }
}
