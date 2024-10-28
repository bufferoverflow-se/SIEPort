package se.bufferoverflow.sieport.sie4;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public sealed interface SIE4Item {
    DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    SIE4ItemType itemType();

    record Adress(String contact, String distributionAddress, String postalAddress, String tel) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ADRESS;
        }
    }

    record Bkod(int sniCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.BKOD;
        }
    }

    record Dim(int dimensionNo, String name) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.DIM;
        }
    }

    record Enhet(int accountNo, String unit) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ENHET;
        }
    }

    record Flagga(int flag) implements SIE4Item {
        public static final Flagga UNSET = new Flagga(0);
        public static final Flagga SET = new Flagga(1);

        public Flagga {
            if (flag < 0 || flag > 1) {
                throw new SIE4Exception("Flagga must be either 0 or 1");
            }
        }

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FLAGGA;
        }
    }

    record Fnamn(String companyName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNAMN;
        }

    }

    record Fnr(String companyId) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNR;
        }
    }

    record Format(FormatType format) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FORMAT;
        }

        public static Format pc8() {
            return new Format(FormatType.PC8);
        }

        public enum FormatType { PC8 }
    }

    record Ftyp(CompanyType companyType) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FTYP;
        }
    }

    record Gen(LocalDate date, Optional<String> signature) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.GEN;
        }

        public static Gen now() {
            return new Gen(LocalDate.now(), Optional.empty());
        }
    }

    record Ib(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.IB;
        }

    }

    record Konto(int accountNo, String accountName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KONTO;
        }

    }

    record Kptyp(String type) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KPTYP;
        }
    }

    record Ktyp(int accountNo, AccountType type) implements SIE4Item {
        public enum AccountType { T, S, K, I }

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KTYP;
        }
    }

    record Objekt(int dimensionNo, String objectNo, String objectName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OBJEKT;
        }
    }

    record Oib(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OIB;
        }
    }

    record Omfattn(LocalDate date) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OMFATTN;
        }
    }

    record OrgNr(String orgNr, Optional<Integer> acqNo, Optional<Integer> actNo) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ORGNR;
        }

        public static OrgNr of(String orgNr) {
            return new OrgNr(orgNr, Optional.empty(), Optional.empty());
        }
    }

    record Oub(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OUB;
        }
    }

    record Pbudget(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PBUDGET;
        }
    }

    record Program(String programName, String version) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PROGRAM;
        }

    }

    record Prosa(String comment) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PROSA;
        }
    }

    record Psaldo(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PSALDO;
        }
    }

    record Rar(YearNumber yearNumber, LocalDate start, LocalDate end) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.RAR;
        }

    }

    record Res(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.RES;
        }

    }

    record Sietyp(int typeNo) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.SIETYP;
        }

    }

    record Sru(int accountNo, int sruCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.SRU;
        }
    }

    record Taxar(int year) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.TAXAR;
        }
    }

    record Ub(YearNumber yearNumber, int account, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.UB;
        }

    }

    record Underdim(int dimensionNo, String name, int superDimensionNo) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.UNDERDIM;
        }
    }

    record Valuta(String currencyCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.VALUTA;
        }
    }

    record Ver(LocalDate date, Optional<String> series, Optional<String> verificationNo, Optional<String> text, Optional<LocalDate> regDate, Optional<String> sign, List<Transaction> transactions) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.VER;
        }

        public static Ver forSie4e(LocalDate date, String text, String series, String verificationNumber, List<Transaction> transactions) {
            return new Ver(date, Optional.of(series), Optional.of(verificationNumber), Optional.of(text), Optional.empty(), Optional.empty(), transactions);
        }

        public static Ver forSie4i(LocalDate date, String text, List<Transaction> transactions) {
            return new Ver(date, Optional.empty(), Optional.empty(), Optional.of(text), Optional.empty(), Optional.empty(), transactions);
        }
    }

    sealed interface Transaction extends SIE4Item {
        int accountNo();
        BigDecimal amount();
        List<ObjectReference> objectReferences();
        Optional<LocalDate> transactionDate();
        Optional<String> text();
        Optional<BigDecimal> quantity();
        Optional<String> sign();

        record Trans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.TRANS;
            }
        }

        record Rtrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.RTRANS;
            }
        }

        record Btrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.BTRANS;
            }
        }
    }
}
