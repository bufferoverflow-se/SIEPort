package se.bufferoverflow.sieport.sie4;

import java.time.LocalDate;
import java.util.List;

/**
 * Items identifying company and file information
 */
public record IdentificationItems(
    Integer flag,
    String programWithVersion,
    LocalDate generatedAt,
    Integer sieType,
    String comment,
    String kptyp,
    String currencyCode,
    Integer taxYear,
    List<FinancialYear> periods,
    String contactPerson,
    String address,
    String phoneNumber,
    Integer sniCode,
    CompanyType companyType,
    String companyName,
    String organizationNumber
) {
}
