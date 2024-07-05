package se.bufferoverflow.sieport.sie4.parser;

import se.bufferoverflow.sieport.sie4.CompanyType;
import se.bufferoverflow.sieport.sie4.Constants;
import se.bufferoverflow.sieport.sie4.FileItem;
import se.bufferoverflow.sieport.sie4.ObjectReference;
import se.bufferoverflow.sieport.sie4.Period;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FieldMapper {
    enum Label {
        ADRESS,
        BKOD,
        DIM,
        ENHET,
        FLAGGA,
        FNAMN,
        FNR,
        FORMAT,
        FTYP,
        GEN,
        IB,
        KONTO,
        KPTYP,
        KTYP,
        OBJEKT,
        OIB,
        OMFATTN,
        ORGNR,
        OUB,
        PBUDGET,
        PROGRAM,
        PROSA,
        PSALDO,
        RAR,
        RES,
        SIETYP,
        SRU,
        TAXAR,
        TRANS,
        RTRANS,
        BTRANS,
        UB,
        UNDERDIM,
        VALUTA,
        VER
    }

    private static final Map<Label, AbstractFieldParser<?>> PARSER_REGISTRY = Map.ofEntries(
            Map.entry(Label.ADRESS, new AbstractFieldParser<FileItem.Adress>() {
                @Override
                protected FileItem.Adress parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 4) {
                        throw new IllegalArgumentException("Label ADRESS requires 4 fields");
                    }
                    return new FileItem.Adress(fields.get(0), fields.get(1), fields.get(2), fields.get(3));
                }
            }),
            Map.entry(Label.BKOD, new AbstractFieldParser<FileItem.Bkod>() {
                @Override
                protected FileItem.Bkod parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label BKOD requires 1 field");
                    }
                    return new FileItem.Bkod(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.DIM, new AbstractFieldParser<FileItem.Dim>() {
                @Override
                protected FileItem.Dim parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label DIM requires 2 fields");
                    }
                    return new FileItem.Dim(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.ENHET, new AbstractFieldParser<FileItem.Enhet>() {
                @Override
                protected FileItem.Enhet parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label ENHET requires 2 fields");
                    }
                    return new FileItem.Enhet(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.FLAGGA, new AbstractFieldParser<FileItem.Flagga>() {
                @Override
                protected FileItem.Flagga parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label FLAGGA requires 1 field");
                    }
                    return new FileItem.Flagga(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.FNAMN, new AbstractFieldParser<FileItem.Fnamn>() {
                @Override
                protected FileItem.Fnamn parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label FNAMN requires 1 field");
                    }
                    return new FileItem.Fnamn(fields.getFirst());
                }
            }),
            Map.entry(Label.FNR, new AbstractFieldParser<FileItem.Fnr>() {
                @Override
                protected FileItem.Fnr parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label FNR requires 1 field");
                    }
                    return new FileItem.Fnr(fields.getFirst());
                }
            }),

            Map.entry(Label.FORMAT, new AbstractFieldParser<FileItem.Format>() {
                @Override
                protected FileItem.Format parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label FORMAT requires 1 field");
                    }
                    if (!fields.getFirst().equalsIgnoreCase("PC8")) {
                        throw new IllegalArgumentException("Standard only allows FORMAT to be PC8");
                    }
                    return new FileItem.Format(fields.getFirst());
                }
            }),
            Map.entry(Label.FTYP, new AbstractFieldParser<FileItem.Ftyp>() {
                @Override
                protected FileItem.Ftyp parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label FTYP requires 1 field");
                    }
                    return new FileItem.Ftyp(CompanyType.valueOf(fields.getFirst().toUpperCase()));
                }
            }),
            Map.entry(Label.GEN, new AbstractFieldParser<FileItem.Gen>() {
                @Override
                protected FileItem.Gen parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.isEmpty() || fields.size() > 2) {
                        throw new IllegalArgumentException("Label GEN requires at least 1 field, but max 2");
                    }
                    LocalDate date = LocalDate.parse(fields.get(0), Constants.SIE4_DATE_FORMATTER);

                    Optional<String> signature = Optional.empty();
                    if (fields.size() > 1) {
                        signature = parseOptionalField(fields.get(1));
                    }

                    return new FileItem.Gen(date, signature);
                }
            }),
            Map.entry(Label.IB, new AbstractFieldParser<FileItem.Ib>() {
                @Override
                protected FileItem.Ib parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new IllegalArgumentException("Label IB requires 3 or 4 fields");
                    }

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new FileItem.Ib(
                            YearNumber.of(Integer.parseInt(fields.get(0))),
                            Integer.parseInt(fields.get(1)),
                            new BigDecimal(fields.get(2)),
                            quantity);
                }
            }),
            Map.entry(Label.KONTO, new AbstractFieldParser<FileItem.Konto>() {
                @Override
                protected FileItem.Konto parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label KONTO requires 2 fields");
                    }
                    return new FileItem.Konto(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.KPTYP, new AbstractFieldParser<FileItem.Kptyp>() {
                @Override
                protected FileItem.Kptyp parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label KPTYP requires 1 field");
                    }
                    return new FileItem.Kptyp(fields.getFirst());
                }
            }),
            Map.entry(Label.KTYP, new AbstractFieldParser<FileItem.Ktyp>() {
                @Override
                protected FileItem.Ktyp parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label KTYP requires 2 fields");
                    }
                    return new FileItem.Ktyp(
                            Integer.parseInt(fields.get(0)),
                            FileItem.Ktyp.AccountType.valueOf(fields.get(1).toUpperCase()));
                }
            }),
            Map.entry(Label.OBJEKT, new AbstractFieldParser<FileItem.Objekt>() {
                @Override
                protected FileItem.Objekt parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 3) {
                        throw new IllegalArgumentException("Label OBJEKT requires 3 fields");
                    }
                    return new FileItem.Objekt(Integer.parseInt(fields.get(0)), fields.get(1), fields.get(2));
                }
            }),
            Map.entry(Label.OIB, new AbstractFieldParser<FileItem.Oib>() {
                @Override
                protected FileItem.Oib parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 4 || fields.size() > 5) {
                        throw new IllegalArgumentException("Label OIB requires 4 or 5 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(2));
                    if (objectReferences.size() != 1) {
                        throw new IllegalArgumentException("Label OIB requires 1 object reference");
                    }
                    ObjectReference objectReference = objectReferences.getFirst();

                    BigDecimal balance = new BigDecimal(fields.get(3));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 5) {
                        quantity = Optional.of(new BigDecimal(fields.get(4)));
                    }

                    return new FileItem.Oib(yearNumber, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.OMFATTN, new AbstractFieldParser<FileItem.Omfattn>() {
                @Override
                protected FileItem.Omfattn parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label OMFATTN requires 1 field");
                    }
                    LocalDate date = LocalDate.parse(fields.getFirst(), Constants.SIE4_DATE_FORMATTER);
                    return new FileItem.Omfattn(date);
                }
            }),
            Map.entry(Label.ORGNR, new AbstractFieldParser<FileItem.OrgNr>() {
                @Override
                protected FileItem.OrgNr parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.isEmpty() || fields.size() > 3) {
                        throw new IllegalArgumentException("Label ORGNR requires 1 to 3 fields");
                    }
                    String orgNr = fields.get(0);
                    Optional<Integer> acqNo = Optional.empty();
                    if (fields.size() > 1) {
                        acqNo = Optional.of(Integer.parseInt(fields.get(1)));
                    }
                    Optional<Integer> actNo = Optional.empty();
                    if (fields.size() > 2) {
                        actNo = Optional.of(Integer.parseInt(fields.get(2)));
                    }
                    return new FileItem.OrgNr(orgNr, acqNo, actNo);
                }
            }),
            Map.entry(Label.OUB, new AbstractFieldParser<FileItem.Oub>() {
                @Override
                protected FileItem.Oub parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 4 || fields.size() > 5) {
                        throw new IllegalArgumentException("Label OUB requires 4 or 5 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));

                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(2));
                    if (objectReferences.size() != 1) {
                        throw new IllegalArgumentException("Label OUB requires 1 object reference");
                    }
                    ObjectReference objectReference = objectReferences.getFirst();

                    BigDecimal balance = new BigDecimal(fields.get(3));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 5) {
                        quantity = Optional.of(new BigDecimal(fields.get(4)));
                    }

                    return new FileItem.Oub(yearNumber, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.PBUDGET, new AbstractFieldParser<FileItem.Pbudget>() {
                @Override
                protected FileItem.Pbudget parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 5 || fields.size() > 6) {
                        throw new IllegalArgumentException("Label PBUDGET requires 5 or 6 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    Period period = Period.of(fields.get(1));
                    int accountNo = Integer.parseInt(fields.get(2));

                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(3));
                    if (objectReferences.size() > 1) {
                        throw new IllegalArgumentException("Label PBUDGET can have at most 1 object reference");
                    }
                    Optional<ObjectReference> objectReference = objectReferences.stream().findFirst();

                    BigDecimal balance = new BigDecimal(fields.get(4));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 6) {
                        quantity = Optional.of(new BigDecimal(fields.get(5)));
                    }

                    return new FileItem.Pbudget(yearNumber, period, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.PROGRAM, new AbstractFieldParser<FileItem.Program>() {
                @Override
                protected FileItem.Program parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label PROGRAM requires 2 fields");
                    }
                    return new FileItem.Program(fields.get(0), fields.get(1));
                }
            }),
            Map.entry(Label.PROSA, new AbstractFieldParser<FileItem.Prosa>() {
                @Override
                protected FileItem.Prosa parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label PROSA requires 1 field");
                    }
                    return new FileItem.Prosa(fields.getFirst());
                }
            }),
            Map.entry(Label.PSALDO, new AbstractFieldParser<FileItem.Psaldo>() {
                @Override
                protected FileItem.Psaldo parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 5 || fields.size() > 6) {
                        throw new IllegalArgumentException("Label PSALDO requires 5 or 6 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    Period period = Period.of(fields.get(1));
                    int accountNo = Integer.parseInt(fields.get(2));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(3));
                    if (objectReferences.size() > 1) {
                        throw new IllegalArgumentException("Label PSALDO can have at most 1 object reference");
                    }
                    Optional<ObjectReference> objectReference = objectReferences.stream().findFirst();

                    BigDecimal balance = new BigDecimal(fields.get(4));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 6) {
                        quantity = Optional.of(new BigDecimal(fields.get(5)));
                    }

                    return new FileItem.Psaldo(yearNumber, period, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.RAR, new AbstractFieldParser<FileItem.Rar>() {
                @Override
                protected FileItem.Rar parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 3) {
                        throw new IllegalArgumentException("Label RAR requires 3 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    LocalDate start = LocalDate.parse(fields.get(1), Constants.SIE4_DATE_FORMATTER);
                    LocalDate end = LocalDate.parse(fields.get(2), Constants.SIE4_DATE_FORMATTER);

                    return new FileItem.Rar(yearNumber, start, end);
                }
            }),
            Map.entry(Label.RES, new AbstractFieldParser<FileItem.Res>() {
                @Override
                protected FileItem.Res parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new IllegalArgumentException("Label RES requires 3 or 4 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    BigDecimal balance = new BigDecimal(fields.get(2));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new FileItem.Res(yearNumber, accountNo, balance, quantity);
                }
            }),
            Map.entry(Label.SIETYP, new AbstractFieldParser<FileItem.Sietyp>() {
                @Override
                protected FileItem.Sietyp parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label SIETYP requires 1 field");
                    }
                    return new FileItem.Sietyp(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.SRU, new AbstractFieldParser<FileItem.Sru>() {
                @Override
                protected FileItem.Sru parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 2) {
                        throw new IllegalArgumentException("Label SRU requires 2 fields");
                    }
                    return new FileItem.Sru(Integer.parseInt(fields.get(0)), Integer.parseInt(fields.get(1)));
                }
            }),
            Map.entry(Label.TAXAR, new AbstractFieldParser<FileItem.Taxar>() {
                @Override
                protected FileItem.Taxar parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label TAXAR requires 1 field");
                    }
                    return new FileItem.Taxar(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.TRANS, new AbstractFieldParser<FileItem.Transaction.Trans>() {
                @Override
                protected FileItem.Transaction.Trans parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new IllegalArgumentException("Label TRANS requires between 2 and 7 fields");
                    }

                    int accountNo = Integer.parseInt(fields.get(0));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(1));
                    BigDecimal amount = new BigDecimal(fields.get(2));

                    Optional<LocalDate> transactionDate = Optional.empty();
                    if (fields.size() > 3) {
                        transactionDate = parseOptionalField(fields.get(3))
                                .map(value -> LocalDate.parse(value, Constants.SIE4_DATE_FORMATTER));
                    }

                    Optional<String> text = Optional.empty();
                    if (fields.size() > 4) {
                        text = parseOptionalField(fields.get(4));
                    }

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() > 5) {
                        quantity = parseOptionalField(fields.get(5)).map(BigDecimal::new);
                    }

                    Optional<String> sign = Optional.empty();
                    if (fields.size() > 6) {
                        sign = parseOptionalField(fields.get(6));
                    }

                    return new FileItem.Transaction.Trans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.RTRANS, new AbstractFieldParser<FileItem.Transaction.Rtrans>() {
                @Override
                protected FileItem.Transaction.Rtrans parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new IllegalArgumentException("Label RTRANS requires between 2 and 7 fields");
                    }

                    int accountNo = Integer.parseInt(fields.get(0));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(1));
                    BigDecimal amount = new BigDecimal(fields.get(2));

                    Optional<LocalDate> transactionDate = Optional.empty();
                    if (fields.size() > 3) {
                        transactionDate = parseOptionalField(fields.get(3))
                                .map(value -> LocalDate.parse(value, Constants.SIE4_DATE_FORMATTER));
                    }

                    Optional<String> text = Optional.empty();
                    if (fields.size() > 4) {
                        text = parseOptionalField(fields.get(4));
                    }

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() > 5) {
                        quantity = parseOptionalField(fields.get(5)).map(BigDecimal::new);
                    }

                    Optional<String> sign = Optional.empty();
                    if (fields.size() > 6) {
                        sign = parseOptionalField(fields.get(6));
                    }

                    return new FileItem.Transaction.Rtrans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.BTRANS, new AbstractFieldParser<FileItem.Transaction.Btrans>() {
                @Override
                protected FileItem.Transaction.Btrans parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new IllegalArgumentException("Label BTRANS requires between 2 and 7 fields");
                    }

                    int accountNo = Integer.parseInt(fields.get(0));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(1));
                    BigDecimal amount = new BigDecimal(fields.get(2));

                    Optional<LocalDate> transactionDate = Optional.empty();
                    if (fields.size() > 3) {
                        transactionDate = parseOptionalField(fields.get(3))
                                .map(value -> LocalDate.parse(value, Constants.SIE4_DATE_FORMATTER));
                    }

                    Optional<String> text = Optional.empty();
                    if (fields.size() > 4) {
                        text = parseOptionalField(fields.get(4));
                    }

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() > 5) {
                        quantity = parseOptionalField(fields.get(5)).map(BigDecimal::new);
                    }

                    Optional<String> sign = Optional.empty();
                    if (fields.size() > 6) {
                        sign = parseOptionalField(fields.get(6));
                    }

                    return new FileItem.Transaction.Btrans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.UB, new AbstractFieldParser<FileItem.Ub>() {
                @Override
                protected FileItem.Ub parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new IllegalArgumentException("Label UB requires 3 or 4 fields");
                    }
                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    BigDecimal balance = new BigDecimal(fields.get(2));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new FileItem.Ub(yearNumber, accountNo, balance, quantity);
                }
            }),
            Map.entry(Label.UNDERDIM, new AbstractFieldParser<FileItem.Underdim>() {
                @Override
                protected FileItem.Underdim parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 3) {
                        throw new IllegalArgumentException("Label UNDERDIM requires 3 fields");
                    }
                    return new FileItem.Underdim(
                            Integer.parseInt(fields.get(0)),
                            fields.get(1),
                            Integer.parseInt(fields.get(2))
                    );
                }
            }),
            Map.entry(Label.VALUTA, new AbstractFieldParser<FileItem.Valuta>() {
                @Override
                protected FileItem.Valuta parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.size() != 1) {
                        throw new IllegalArgumentException("Label VALUTA requires 1 field");
                    }
                    return new FileItem.Valuta(fields.getFirst());
                }
            }),
            Map.entry(Label.VER, new AbstractFieldParser<FileItem.Ver>() {
                @Override
                protected FileItem.Ver parseFields(List<String> fields, List<FileItem> subItems) {
                    if (fields.isEmpty() || fields.size() > 6) {
                        throw new IllegalArgumentException("Label VER requires between 1 and 6 fields");
                    }

                    Optional<String> series = parseOptionalField(fields.get(0));
                    Optional<String> verificationNo = parseOptionalField(fields.get(1));
                    LocalDate date = LocalDate.parse(fields.get(2), Constants.SIE4_DATE_FORMATTER);

                    Optional<String> text = Optional.empty();
                    if (fields.size() > 3 && !fields.get(3).isEmpty()) {
                        text = parseOptionalField(fields.get(3));
                    }

                    Optional<LocalDate> regDate = Optional.empty();
                    if (fields.size() > 4 && !fields.get(4).isEmpty()) {
                        regDate = parseOptionalField(fields.get(4))
                                .map(value -> LocalDate.parse(value, Constants.SIE4_DATE_FORMATTER));
                    }

                    Optional<String> sign = Optional.empty();
                    if (fields.size() > 5 && !fields.get(5).isEmpty()) {
                        sign = parseOptionalField(fields.get(5));
                    }

                    if (subItems.stream().anyMatch(t -> !(t instanceof FileItem.Transaction))) {
                        throw new IllegalArgumentException("All subItems must be transactions");
                    }
                    List<FileItem.Transaction> transactions = subItems.stream().map(t -> (FileItem.Transaction) t).toList();

                    return new FileItem.Ver(date, series, verificationNo, text, regDate, sign, transactions);
                }
            })
    );

    public static FileItem toModel(String itemLine) {
        LabelWithFields labelWithFields = splitLine(itemLine);

        if (labelWithFields.label() == Label.VER) {
            throw new IllegalArgumentException("#VER items cannot be parsed by this function");
        }

        return PARSER_REGISTRY.get(labelWithFields.label()).parseFields(labelWithFields.fields());
    }

    public static FileItem.Ver toModel(List<String> itemLines) {
        String verLine = itemLines.getFirst();
        LabelWithFields labelWithFields = splitLine(verLine);

        if (labelWithFields.label() != Label.VER) {
            throw new IllegalArgumentException("Only #VER items can be parsed by this function");
        }

        List<FileItem> transactions = itemLines.stream()
                .skip(1)
                .map(FieldMapper::toModel)
                .toList();

        return (FileItem.Ver) PARSER_REGISTRY.get(Label.VER).parseFields(labelWithFields.fields(), transactions);
    }

    private static LabelWithFields splitLine(String itemLine) {
        if (itemLine == null || itemLine.isBlank()) {
            throw new IllegalArgumentException("ItemLine cannot be null or blank");
        }

        String[] split = itemLine.split("\\s+", 2);
        Label label = Label.valueOf(split[0].strip().substring(1).toUpperCase());
        String fields = split[1].strip();
        return new LabelWithFields(label, fields);
    }

    private record LabelWithFields(Label label, String fields) {}
}
