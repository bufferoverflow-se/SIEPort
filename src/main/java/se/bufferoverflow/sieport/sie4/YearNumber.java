package se.bufferoverflow.sieport.sie4;

public record YearNumber(int yearNo) {
    public YearNumber {
        if (yearNo > 0) {
            throw new IllegalArgumentException("Year number cannot be greater than zero");
        }
    }

    public static YearNumber of(int yearNo) {
        return new YearNumber(yearNo);
    }
}
