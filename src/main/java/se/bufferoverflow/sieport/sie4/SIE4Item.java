package se.bufferoverflow.sieport.sie4;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Sealed interface representing a single labeled item in a SIE4 file.
 *
 * <p>Each SIE4 label (e.g. {@code #FNAMN}, {@code #VER}, {@code #TRANS}) maps to a record
 * type implementing this interface. Use {@link SIE4#parse} to obtain items from a file, and
 * {@link SIE4Document} or {@link SIE4#write} to produce them.
 */
public sealed interface SIE4Item {
    /** Date formatter for the {@code yyyyMMdd} pattern used throughout the SIE4 format. */
    DateTimeFormatter SIE4_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    SIE4ItemType itemType();

    /**
     * Company postal address ({@code #ADRESS}).
     *
     * @param contact contact person
     * @param distributionAddress street address or P.O. box
     * @param postalAddress zip code and city
     * @param tel telephone number
     */
    record Adress(String contact, String distributionAddress, String postalAddress, String tel) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ADRESS;
        }
    }

    /**
     * Swedish Standard Industrial Classification code ({@code #BKOD}).
     *
     * @param sniCode the SNI code for the company's industry
     */
    record Bkod(int sniCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.BKOD;
        }
    }

    /**
     * Named cost dimension definition ({@code #DIM}), such as a department or project category.
     *
     * @param dimensionNo dimension identifier
     * @param name human-readable name
     */
    record Dim(int dimensionNo, String name) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.DIM;
        }
    }

    /**
     * Measurement unit for an account's quantity field ({@code #ENHET}).
     *
     * @param accountNo the account this unit applies to
     * @param unit unit label, e.g. {@code "st"} (pieces) or {@code "tim"} (hours)
     */
    record Enhet(int accountNo, String unit) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ENHET;
        }
    }

    /**
     * Import status flag ({@code #FLAGGA}).
     * Use {@link #UNSET} for a newly created file and {@link #SET} after the receiving program
     * has successfully imported it.
     *
     * @param flag {@code 0} = not yet imported, {@code 1} = already imported
     */
    record Flagga(int flag) implements SIE4Item {
        /** Flag value indicating the file has not yet been imported. */
        public static final Flagga UNSET = new Flagga(0);
        /** Flag value indicating the file has already been imported. */
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

    /**
     * Company name ({@code #FNAMN}).
     *
     * @param companyName the full legal name of the company
     */
    record Fnamn(String companyName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNAMN;
        }

    }

    /**
     * Company identifier in the accounting program ({@code #FNR}).
     *
     * @param companyId the program-specific company identifier
     */
    record Fnr(String companyId) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FNR;
        }
    }

    /**
     * File character encoding declaration ({@code #FORMAT}). Always {@code PC8} (IBM-437).
     *
     * @param format the format type; use {@link #pc8()}
     */
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

    /**
     * Legal form of the company ({@code #FTYP}).
     *
     * @param companyType the legal entity type; see {@link CompanyType}
     */
    record Ftyp(CompanyType companyType) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.FTYP;
        }
    }

    /**
     * File generation date and optional author signature ({@code #GEN}).
     *
     * @param date the date the file was generated
     * @param signature optional initials or name of the person who generated the file
     */
    record Gen(LocalDate date, Optional<String> signature) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.GEN;
        }

        public static Gen now() {
            return new Gen(LocalDate.now(), Optional.empty());
        }
    }

    /**
     * Opening balance for an account in a given fiscal year ({@code #IB}).
     *
     * @param yearNumber the fiscal year; 0 = current year, -1 = previous year, etc.
     * @param accountNo the account number
     * @param balance the opening balance
     * @param quantity optional opening quantity for accounts that track units
     */
    record Ib(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.IB;
        }

    }

    /**
     * Chart of accounts entry ({@code #KONTO}).
     *
     * @param accountNo the account number
     * @param accountName the account name
     */
    record Konto(int accountNo, String accountName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KONTO;
        }

    }

    /**
     * Account plan type used in this file ({@code #KPTYP}), e.g. {@code BAS95}, {@code BAS96},
     * or {@code EUBAS97}.
     *
     * @param type the account plan type identifier
     */
    record Kptyp(String type) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KPTYP;
        }
    }

    /**
     * Account type classification ({@code #KTYP}).
     *
     * @param accountNo the account number
     * @param type the account type; see {@link AccountType}
     */
    record Ktyp(int accountNo, AccountType type) implements SIE4Item {
        /**
         * Classification of an account's role in the balance sheet or income statement.
         */
        public enum AccountType {
            /** Tillgång — asset. */
            T,
            /** Skuld — liability. */
            S,
            /** Kostnad — expense. */
            K,
            /** Inkomst — income. */
            I
        }

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.KTYP;
        }
    }

    /**
     * Object definition within a dimension ({@code #OBJEKT}), e.g. a specific department
     * or project.
     *
     * @param dimensionNo the parent dimension
     * @param objectNo identifier for this object within the dimension
     * @param objectName human-readable name
     */
    record Objekt(int dimensionNo, String objectNo, String objectName) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OBJEKT;
        }
    }

    /**
     * Opening balance per object for an account ({@code #OIB}).
     *
     * @param yearNumber the fiscal year
     * @param accountNo the account number
     * @param objectReference the dimension/object this balance belongs to
     * @param balance the opening balance
     * @param quantity optional opening quantity
     */
    record Oib(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OIB;
        }
    }

    /**
     * The last date covered by the data in this file ({@code #OMFATTN}).
     * Used when the file does not contain a full fiscal year.
     *
     * @param date the last date included
     */
    record Omfattn(LocalDate date) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OMFATTN;
        }
    }

    /**
     * Organisation number ({@code #ORGNR}).
     *
     * @param orgNr the Swedish organisation number
     * @param acqNo optional acquisition number (used by some legal forms)
     * @param actNo optional activity number; only valid when {@code acqNo} is present
     */
    record OrgNr(String orgNr, Optional<Integer> acqNo, Optional<Integer> actNo) implements SIE4Item {
        public OrgNr {
            if (actNo.isPresent() && acqNo.isEmpty()) {
                throw new IllegalArgumentException("actNo requires acqNo to be present");
            }
        }

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.ORGNR;
        }

        public static OrgNr of(String orgNr) {
            return new OrgNr(orgNr, Optional.empty(), Optional.empty());
        }
    }

    /**
     * Closing balance per object for an account ({@code #OUB}).
     *
     * @param yearNumber the fiscal year
     * @param accountNo the account number
     * @param objectReference the dimension/object this balance belongs to
     * @param balance the closing balance
     * @param quantity optional closing quantity
     */
    record Oub(YearNumber yearNumber, int accountNo, ObjectReference objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.OUB;
        }
    }

    /**
     * Budgeted period balance per account and object ({@code #PBUDGET}).
     *
     * @param yearNumber the fiscal year
     * @param period the accounting period
     * @param accountNo the account number
     * @param objectReference optional dimension/object filter
     * @param balance the budgeted balance for the period
     * @param quantity optional budgeted quantity
     */
    record Pbudget(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PBUDGET;
        }
    }

    /**
     * Name and version of the accounting program that created the file ({@code #PROGRAM}).
     *
     * @param programName the program name
     * @param version the program version
     */
    record Program(String programName, String version) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PROGRAM;
        }

    }

    /**
     * Free-text comment about the file ({@code #PROSA}).
     *
     * @param comment the comment text
     */
    record Prosa(String comment) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PROSA;
        }
    }

    /**
     * Actual period balance per account and object ({@code #PSALDO}).
     *
     * @param yearNumber the fiscal year
     * @param period the accounting period
     * @param accountNo the account number
     * @param objectReference optional dimension/object filter
     * @param balance the actual balance for the period
     * @param quantity optional actual quantity
     */
    record Psaldo(YearNumber yearNumber, Period period, int accountNo, Optional<ObjectReference> objectReference, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.PSALDO;
        }
    }

    /**
     * Fiscal year date range ({@code #RAR}).
     * Year 0 is the current fiscal year, -1 the previous year, and so on.
     *
     * @param yearNumber the fiscal year index
     * @param start first date of the fiscal year
     * @param end last date of the fiscal year
     */
    record Rar(YearNumber yearNumber, LocalDate start, LocalDate end) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.RAR;
        }

    }

    /**
     * Closing balance (result) for an income or expense account ({@code #RES}).
     *
     * @param yearNumber the fiscal year
     * @param accountNo the account number
     * @param balance the closing balance
     * @param quantity optional closing quantity
     */
    record Res(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.RES;
        }

    }

    /**
     * SIE file type declaration ({@code #SIETYP}). This library supports type 4 only.
     * Use the constant {@link #SIE_4} rather than constructing this directly.
     *
     * @param typeNo the SIE file type number; must be {@code 4} for this library
     */
    record Sietyp(int typeNo) implements SIE4Item {
        /** The only SIE type supported by this library. */
        public static final Sietyp SIE_4 = Sietyp.of(4);

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.SIETYP;
        }

        public static Sietyp of(int typeNo) {
            return new Sietyp(typeNo);
        }
    }

    /**
     * Mapping from an account to a Swedish tax return (SRU) code ({@code #SRU}).
     *
     * @param accountNo the account number
     * @param sruCode the SRU code used in tax filings
     */
    record Sru(int accountNo, int sruCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.SRU;
        }
    }

    /**
     * Tax year the file covers ({@code #TAXAR}).
     *
     * @param year the four-digit tax year
     */
    record Taxar(int year) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.TAXAR;
        }
    }

    /**
     * Closing balance for an account in a given fiscal year ({@code #UB}).
     *
     * @param yearNumber the fiscal year; 0 = current year, -1 = previous year, etc.
     * @param accountNo the account number
     * @param balance the closing balance
     * @param quantity optional closing quantity for accounts that track units
     */
    record Ub(YearNumber yearNumber, int accountNo, BigDecimal balance, Optional<BigDecimal> quantity) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.UB;
        }

    }

    /**
     * Sub-dimension nested within a parent dimension ({@code #UNDERDIM}).
     *
     * @param dimensionNo identifier for this sub-dimension
     * @param name human-readable name
     * @param superDimensionNo the identifier of the parent dimension
     */
    record Underdim(int dimensionNo, String name, int superDimensionNo) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.UNDERDIM;
        }
    }

    /**
     * Currency used in the file ({@code #VALUTA}). Defaults to SEK when absent.
     *
     * @param currencyCode ISO 4217 currency code, e.g. {@code "SEK"} or {@code "EUR"}
     */
    record Valuta(String currencyCode) implements SIE4Item {
        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.VALUTA;
        }
    }

    /**
     * A voucher (verification) containing one or more accounting transactions ({@code #VER}).
     * The amounts of all {@link Transaction.Trans} entries must sum to zero.
     *
     * <p>Use {@link #of(LocalDate, String, List)} or
     * {@link #of(LocalDate, String, String, String, List)} as convenient factory methods.
     *
     * @param date the voucher date
     * @param series optional voucher series identifier
     * @param verificationNo optional voucher number within the series
     * @param text optional description
     * @param regDate optional date the voucher was registered in the accounting system
     * @param sign optional initials of the person who registered the voucher
     * @param transactions the accounting transactions; must contain at least two entries
     *                     and all {@link Transaction.Trans} amounts must sum to zero
     */
    record Ver(LocalDate date, Optional<String> series, Optional<String> verificationNo, Optional<String> text, Optional<LocalDate> regDate, Optional<String> sign, List<Transaction> transactions) implements SIE4Item {
        public Ver {
            if (transactions == null || transactions.size() < 2) {
                throw new SIE4Exception("VER items must have at least two transactions");
            }
            BigDecimal sum = transactions.stream().filter(t -> t instanceof Transaction.Trans).map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.compareTo(BigDecimal.ZERO) != 0) {
                throw new SIE4Exception("VER transaction items must have a zero sum, was: " + sum);
            }
            transactions = List.copyOf(transactions);
        }

        @Override
        public SIE4ItemType itemType() {
            return SIE4ItemType.VER;
        }

        public static Ver of(LocalDate date, String text, String series, String verificationNumber, List<Transaction> transactions) {
            return new Ver(date, Optional.of(series), Optional.of(verificationNumber), Optional.of(text), Optional.empty(), Optional.empty(), transactions);
        }

        public static Ver of(LocalDate date, String text, List<Transaction> transactions) {
            return new Ver(date, Optional.empty(), Optional.empty(), Optional.of(text), Optional.empty(), Optional.empty(), transactions);
        }
    }

    /**
     * A single accounting transaction within a {@link Ver} voucher.
     * All three implementing types ({@link Trans}, {@link Rtrans}, {@link Btrans}) share
     * the same fields.
     */
    sealed interface Transaction extends SIE4Item {
        int accountNo();
        BigDecimal amount();
        List<ObjectReference> objectReferences();
        Optional<LocalDate> transactionDate();
        Optional<String> text();
        Optional<BigDecimal> quantity();
        Optional<String> sign();

        /**
         * A regular accounting transaction ({@code #TRANS}).
         * Use {@link #of(int, BigDecimal)} when only account and amount are needed.
         *
         * @param accountNo the account number
         * @param amount the transaction amount (positive = debit, negative = credit)
         * @param objectReferences dimension/object allocations for this transaction
         * @param transactionDate optional transaction date if different from the voucher date
         * @param text optional description
         * @param quantity optional quantity
         * @param sign optional initials of the person who entered the transaction
         */
        record Trans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            public Trans {
                objectReferences = List.copyOf(objectReferences);
            }

            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.TRANS;
            }

            public static Trans of(int accountNo, BigDecimal amount) {
                return new Trans(accountNo, amount, List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
            }
        }

        /**
         * A transaction to be added during SIE4I import ({@code #RTRANS}).
         * Used to correct a previously imported voucher by adding a new transaction.
         *
         * @param accountNo the account number
         * @param amount the transaction amount
         * @param objectReferences dimension/object allocations
         * @param transactionDate optional transaction date
         * @param text optional description
         * @param quantity optional quantity
         * @param sign optional initials
         */
        record Rtrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            public Rtrans {
                objectReferences = List.copyOf(objectReferences);
            }

            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.RTRANS;
            }

            public static Rtrans of(int accountNo, BigDecimal amount) {
                return new Rtrans(accountNo, amount, List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
            }
        }

        /**
         * A transaction to be removed during SIE4I import ({@code #BTRANS}).
         * Used to correct a previously imported voucher by removing an existing transaction.
         *
         * @param accountNo the account number
         * @param amount the transaction amount
         * @param objectReferences dimension/object allocations
         * @param transactionDate optional transaction date
         * @param text optional description
         * @param quantity optional quantity
         * @param sign optional initials
         */
        record Btrans(int accountNo, BigDecimal amount, List<ObjectReference> objectReferences, Optional<LocalDate> transactionDate, Optional<String> text, Optional<BigDecimal> quantity, Optional<String> sign) implements Transaction {
            public Btrans {
                objectReferences = List.copyOf(objectReferences);
            }

            @Override
            public SIE4ItemType itemType() {
                return SIE4ItemType.BTRANS;
            }

            public static Btrans of(int accountNo, BigDecimal amount) {
                return new Btrans(accountNo, amount, List.of(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
            }
        }
    }
}
