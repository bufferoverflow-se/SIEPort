package se.bufferoverflow.sieport.sie4;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public sealed interface FileItem {
    record Adress(String contact, String distributionAddress, String postalAddress, String tel) implements FileItem {}
    record Bkod(int sniCode) implements FileItem {}
    record Dim(int dimensionNo, String name) implements FileItem {}
    record Enhet(int accountNo, String unit) implements FileItem {}
    record Flagga(int flag) implements FileItem {}

    record Fnamn(String companyName) implements FileItem {}
    record Fnr(String companyId) implements FileItem {}
    record Format(String format) implements FileItem {}
    record Ftyp(CompanyType companyType) implements FileItem {}
    record Gen(LocalDate date, Optional<String> signature) implements FileItem {}

    record Ib(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}
    record Konto(int accountNo, String accountName) implements FileItem {}
    record Kptyp(String type) implements FileItem {}
    record Ktyp(int accountNo, AccountType type) implements FileItem {
        public enum AccountType { T, S, K, I }
    }
    record Objekt(int dimensionNo, String objectNo, String objectName) implements FileItem {}

    record Oib(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}
    record Omfattn(LocalDate date) implements FileItem {}
    record OrgNr(String orgNr, Optional<Integer> acqNo, Optional<Integer> actNo) implements FileItem {}
    record Oub(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}
    record Pbudget(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}

    record Program(String programName, String version) implements FileItem {}
    record Prosa(String comment) implements FileItem {}
    record Psaldo(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}
    record Rar(YearNumber yearNumber, LocalDate start, LocalDate end) implements FileItem {}
    record Res(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}

    record Sietyp(int typeNo) implements FileItem {}
    record Sru(int accountNo, int sruCode) implements FileItem {}
    record Taxar(int year) implements FileItem {}
    record Ub(YearNumber yearNumber, int account, BigDecimal balance, Optional<BigDecimal> quantity) implements FileItem {}
    record Underdim(int dimensionNo, String name, int superDimensionNo) implements FileItem {}
    record Valuta(String currencyCode) implements FileItem {}
    record Ver(LocalDate date, Optional<String> series, Optional<String> verificationNo, Optional<String> text, Optional<LocalDate> regDate, Optional<String> sign, List<Transaction> transactions) implements FileItem {}

    sealed interface Transaction extends FileItem {
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
