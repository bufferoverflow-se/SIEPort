package se.bufferoverflow.sieport.sie4;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PeriodTest {

    @Test
    void testOf_validIntegers_shouldReturnPeriod() {
        assertDoesNotThrow(() -> {
            Period period = Period.of(2024, 12);
            assertEquals(2024, period.year());
            assertEquals(12, period.month());
        });
    }

    @Test
    void testOf_invalidMonth_shouldThrowException() {
        assertThrows(DateTimeException.class, () -> Period.of(2024, 15));
    }

    @Test
    void testOfString_validPeriodString_shouldReturnPeriod() {
        assertDoesNotThrow(() -> {
            Period period = Period.of("202411");
            assertEquals(2024, period.year());
            assertEquals(11, period.month());
        });
    }

    @Test
    void testOfString_illegalStrings_shouldThrowException() {
        assertThrows(SIE4Exception.class, () -> Period.of(""));
        assertThrows(SIE4Exception.class, () -> Period.of(" "));
        assertThrows(SIE4Exception.class, () -> Period.of(null));
    }

    @Test
    void testOfString_invalidPeriodString_shouldThrowException() {
        assertThrows(DateTimeException.class, () -> Period.of("202413"));
    }
}
