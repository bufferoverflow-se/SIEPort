package se.bufferoverflow.sieport.sie4;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public sealed interface SIE4Item {
    DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    record Adress(String contact, String distributionAddress, String postalAddress, String tel) implements SIE4Item {}
    record Bkod(int sniCode) implements SIE4Item {}
    record Dim(int dimensionNo, String name) implements SIE4Item {}
    record Enhet(int accountNo, String unit) implements SIE4Item {}
    record Flagga(int flag) implements SIE4Item {}

    record Fnamn(String companyName) implements SIE4Item {}
    record Fnr(String companyId) implements SIE4Item {}
    record Format(String format) implements SIE4Item {}
    record Ftyp(CompanyType companyType) implements SIE4Item {}
    record Gen(LocalDate date, Optional<String> signature) implements SIE4Item {}

    record Ib(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}
    record Konto(int accountNo, String accountName) implements SIE4Item {}
    record Kptyp(String type) implements SIE4Item {}
    record Ktyp(int accountNo, AccountType type) implements SIE4Item {
        public enum AccountType { T, S, K, I }
    }
    record Objekt(int dimensionNo, String objectNo, String objectName) implements SIE4Item {}

    record Oib(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}
    record Omfattn(LocalDate date) implements SIE4Item {}
    record OrgNr(String orgNr, Optional<Integer> acqNo, Optional<Integer> actNo) implements SIE4Item {
        public static OrgNr of(String orgNr) {
            return new OrgNr(orgNr, Optional.empty(), Optional.empty());
        }
    }
    record Oub(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}
    record Pbudget(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}

    record Program(String programName, String version) implements SIE4Item {}
    record Prosa(String comment) implements SIE4Item {}
    record Psaldo(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}
    record Rar(YearNumber yearNumber, LocalDate start, LocalDate end) implements SIE4Item {}
    record Res(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}

    record Sietyp(int typeNo) implements SIE4Item {}
    record Sru(int accountNo, int sruCode) implements SIE4Item {}
    record Taxar(int year) implements SIE4Item {}
    record Ub(YearNumber yearNumber, int account, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {}
    record Underdim(int dimensionNo, String name, int superDimensionNo) implements SIE4Item {}
    record Valuta(String currencyCode) implements SIE4Item {}
    record Ver(LocalDate date, Optional<String> series, Optional<String> verificationNo, Optional<String> text, Optional<LocalDate> regDate, Optional<String> sign, List<Transaction> transactions) implements SIE4Item {}

    sealed interface Transaction extends SIE4Item {
        int accountNo();
        BigDecimal amount();
        List<ObjectReference> objectReferences();
        Optional<LocalDate> transactionDate();
        Optional<String> text();
        Optional<BigDecimal> quantity();
        Optional<String> sign();

        record Trans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {}
        record Rtrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {}
        record Btrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {}
    }
}
