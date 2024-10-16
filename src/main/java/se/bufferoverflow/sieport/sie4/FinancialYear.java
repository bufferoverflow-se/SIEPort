package se.bufferoverflow.sieport.sie4;

import java.time.LocalDate;

public record FinancialYear(LocalDate from, LocalDate to) {
    public FinancialYear {
        if (to.isBefore(from)) {
           throw new SIE4Exception("from date must be before to date");
        }
    }

    /**
     * Returns an instance which is equal to one year, for example 2000-01-01 - 2000-12-31
     *
     * @param from Start date of year
     * @return Instance with end date = start date + 1 year - 1 day.
     */
    public static FinancialYear of(LocalDate from) {
        return new FinancialYear(from, from.plusYears(1).minusDays(1));
    }
}
