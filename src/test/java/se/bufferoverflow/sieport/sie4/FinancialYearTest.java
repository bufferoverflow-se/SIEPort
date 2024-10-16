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

        FinancialYear actual = FinancialYear.of(from);

        assertEquals(actual.from(), from);
        assertEquals(actual.to(), to);
    }

    @Test
    void shouldThrowSIE4ExceptionWhenToDateIsBeforeFromDate() {
        LocalDate from = LocalDate.now();
        assertThrows(SIE4Exception.class, () -> new FinancialYear(from.plusDays(1), from));
    }
}
