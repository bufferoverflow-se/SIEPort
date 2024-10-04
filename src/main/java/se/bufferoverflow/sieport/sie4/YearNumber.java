package se.bufferoverflow.sieport.sie4;

public record YearNumber(int yearNo) {
    public static final YearNumber CURRENT_YEAR = YearNumber.of(0);
    public static final YearNumber PREV_YEAR = YearNumber.of(-1);

    public YearNumber {
        if (yearNo > 0) {
            throw new SIE4Exception("Year number cannot be greater than zero");
        }
    }

    public static YearNumber of(int yearNo) {
        return new YearNumber(yearNo);
    }
}
