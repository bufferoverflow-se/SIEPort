package se.bufferoverflow.sieport.sie4;

import java.time.LocalDate;

public record FinancialYear(YearNumber yearNumber, LocalDate from, LocalDate to) {
    public FinancialYear {
        if (!to.isAfter(from)) {
           throw new SIE4Exception("from date must be before to date");
        }
    }

    /**
     * Returns an instance spanning exactly one year, e.g. 2000-01-01 to 2000-12-31.
     *
     * @param yearNumber the SIE4 year index (0 = current, -1 = previous, etc.)
     * @param from start date of the year
     * @return instance with end date = start date + 1 year - 1 day
     */
    public static FinancialYear of(YearNumber yearNumber, LocalDate from) {
        return new FinancialYear(yearNumber, from, from.plusYears(1).minusDays(1));
    }
}
