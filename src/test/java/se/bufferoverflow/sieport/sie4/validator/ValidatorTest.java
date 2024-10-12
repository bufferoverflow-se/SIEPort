package se.bufferoverflow.sieport.sie4.validator;

import org.junit.jupiter.api.Test;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ValidatorTest {

    @Test
    void validateSie4i_testWithMissingMandatoryItems() {
        List<SIE4Item> items = List.of(new SIE4Item.Program("TestProgram", "1.0"));

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result).contains(ValidationError.MISSING_MANDATORY_ITEMS);
    }

    @Test
    void validateSie4i_testWithForbiddenItems() {
        List<SIE4Item> items = List.of(
                new SIE4Item.Fnamn("TestCompany"),
                new SIE4Item.Bkod(12345678));

        List<ValidationError> result = Validator.validateSie4i(items);

        assertThat(result).contains(ValidationError.FORBIDDEN_ITEMS_PRESENT);
    }

    @Test
    void validateSie4i_testWithNoErrors() {
        List<SIE4Item> items = List.of(
                new SIE4Item.Flagga(0),
                new SIE4Item.Format("PC8"),
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

        assertThat(result).contains(ValidationError.MISSING_MANDATORY_ITEMS);
    }

    @Test
    void validateSie4e_testWithNoErrors() {
        List<SIE4Item> items = List.of(
                new SIE4Item.Flagga(0),
                new SIE4Item.Format("PC8"),
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
