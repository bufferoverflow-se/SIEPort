package se.bufferoverflow.sieport.sie4.parser;

import se.bufferoverflow.sieport.sie4.CompanyType;
import se.bufferoverflow.sieport.sie4.Constants;
import se.bufferoverflow.sieport.sie4.SIE4Exception;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.ObjectReference;
import se.bufferoverflow.sieport.sie4.Period;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InFieldMapper {

    private static final Map<Label, AbstractFieldParser<?>> PARSER_REGISTRY = Map.ofEntries(
            Map.entry(Label.ADRESS, new AbstractFieldParser<SIE4Item.Adress>() {
                @Override
                protected SIE4Item.Adress parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 4) {
                        throw new SIE4Exception("Label ADRESS requires 4 fields");
                    }
                    return new SIE4Item.Adress(fields.get(0), fields.get(1), fields.get(2), fields.get(3));
                }
            }),
            Map.entry(Label.BKOD, new AbstractFieldParser<SIE4Item.Bkod>() {
                @Override
                protected SIE4Item.Bkod parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label BKOD requires 1 field");
                    }
                    return new SIE4Item.Bkod(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.DIM, new AbstractFieldParser<SIE4Item.Dim>() {
                @Override
                protected SIE4Item.Dim parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label DIM requires 2 fields");
                    }
                    return new SIE4Item.Dim(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.ENHET, new AbstractFieldParser<SIE4Item.Enhet>() {
                @Override
                protected SIE4Item.Enhet parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label ENHET requires 2 fields");
                    }
                    return new SIE4Item.Enhet(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.FLAGGA, new AbstractFieldParser<SIE4Item.Flagga>() {
                @Override
                protected SIE4Item.Flagga parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label FLAGGA requires 1 field");
                    }
                    return new SIE4Item.Flagga(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.FNAMN, new AbstractFieldParser<SIE4Item.Fnamn>() {
                @Override
                protected SIE4Item.Fnamn parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label FNAMN requires 1 field");
                    }
                    return new SIE4Item.Fnamn(fields.getFirst());
                }
            }),
            Map.entry(Label.FNR, new AbstractFieldParser<SIE4Item.Fnr>() {
                @Override
                protected SIE4Item.Fnr parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label FNR requires 1 field");
                    }
                    return new SIE4Item.Fnr(fields.getFirst());
                }
            }),

            Map.entry(Label.FORMAT, new AbstractFieldParser<SIE4Item.Format>() {
                @Override
                protected SIE4Item.Format parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label FORMAT requires 1 field");
                    }
                    if (!fields.getFirst().equalsIgnoreCase("PC8")) {
                        throw new SIE4Exception("Standard only allows FORMAT to be PC8");
                    }
                    return new SIE4Item.Format(fields.getFirst());
                }
            }),
            Map.entry(Label.FTYP, new AbstractFieldParser<SIE4Item.Ftyp>() {
                @Override
                protected SIE4Item.Ftyp parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label FTYP requires 1 field");
                    }
                    return new SIE4Item.Ftyp(CompanyType.valueOf(fields.getFirst().toUpperCase()));
                }
            }),
            Map.entry(Label.GEN, new AbstractFieldParser<SIE4Item.Gen>() {
                @Override
                protected SIE4Item.Gen parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.isEmpty() || fields.size() > 2) {
                        throw new SIE4Exception("Label GEN requires at least 1 field, but max 2");
                    }
                    LocalDate date = LocalDate.parse(fields.get(0), Constants.SIE4_DATE_FORMATTER);

                    Optional<String> signature = Optional.empty();
                    if (fields.size() > 1) {
                        signature = parseOptionalField(fields.get(1));
                    }

                    return new SIE4Item.Gen(date, signature);
                }
            }),
            Map.entry(Label.IB, new AbstractFieldParser<SIE4Item.Ib>() {
                @Override
                protected SIE4Item.Ib parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new SIE4Exception("Label IB requires 3 or 4 fields");
                    }

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new SIE4Item.Ib(
                            YearNumber.of(Integer.parseInt(fields.get(0))),
                            Integer.parseInt(fields.get(1)),
                            new BigDecimal(fields.get(2)),
                            quantity);
                }
            }),
            Map.entry(Label.KONTO, new AbstractFieldParser<SIE4Item.Konto>() {
                @Override
                protected SIE4Item.Konto parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label KONTO requires 2 fields");
                    }
                    return new SIE4Item.Konto(Integer.parseInt(fields.get(0)), fields.get(1));
                }
            }),
            Map.entry(Label.KPTYP, new AbstractFieldParser<SIE4Item.Kptyp>() {
                @Override
                protected SIE4Item.Kptyp parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label KPTYP requires 1 field");
                    }
                    return new SIE4Item.Kptyp(fields.getFirst());
                }
            }),
            Map.entry(Label.KTYP, new AbstractFieldParser<SIE4Item.Ktyp>() {
                @Override
                protected SIE4Item.Ktyp parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label KTYP requires 2 fields");
                    }
                    return new SIE4Item.Ktyp(
                            Integer.parseInt(fields.get(0)),
                            SIE4Item.Ktyp.AccountType.valueOf(fields.get(1).toUpperCase()));
                }
            }),
            Map.entry(Label.OBJEKT, new AbstractFieldParser<SIE4Item.Objekt>() {
                @Override
                protected SIE4Item.Objekt parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 3) {
                        throw new SIE4Exception("Label OBJEKT requires 3 fields");
                    }
                    return new SIE4Item.Objekt(Integer.parseInt(fields.get(0)), fields.get(1), fields.get(2));
                }
            }),
            Map.entry(Label.OIB, new AbstractFieldParser<SIE4Item.Oib>() {
                @Override
                protected SIE4Item.Oib parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 4 || fields.size() > 5) {
                        throw new SIE4Exception("Label OIB requires 4 or 5 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(2));
                    if (objectReferences.size() != 1) {
                        throw new SIE4Exception("Label OIB requires 1 object reference");
                    }
                    ObjectReference objectReference = objectReferences.getFirst();

                    BigDecimal balance = new BigDecimal(fields.get(3));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 5) {
                        quantity = Optional.of(new BigDecimal(fields.get(4)));
                    }

                    return new SIE4Item.Oib(yearNumber, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.OMFATTN, new AbstractFieldParser<SIE4Item.Omfattn>() {
                @Override
                protected SIE4Item.Omfattn parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label OMFATTN requires 1 field");
                    }
                    LocalDate date = LocalDate.parse(fields.getFirst(), Constants.SIE4_DATE_FORMATTER);
                    return new SIE4Item.Omfattn(date);
                }
            }),
            Map.entry(Label.ORGNR, new AbstractFieldParser<SIE4Item.OrgNr>() {
                @Override
                protected SIE4Item.OrgNr parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.isEmpty() || fields.size() > 3) {
                        throw new SIE4Exception("Label ORGNR requires 1 to 3 fields");
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
                    return new SIE4Item.OrgNr(orgNr, acqNo, actNo);
                }
            }),
            Map.entry(Label.OUB, new AbstractFieldParser<SIE4Item.Oub>() {
                @Override
                protected SIE4Item.Oub parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 4 || fields.size() > 5) {
                        throw new SIE4Exception("Label OUB requires 4 or 5 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));

                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(2));
                    if (objectReferences.size() != 1) {
                        throw new SIE4Exception("Label OUB requires 1 object reference");
                    }
                    ObjectReference objectReference = objectReferences.getFirst();

                    BigDecimal balance = new BigDecimal(fields.get(3));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 5) {
                        quantity = Optional.of(new BigDecimal(fields.get(4)));
                    }

                    return new SIE4Item.Oub(yearNumber, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.PBUDGET, new AbstractFieldParser<SIE4Item.Pbudget>() {
                @Override
                protected SIE4Item.Pbudget parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 5 || fields.size() > 6) {
                        throw new SIE4Exception("Label PBUDGET requires 5 or 6 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    Period period = Period.of(fields.get(1));
                    int accountNo = Integer.parseInt(fields.get(2));

                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(3));
                    if (objectReferences.size() > 1) {
                        throw new SIE4Exception("Label PBUDGET can have at most 1 object reference");
                    }
                    Optional<ObjectReference> objectReference = objectReferences.stream().findFirst();

                    BigDecimal balance = new BigDecimal(fields.get(4));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 6) {
                        quantity = Optional.of(new BigDecimal(fields.get(5)));
                    }

                    return new SIE4Item.Pbudget(yearNumber, period, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.PROGRAM, new AbstractFieldParser<SIE4Item.Program>() {
                @Override
                protected SIE4Item.Program parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label PROGRAM requires 2 fields");
                    }
                    return new SIE4Item.Program(fields.get(0), fields.get(1));
                }
            }),
            Map.entry(Label.PROSA, new AbstractFieldParser<SIE4Item.Prosa>() {
                @Override
                protected SIE4Item.Prosa parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label PROSA requires 1 field");
                    }
                    return new SIE4Item.Prosa(fields.getFirst());
                }
            }),
            Map.entry(Label.PSALDO, new AbstractFieldParser<SIE4Item.Psaldo>() {
                @Override
                protected SIE4Item.Psaldo parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 5 || fields.size() > 6) {
                        throw new SIE4Exception("Label PSALDO requires 5 or 6 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    Period period = Period.of(fields.get(1));
                    int accountNo = Integer.parseInt(fields.get(2));
                    List<ObjectReference> objectReferences = parseObjectReferences(fields.get(3));
                    if (objectReferences.size() > 1) {
                        throw new SIE4Exception("Label PSALDO can have at most 1 object reference");
                    }
                    Optional<ObjectReference> objectReference = objectReferences.stream().findFirst();

                    BigDecimal balance = new BigDecimal(fields.get(4));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 6) {
                        quantity = Optional.of(new BigDecimal(fields.get(5)));
                    }

                    return new SIE4Item.Psaldo(yearNumber, period, accountNo, objectReference, balance, quantity);
                }
            }),
            Map.entry(Label.RAR, new AbstractFieldParser<SIE4Item.Rar>() {
                @Override
                protected SIE4Item.Rar parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 3) {
                        throw new SIE4Exception("Label RAR requires 3 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    LocalDate start = LocalDate.parse(fields.get(1), Constants.SIE4_DATE_FORMATTER);
                    LocalDate end = LocalDate.parse(fields.get(2), Constants.SIE4_DATE_FORMATTER);

                    return new SIE4Item.Rar(yearNumber, start, end);
                }
            }),
            Map.entry(Label.RES, new AbstractFieldParser<SIE4Item.Res>() {
                @Override
                protected SIE4Item.Res parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new SIE4Exception("Label RES requires 3 or 4 fields");
                    }

                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    BigDecimal balance = new BigDecimal(fields.get(2));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new SIE4Item.Res(yearNumber, accountNo, balance, quantity);
                }
            }),
            Map.entry(Label.SIETYP, new AbstractFieldParser<SIE4Item.Sietyp>() {
                @Override
                protected SIE4Item.Sietyp parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label SIETYP requires 1 field");
                    }
                    return new SIE4Item.Sietyp(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.SRU, new AbstractFieldParser<SIE4Item.Sru>() {
                @Override
                protected SIE4Item.Sru parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 2) {
                        throw new SIE4Exception("Label SRU requires 2 fields");
                    }
                    return new SIE4Item.Sru(Integer.parseInt(fields.get(0)), Integer.parseInt(fields.get(1)));
                }
            }),
            Map.entry(Label.TAXAR, new AbstractFieldParser<SIE4Item.Taxar>() {
                @Override
                protected SIE4Item.Taxar parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label TAXAR requires 1 field");
                    }
                    return new SIE4Item.Taxar(Integer.parseInt(fields.getFirst()));
                }
            }),
            Map.entry(Label.TRANS, new AbstractFieldParser<SIE4Item.Transaction.Trans>() {
                @Override
                protected SIE4Item.Transaction.Trans parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new SIE4Exception("Label TRANS requires between 2 and 7 fields");
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

                    return new SIE4Item.Transaction.Trans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.RTRANS, new AbstractFieldParser<SIE4Item.Transaction.Rtrans>() {
                @Override
                protected SIE4Item.Transaction.Rtrans parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new SIE4Exception("Label RTRANS requires between 2 and 7 fields");
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

                    return new SIE4Item.Transaction.Rtrans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.BTRANS, new AbstractFieldParser<SIE4Item.Transaction.Btrans>() {
                @Override
                protected SIE4Item.Transaction.Btrans parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 2 || fields.size() > 7) {
                        throw new SIE4Exception("Label BTRANS requires between 2 and 7 fields");
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

                    return new SIE4Item.Transaction.Btrans(accountNo, amount, objectReferences, transactionDate, text, quantity, sign);
                }
            }),
            Map.entry(Label.UB, new AbstractFieldParser<SIE4Item.Ub>() {
                @Override
                protected SIE4Item.Ub parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() < 3 || fields.size() > 4) {
                        throw new SIE4Exception("Label UB requires 3 or 4 fields");
                    }
                    YearNumber yearNumber = YearNumber.of(Integer.parseInt(fields.get(0)));
                    int accountNo = Integer.parseInt(fields.get(1));
                    BigDecimal balance = new BigDecimal(fields.get(2));

                    Optional<BigDecimal> quantity = Optional.empty();
                    if (fields.size() == 4) {
                        quantity = Optional.of(new BigDecimal(fields.get(3)));
                    }

                    return new SIE4Item.Ub(yearNumber, accountNo, balance, quantity);
                }
            }),
            Map.entry(Label.UNDERDIM, new AbstractFieldParser<SIE4Item.Underdim>() {
                @Override
                protected SIE4Item.Underdim parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 3) {
                        throw new SIE4Exception("Label UNDERDIM requires 3 fields");
                    }
                    return new SIE4Item.Underdim(
                            Integer.parseInt(fields.get(0)),
                            fields.get(1),
                            Integer.parseInt(fields.get(2))
                    );
                }
            }),
            Map.entry(Label.VALUTA, new AbstractFieldParser<SIE4Item.Valuta>() {
                @Override
                protected SIE4Item.Valuta parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.size() != 1) {
                        throw new SIE4Exception("Label VALUTA requires 1 field");
                    }
                    return new SIE4Item.Valuta(fields.getFirst());
                }
            }),
            Map.entry(Label.VER, new AbstractFieldParser<SIE4Item.Ver>() {
                @Override
                protected SIE4Item.Ver parseFields(List<String> fields, List<SIE4Item> subItems) {
                    if (fields.isEmpty() || fields.size() > 6) {
                        throw new SIE4Exception("Label VER requires between 1 and 6 fields");
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

                    if (subItems.stream().anyMatch(t -> !(t instanceof SIE4Item.Transaction))) {
                        throw new SIE4Exception("All subItems must be transactions");
                    }
                    List<SIE4Item.Transaction> transactions = subItems.stream().map(t -> (SIE4Item.Transaction) t).toList();

                    return new SIE4Item.Ver(date, series, verificationNo, text, regDate, sign, transactions);
                }
            })
    );

    public static SIE4Item toModel(String itemLine) {
        LabelWithFields labelWithFields = splitLine(itemLine);

        if (labelWithFields.label() == Label.VER) {
            throw new SIE4Exception("#VER items cannot be parsed by this function");
        }

        return PARSER_REGISTRY.get(labelWithFields.label()).parseFields(labelWithFields.fields());
    }

    public static SIE4Item.Ver toModel(List<String> itemLines) {
        String verLine = itemLines.getFirst();
        LabelWithFields labelWithFields = splitLine(verLine);

        if (labelWithFields.label() != Label.VER) {
            throw new SIE4Exception("Only #VER items can be parsed by this function");
        }

        List<SIE4Item> transactions = itemLines.stream()
                .skip(1)
                .map(InFieldMapper::toModel)
                .toList();

        return (SIE4Item.Ver) PARSER_REGISTRY.get(Label.VER).parseFields(labelWithFields.fields(), transactions);
    }

    private static LabelWithFields splitLine(String itemLine) {
        if (itemLine == null || itemLine.isBlank()) {
            throw new SIE4Exception("ItemLine cannot be null or blank");
        }

        String[] split = itemLine.split("\\s+", 2);
        Label label = Label.valueOf(split[0].strip().substring(1).toUpperCase());
        String fields = split[1].strip();
        return new LabelWithFields(label, fields);
    }

    private record LabelWithFields(Label label, String fields) {}

    private enum Label {
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
}
