package se.bufferoverflow.sieport.sie4;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A flat view of the identification section of a SIE4 document, returned by
 * {@link SIE4Document#getIdentificationItems()}. Fields that are compulsory in
 * both SIE4I and SIE4E are returned as plain types; fields that are optional in
 * at least one format are wrapped in {@link Optional}.
 *
 * @param flag              import status flag; 0 = not yet imported, 1 = already imported
 *                          (compulsory in both SIE4I and SIE4E — {@code null} only for
 *                          documents built without {@code #FLAGGA})
 * @param programWithVersion accounting program name and version, e.g. {@code "Visma, 2022.2"}
 *                          (compulsory in both formats — {@code null} only for incomplete documents)
 * @param generatedAt       date the file was generated
 *                          (compulsory in both formats — {@code null} only for incomplete documents)
 * @param sieType           SIE file type number (always 4 for this library)
 *                          (compulsory in both formats — {@code null} only for incomplete documents)
 * @param comment           free-text comment ({@code #PROSA})
 * @param kptyp             account plan type, e.g. {@code "EUBAS97"}
 * @param currencyCode      ISO 4217 currency code, e.g. {@code "SEK"}
 * @param taxYear           four-digit tax year
 * @param periods           fiscal year date ranges; empty list if none present
 * @param contactPerson     contact person name from the company address
 * @param address           street and postal address concatenated
 * @param phoneNumber       telephone number from the company address
 * @param sniCode           Swedish Standard Industrial Classification code
 * @param companyType       legal form of the company; see {@link CompanyType}
 * @param companyName       full legal name of the company
 *                          (compulsory in both formats — {@code null} only for incomplete documents)
 * @param organizationNumber Swedish organisation number
 */
public record IdentificationItems(
    Integer flag,
    String programWithVersion,
    LocalDate generatedAt,
    Integer sieType,
    Optional<String> comment,
    Optional<String> kptyp,
    Optional<String> currencyCode,
    Optional<Integer> taxYear,
    List<FinancialYear> periods,
    Optional<String> contactPerson,
    Optional<String> address,
    Optional<String> phoneNumber,
    Optional<Integer> sniCode,
    Optional<CompanyType> companyType,
    String companyName,
    Optional<String> organizationNumber
) {
}
