package se.bufferoverflow.sieport.sie4;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

public record Period(int year, int month) {

    public static final DateTimeFormatter SIE4_PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public Period {
        ChronoField.YEAR.checkValidValue(year);
        ChronoField.MONTH_OF_YEAR.checkValidValue(month);
    }

    public static Period of(int year, int month) {
        return new Period(year, month);
    }

    public static Period of(String periodString) {
        if (periodString == null || periodString.isBlank()) {
            throw new IllegalArgumentException("Period string cannot be null or blank");
        }
        TemporalAccessor accessor = SIE4_PERIOD_FORMATTER.parse(periodString);
        return Period.of(accessor.get(ChronoField.YEAR), accessor.get(ChronoField.MONTH_OF_YEAR));
    }
}
