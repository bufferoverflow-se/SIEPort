package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialYearTest {

    @Test
    void of_ShouldReturnFinancialYearWithCorrectDates() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);

        FinancialYear actual = FinancialYear.of(YearNumber.CURRENT_YEAR, from);

        assertEquals(YearNumber.CURRENT_YEAR, actual.yearNumber());
        assertEquals(actual.from(), from);
        assertEquals(actual.to(), to);
    }

    @Test
    void shouldThrowSIE4ExceptionWhenToDateIsBeforeFromDate() {
        LocalDate from = LocalDate.now();
        assertThrows(SIE4Exception.class, () -> new FinancialYear(YearNumber.CURRENT_YEAR, from.plusDays(1), from));
    }

    @Test
    void shouldThrowSIE4ExceptionWhenToDateEqualsFromDate() {
        LocalDate date = LocalDate.of(2024, 6, 1);
        assertThrows(SIE4Exception.class, () -> new FinancialYear(YearNumber.CURRENT_YEAR, date, date));
    }
}
