package se.bufferoverflow.sieport.sie4;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public sealed interface SIE4Item {
    DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    SIE4ItemType itemType();

    default boolean isMandatorySie4E() {
        return false;
    }

    default boolean isMandatorySie4I() {
        return false;
    }

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
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FLAGGA;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
        }
    }

    record Fnamn(String companyName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNAMN;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
        }
    }

    record Fnr(String companyId) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNR;
        }
    }

    record Format(String format) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FORMAT;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
        }
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

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
        }
    }

    record Ib(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.IB;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }
    }

    record Konto(int accountNo, String accountName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KONTO;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
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

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
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

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }
    }

    record Res(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.RES;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }
    }

    record Sietyp(int typeNo) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.SIETYP;
        }

        @Override
        public boolean isMandatorySie4E() {
            return true;
        }

        @Override
        public boolean isMandatorySie4I() {
            return true;
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

        @Override
        public boolean isMandatorySie4E() {
            return true;
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
