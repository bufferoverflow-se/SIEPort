package se.bufferoverflow.sieport.sie4.writer;

import se.bufferoverflow.sieport.sie4.FileItem;
import se.bufferoverflow.sieport.sie4.Label;

import java.util.Map;
import java.util.stream.Collectors;

import static se.bufferoverflow.sieport.sie4.Constants.SIE4_DATE_FORMATTER;

public class OutFieldMapper {
    private OutFieldMapper() {}

    private static final Map<Label, AbstractFieldWriter<?>> WRITER_REGISTRY = Map.ofEntries(
            Map.entry(Label.ADRESS, new AbstractFieldWriter<FileItem.Adress>() {
                @Override
                String writeFields(FileItem.Adress item) {
                    return "%s %s %s %s".formatted(quoted(item.contact()), quoted(item.distributionAddress()),
                            quoted(item.postalAddress()), quoted(item.tel()));
                }
            }),
            Map.entry(Label.BKOD, new AbstractFieldWriter<FileItem.Bkod>() {
                @Override
                String writeFields(FileItem.Bkod item) {
                    return "%d".formatted(item.sniCode());
                }
            }),
            Map.entry(Label.DIM, new AbstractFieldWriter<FileItem.Dim>() {
                @Override
                String writeFields(FileItem.Dim item) {
                    return "%d %s".formatted(item.dimensionNo(), quoted(item.name()));
                }
            }),
            Map.entry(Label.ENHET, new AbstractFieldWriter<FileItem.Enhet>() {
                @Override
                String writeFields(FileItem.Enhet item) {
                    return "%d %s".formatted(item.accountNo(), quoted(item.unit()));
                }
            }),
            Map.entry(Label.FLAGGA, new AbstractFieldWriter<FileItem.Flagga>() {
                @Override
                String writeFields(FileItem.Flagga item) {
                    return "%d".formatted(item.flag());
                }
            }),
            Map.entry(Label.FNAMN, new AbstractFieldWriter<FileItem.Fnamn>() {
                @Override
                String writeFields(FileItem.Fnamn item) {
                    return quoted(item.companyName());
                }
            }),
            Map.entry(Label.FNR, new AbstractFieldWriter<FileItem.Fnr>() {
                @Override
                String writeFields(FileItem.Fnr item) {
                    return quoted(item.companyId());
                }
            }),
            Map.entry(Label.FORMAT, new AbstractFieldWriter<FileItem.Format>() {
                @Override
                String writeFields(FileItem.Format item) {
                    return quoted(item.format());
                }
            }),
            Map.entry(Label.FTYP, new AbstractFieldWriter<FileItem.Ftyp>() {
                @Override
                String writeFields(FileItem.Ftyp item) {
                    return item.companyType().name();
                }
            }),
            Map.entry(Label.GEN, new AbstractFieldWriter<FileItem.Gen>() {
                @Override
                String writeFields(FileItem.Gen item) {
                    return "%s %s".formatted(
                            SIE4_DATE_FORMATTER.format(item.date()),
                            item.signature().map(AbstractFieldWriter::quoted).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.IB, new AbstractFieldWriter<FileItem.Ib>() {
                @Override
                String writeFields(FileItem.Ib item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.KONTO, new AbstractFieldWriter<FileItem.Konto>() {
                @Override
                String writeFields(FileItem.Konto item) {
                    return "%d %s".formatted(item.accountNo(), quoted(item.accountName()));
                }
            }),
            Map.entry(Label.KPTYP, new AbstractFieldWriter<FileItem.Kptyp>() {
                @Override
                String writeFields(FileItem.Kptyp item) {
                    return quoted(item.type());
                }
            }),
            Map.entry(Label.KTYP, new AbstractFieldWriter<FileItem.Ktyp>() {
                @Override
                String writeFields(FileItem.Ktyp item) {
                    return "%d %s".formatted(item.accountNo(), item.type().name());
                }
            }),
            Map.entry(Label.OBJEKT, new AbstractFieldWriter<FileItem.Objekt>() {
                @Override
                String writeFields(FileItem.Objekt item) {
                    return "%d %s %s".formatted(item.dimensionNo(), quoted(item.objectNo()), quoted(item.objectName()));
                }
            }),
            Map.entry(Label.OIB, new AbstractFieldWriter<FileItem.Oib>() {
                @Override
                String writeFields(FileItem.Oib item) {
                    return "%d %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.objectReference(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.OMFATTN, new AbstractFieldWriter<FileItem.Omfattn>() {
                @Override
                String writeFields(FileItem.Omfattn item) {
                    return SIE4_DATE_FORMATTER.format(item.date());
                }
            }),
            Map.entry(Label.ORGNR, new AbstractFieldWriter<FileItem.OrgNr>() {
                @Override
                String writeFields(FileItem.OrgNr item) {
                    return "%s %s %s".formatted(
                            quoted(item.orgNr()),
                            item.acqNo().map(Object::toString).orElse(""),
                            item.actNo().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.OUB, new AbstractFieldWriter<FileItem.Oub>() {
                @Override
                String writeFields(FileItem.Oub item) {
                    return "%d %d %s %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.objectReference(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.PBUDGET, new AbstractFieldWriter<FileItem.Pbudget>() {
                @Override
                String writeFields(FileItem.Pbudget item) {
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
            Map.entry(Label.PROGRAM, new AbstractFieldWriter<FileItem.Program>() {
                @Override
                String writeFields(FileItem.Program item) {
                    return "%s %s".formatted(
                            quoted(item.programName()),
                            quoted(item.version())
                    );
                }
            }),
            Map.entry(Label.PROSA, new AbstractFieldWriter<FileItem.Prosa>() {
                @Override
                String writeFields(FileItem.Prosa item) {
                    return quoted(item.comment());
                }
            }),
            Map.entry(Label.PSALDO, new AbstractFieldWriter<FileItem.Psaldo>() {
                @Override
                String writeFields(FileItem.Psaldo item) {
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
            Map.entry(Label.RAR, new AbstractFieldWriter<FileItem.Rar>() {
                @Override
                String writeFields(FileItem.Rar item) {
                    return "%d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            SIE4_DATE_FORMATTER.format(item.start()),
                            SIE4_DATE_FORMATTER.format(item.end())
                    );
                }
            }),
            Map.entry(Label.RES, new AbstractFieldWriter<FileItem.Res>() {
                @Override
                String writeFields(FileItem.Res item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.accountNo(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.SIETYP, new AbstractFieldWriter<FileItem.Sietyp>() {
                @Override
                String writeFields(FileItem.Sietyp item) {
                    return "%d".formatted(item.typeNo());
                }
            }),
            Map.entry(Label.SRU, new AbstractFieldWriter<FileItem.Sru>() {
                @Override
                String writeFields(FileItem.Sru item) {
                    return "%d %d".formatted(item.accountNo(), item.sruCode());
                }
            }),
            Map.entry(Label.TAXAR, new AbstractFieldWriter<FileItem.Taxar>() {
                @Override
                String writeFields(FileItem.Taxar item) {
                    return "%d".formatted(item.year());
                }
            }),
            Map.entry(Label.UB, new AbstractFieldWriter<FileItem.Ub>() {
                @Override
                String writeFields(FileItem.Ub item) {
                    return "%d %d %s %s".formatted(
                            item.yearNumber().yearNo(),
                            item.account(),
                            item.balance(),
                            item.quantity().map(Object::toString).orElse("")
                    ).trim();
                }
            }),
            Map.entry(Label.UNDERDIM, new AbstractFieldWriter<FileItem.Underdim>() {
                @Override
                String writeFields(FileItem.Underdim item) {
                    return "%d %s %d".formatted(
                            item.dimensionNo(),
                            quoted(item.name()),
                            item.superDimensionNo()
                    );
                }
            }),
            Map.entry(Label.VALUTA, new AbstractFieldWriter<FileItem.Valuta>() {
                @Override
                String writeFields(FileItem.Valuta item) {
                    return quoted(item.currencyCode());
                }
            }),
            Map.entry(Label.VER, new AbstractFieldWriter<FileItem.Ver>() {
                @Override
                String writeFields(FileItem.Ver item) {
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

                private String writeFields(FileItem.Transaction tx) {
                    String fields = switch (tx) {
                        case FileItem.Transaction.Trans ignored -> WRITER_REGISTRY.get(Label.TRANS).writeItem(Label.TRANS, tx);
                        case FileItem.Transaction.Btrans ignored -> WRITER_REGISTRY.get(Label.BTRANS).writeItem(Label.BTRANS, tx);
                        case FileItem.Transaction.Rtrans ignored -> WRITER_REGISTRY.get(Label.RTRANS).writeItem(Label.RTRANS, tx);
                    };
                    return "   ".concat(fields);
                }
            }),
            Map.entry(Label.TRANS, new AbstractFieldWriter<FileItem.Transaction.Trans>() {
                @Override
                String writeFields(FileItem.Transaction.Trans field) {
                    return writeTransactionFields(field);
                }
            }),
            Map.entry(Label.BTRANS, new AbstractFieldWriter<FileItem.Transaction.Btrans>() {
                @Override
                String writeFields(FileItem.Transaction.Btrans field) {
                    return writeTransactionFields(field);
                }
            }),
            Map.entry(Label.RTRANS, new AbstractFieldWriter<FileItem.Transaction.Rtrans>() {
                @Override
                String writeFields(FileItem.Transaction.Rtrans field) {
                    return writeTransactionFields(field);
                }
            })
    );

    private static String writeTransactionFields(FileItem.Transaction tx) {
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

    public static String toFileString(FileItem field) {
        Label label = Label.valueOf(field.getClass().getSimpleName().toUpperCase());
        return WRITER_REGISTRY.get(label).writeItem(label, field);
    }

}
