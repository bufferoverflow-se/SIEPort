package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.SIE4Item;

import java.util.Map;
import java.util.stream.Collectors;

import static se.bufferoverflow.sieport.sie4.SIE4Item.SIE4_DATE_FORMATTER;

public class OutFieldMapper {
    private OutFieldMapper() {}

    private static final Map<Class<? extends SIE4Item>, AbstractFieldWriter<? extends SIE4Item>> WRITER_REGISTRY = Map.ofEntries(
            Map.entry(SIE4Item.Adress.class, new AbstractFieldWriter<SIE4Item.Adress>() {
                @Override
                String writeFields(SIE4Item.Adress item) {
                    return "%s %s %s %s".formatted(quoted(item.contact()), quoted(item.distributionAddress()),
                            quoted(item.postalAddress()), quoted(item.tel()));
                }
            }),
            Map.entry(SIE4Item.Bkod.class, new AbstractFieldWriter<SIE4Item.Bkod>() {
                @Override
                String writeFields(SIE4Item.Bkod item) {
                    return "%d".formatted(item.sniCode());
                }
            }),
            Map.entry(SIE4Item.Dim.class, new AbstractFieldWriter<SIE4Item.Dim>() {
                @Override
                String writeFields(SIE4Item.Dim item) {
                    return "%d %s".formatted(item.dimensionNo(), quoted(item.name()));
                }
            }),
            Map.entry(SIE4Item.Enhet.class, new AbstractFieldWriter<SIE4Item.Enhet>() {
                @Override
                String writeFields(SIE4Item.Enhet item) {
                    return "%d %s".formatted(item.accountNo(), quoted(item.unit()));
                }
            }),
            Map.entry(SIE4Item.Flagga.class, new AbstractFieldWriter<SIE4Item.Flagga>() {
                @Override
                String writeFields(SIE4Item.Flagga item) {
                    return "%d".formatted(item.flag());
                }
            }),
            Map.entry(SIE4Item.Fnamn.class, new AbstractFieldWriter<SIE4Item.Fnamn>() {
                @Override
                String writeFields(SIE4Item.Fnamn item) {
                    return quoted(item.companyName());
                }
            }),
            Map.entry(SIE4Item.Fnr.class, new AbstractFieldWriter<SIE4Item.Fnr>() {
                @Override
                String writeFields(SIE4Item.Fnr item) {
                    return quoted(item.companyId());
                }
            }),
            Map.entry(SIE4Item.Format.class, new AbstractFieldWriter<SIE4Item.Format>() {
                @Override
                String writeFields(SIE4Item.Format item) {
                    return quoted(item.format());
                }
            }),
            Map.entry(SIE4Item.Ftyp.class, new AbstractFieldWriter<SIE4Item.Ftyp>() {
                @Override
                String writeFields(SIE4Item.Ftyp item) {
                    return item.companyType().name();
                }
            }),
            Map.entry(SIE4Item.Gen.class, new AbstractFieldWriter<SIE4Item.Gen>() {
                @Override
                String writeFields(SIE4Item.Gen item) {
                    return "%s %s".formatted(
                            SIE4_DATE_FORMATTER.format(item.date()),
                            item.signature().map(AbstractFieldWriter::quoted).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Ib.class, new AbstractFieldWriter<SIE4Item.Ib>() {
                @Override
                String writeFields(SIE4Item.Ib item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Konto.class, new AbstractFieldWriter<SIE4Item.Konto>() {
                @Override
                String writeFields(SIE4Item.Konto item) {
                    return "%d %s".formatted(item.accountNo(), quoted(item.accountName()));
                }
            }),
            Map.entry(SIE4Item.Kptyp.class, new AbstractFieldWriter<SIE4Item.Kptyp>() {
                @Override
                String writeFields(SIE4Item.Kptyp item) {
                    return quoted(item.type());
                }
            }),
            Map.entry(SIE4Item.Ktyp.class, new AbstractFieldWriter<SIE4Item.Ktyp>() {
                @Override
                String writeFields(SIE4Item.Ktyp item) {
                    return "%d %s".formatted(item.accountNo(), item.type().name());
                }
            }),
            Map.entry(SIE4Item.Objekt.class, new AbstractFieldWriter<SIE4Item.Objekt>() {
                @Override
                String writeFields(SIE4Item.Objekt item) {
                    return "%d %s %s".formatted(item.dimensionNo(), quoted(item.objectNo()), quoted(item.objectName()));
                }
            }),
            Map.entry(SIE4Item.Oib.class, new AbstractFieldWriter<SIE4Item.Oib>() {
                @Override
                String writeFields(SIE4Item.Oib item) {
                    return "%d %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.objectReference(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Omfattn.class, new AbstractFieldWriter<SIE4Item.Omfattn>() {
                @Override
                String writeFields(SIE4Item.Omfattn item) {
                    return SIE4_DATE_FORMATTER.format(item.date());
                }
            }),
            Map.entry(SIE4Item.OrgNr.class, new AbstractFieldWriter<SIE4Item.OrgNr>() {
                @Override
                String writeFields(SIE4Item.OrgNr item) {
                    return "%s %s %s".formatted(
                            quoted(item.orgNr()),
                            item.acqNo().map(Object::toString).orElse(""),
                            item.actNo().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Oub.class, new AbstractFieldWriter<SIE4Item.Oub>() {
                @Override
                String writeFields(SIE4Item.Oub item) {
                    return "%d %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.objectReference(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Pbudget.class, new AbstractFieldWriter<SIE4Item.Pbudget>() {
                @Override
                String writeFields(SIE4Item.Pbudget item) {
                    return "%d %s %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.period(),
                            item.accountNo(),
                            item.objectReference().map(Object::toString).orElse("{}"),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Program.class, new AbstractFieldWriter<SIE4Item.Program>() {
                @Override
                String writeFields(SIE4Item.Program item) {
                    return "%s %s".formatted(
                            quoted(item.programName()),
                            quoted(item.version())
                    );
                }
            }),
            Map.entry(SIE4Item.Prosa.class, new AbstractFieldWriter<SIE4Item.Prosa>() {
                @Override
                String writeFields(SIE4Item.Prosa item) {
                    return quoted(item.comment());
                }
            }),
            Map.entry(SIE4Item.Psaldo.class, new AbstractFieldWriter<SIE4Item.Psaldo>() {
                @Override
                String writeFields(SIE4Item.Psaldo item) {
                    return "%d %s %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.period(),
                            item.accountNo(),
                            item.objectReference().map(Object::toString).orElse("{}"),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Rar.class, new AbstractFieldWriter<SIE4Item.Rar>() {
                @Override
                String writeFields(SIE4Item.Rar item) {
                    return "%d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            SIE4_DATE_FORMATTER.format(item.start()),
                            SIE4_DATE_FORMATTER.format(item.end())
                    );
                }
            }),
            Map.entry(SIE4Item.Res.class, new AbstractFieldWriter<SIE4Item.Res>() {
                @Override
                String writeFields(SIE4Item.Res item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Sietyp.class, new AbstractFieldWriter<SIE4Item.Sietyp>() {
                @Override
                String writeFields(SIE4Item.Sietyp item) {
                    return "%d".formatted(item.typeNo());
                }
            }),
            Map.entry(SIE4Item.Sru.class, new AbstractFieldWriter<SIE4Item.Sru>() {
                @Override
                String writeFields(SIE4Item.Sru item) {
                    return "%d %d".formatted(item.accountNo(), item.sruCode());
                }
            }),
            Map.entry(SIE4Item.Taxar.class, new AbstractFieldWriter<SIE4Item.Taxar>() {
                @Override
                String writeFields(SIE4Item.Taxar item) {
                    return "%d".formatted(item.year());
                }
            }),
            Map.entry(SIE4Item.Ub.class, new AbstractFieldWriter<SIE4Item.Ub>() {
                @Override
                String writeFields(SIE4Item.Ub item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.account(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(SIE4Item.Underdim.class, new AbstractFieldWriter<SIE4Item.Underdim>() {
                @Override
                String writeFields(SIE4Item.Underdim item) {
                    return "%d %s %d".formatted(
                            item.dimensionNo(),
                            quoted(item.name()),
                            item.superDimensionNo()
                    );
                }
            }),
            Map.entry(SIE4Item.Valuta.class, new AbstractFieldWriter<SIE4Item.Valuta>() {
                @Override
                String writeFields(SIE4Item.Valuta item) {
                    return quoted(item.currencyCode());
                }
            }),
            Map.entry(SIE4Item.Ver.class, new AbstractFieldWriter<SIE4Item.Ver>() {
                @Override
                String writeFields(SIE4Item.Ver item) {
                    StringBuilder sb = new StringBuilder();
                    item.series().ifPresent(s -> sb.append(quoted(s)));
                    item.verificationNo().ifPresent(vn -> sb.append(' ').append(quoted(vn)));
                    sb.append(' ').append(SIE4_DATE_FORMATTER.format(item.date()));
                    item.text().ifPresent(t -> sb.append(' ').append(quoted(t)));
                    item.regDate().ifPresent(rd -> sb.append(' ').append(SIE4_DATE_FORMATTER.format(rd)));
                    item.sign().ifPresent(s -> sb.append(' ').append(quoted(s)));
                    sb.append("\n{");
                    item.transactions().forEach(tx -> sb.append('\n').append(writeFields(tx)));
                    sb.append("\n}");
                    return sb.toString();
                }

                private String writeFields(SIE4Item.Transaction tx) {
                    String fields = WRITER_REGISTRY.get(tx.getClass()).writeItem(tx);
                    return "   ".concat(fields);
                }
            }),
            Map.entry(SIE4Item.Transaction.Trans.class, new AbstractFieldWriter<SIE4Item.Transaction.Trans>() {
                @Override
                String writeFields(SIE4Item.Transaction.Trans item) {
                    return writeTransactionFields(item);
                }
            }),
            Map.entry(SIE4Item.Transaction.Btrans.class, new AbstractFieldWriter<SIE4Item.Transaction.Btrans>() {
                @Override
                String writeFields(SIE4Item.Transaction.Btrans item) {
                    return writeTransactionFields(item);
                }
            }),
            Map.entry(SIE4Item.Transaction.Rtrans.class, new AbstractFieldWriter<SIE4Item.Transaction.Rtrans>() {
                @Override
                String writeFields(SIE4Item.Transaction.Rtrans item) {
                    return writeTransactionFields(item);
                }
            })
    );

    private static String writeTransactionFields(SIE4Item.Transaction tx) {
        String objectRefs = "{" + tx.objectReferences().stream()
                .map(r -> "%d %s".formatted(r.dimensionNo(), r.objectNo()))
                .collect(Collectors.joining(" ")) + "}";
        return "%d %s %s %s %s %s %s".formatted(
                tx.accountNo(),
                objectRefs,
                tx.amount(),
                tx.transactionDate().map(SIE4_DATE_FORMATTER::format).orElse(""),
                tx.text().map(AbstractFieldWriter::quoted).orElseGet(() -> {
                    if (tx.quantity().isPresent() || tx.sign().isPresent()) {
                        return "\"\"";
                    }
                    return "";
                }),
                tx.quantity().map(Object::toString).orElse(""),
                tx.sign().map(AbstractFieldWriter::quoted).orElse("")
        ).stripTrailing();
    }

    public static String toFileString(SIE4Item item) {
        return WRITER_REGISTRY.get(item.getClass()).writeItem(item);
    }
}
