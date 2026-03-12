package se.bufferoverflow.sieport.sie4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Class representing a SIE4 document which is used for transferring
 * accounting data. This class encapsulates various elements of the
 * document such as accounts, transactions, and other relevant metadata.
 * <p>
 * Use {@link #newDocument()} to start building a new document with standard defaults applied
 * ({@code FLAGGA 0}, {@code FORMAT PC8}, {@code GEN} set to today, {@code SIETYP 4}).
 * Use {@link #builder()} for a clean-slate builder with no defaults.
 */
public class SIE4Document {

    private final SIE4Item.Flagga flagga;
    private final SIE4Item.Program program;
    private final SIE4Item.Format format;
    private final SIE4Item.Gen gen;
    private final SIE4Item.Sietyp sietyp;
    private final SIE4Item.Prosa prosa;
    private final SIE4Item.Ftyp ftyp;
    private final SIE4Item.Fnr fnr;
    private final SIE4Item.OrgNr orgnr;
    private final SIE4Item.Bkod bkod;
    private final SIE4Item.Adress adress;
    private final SIE4Item.Fnamn fnamn;
    private final List<SIE4Item.Rar> rar;
    private final SIE4Item.Taxar taxar;
    private final SIE4Item.Omfattn omfattn;
    private final SIE4Item.Kptyp kptyp;
    private final SIE4Item.Valuta valuta;
    private final List<SIE4Item.Konto> konto;
    private final List<SIE4Item.Ktyp> ktyp;
    private final List<SIE4Item.Enhet> enhet;
    private final List<SIE4Item.Sru> sru;
    private final List<SIE4Item.Dim> dim;
    private final List<SIE4Item.Underdim> underdim;
    private final List<SIE4Item.Objekt> objekt;
    private final List<SIE4Item.Ib> ib;
    private final List<SIE4Item.Ub> ub;
    private final List<SIE4Item.Oib> oib;
    private final List<SIE4Item.Oub> oub;
    private final List<SIE4Item.Res> res;
    private final List<SIE4Item.Psaldo> psaldo;
    private final List<SIE4Item.Pbudget> pbudget;
    private final List<SIE4Item.Ver> ver;

    /**
     * Constructs a {@code SIE4Document} from a flat list of parsed items. No defaults are applied;
     * the document reflects the source items exactly. For internal use by the parser.
     */
    static SIE4Document from(List<SIE4Item> items) {
        return new SIE4Document(
                findItem(items, SIE4Item.Flagga.class).orElse(null),
                findItem(items, SIE4Item.Program.class).orElse(null),
                findItem(items, SIE4Item.Format.class).orElse(null),
                findItem(items, SIE4Item.Gen.class).orElse(null),
                findItem(items, SIE4Item.Sietyp.class).orElse(null),
                findItem(items, SIE4Item.Prosa.class).orElse(null),
                findItem(items, SIE4Item.Ftyp.class).orElse(null),
                findItem(items, SIE4Item.Fnr.class).orElse(null),
                findItem(items, SIE4Item.OrgNr.class).orElse(null),
                findItem(items, SIE4Item.Bkod.class).orElse(null),
                findItem(items, SIE4Item.Adress.class).orElse(null),
                findItem(items, SIE4Item.Fnamn.class).orElse(null),
                findItems(items, SIE4Item.Rar.class),
                findItem(items, SIE4Item.Taxar.class).orElse(null),
                findItem(items, SIE4Item.Omfattn.class).orElse(null),
                findItem(items, SIE4Item.Kptyp.class).orElse(null),
                findItem(items, SIE4Item.Valuta.class).orElse(null),
                findItems(items, SIE4Item.Konto.class),
                findItems(items, SIE4Item.Ktyp.class),
                findItems(items, SIE4Item.Enhet.class),
                findItems(items, SIE4Item.Sru.class),
                findItems(items, SIE4Item.Dim.class),
                findItems(items, SIE4Item.Underdim.class),
                findItems(items, SIE4Item.Objekt.class),
                findItems(items, SIE4Item.Ib.class),
                findItems(items, SIE4Item.Ub.class),
                findItems(items, SIE4Item.Oib.class),
                findItems(items, SIE4Item.Oub.class),
                findItems(items, SIE4Item.Res.class),
                findItems(items, SIE4Item.Psaldo.class),
                findItems(items, SIE4Item.Pbudget.class),
                findItems(items, SIE4Item.Ver.class)
        );
    }

    /**
     * Retrieves all items of the specified type.
     *
     * @param clazz the type of SIE4Item to be retrieved
     * @return items requested, or empty list if none found
     */
    public <T extends SIE4Item> List<T> getItems(Class<T> clazz) {
        return findItems(getItems(), clazz);
    }

    /**
     * Retrieves an item of the specified type.
     * Will throw if more than one item of the specified type exists.
     *
     * @param clazz the type of SIE4Item to be retrieved
     * @return the item requested, or {@link Optional#empty()} if not found
     * @throws SIE4Exception if there is more than one item of the specified class type
     */
    public <T extends SIE4Item> Optional<T> getItem(Class<T> clazz) {
        return findItem(getItems(), clazz);
    }

    /**
     * Retrieves the basic values from the items composing the "identification part" of the SIE4 document.
     */
    public IdentificationItems getIdentificationItems() {
        return new IdentificationItems(
            mapOrNull(getFlagga(), SIE4Item.Flagga::flag),
            mapOrNull(getProgram(), p -> "%s, %s".formatted(p.programName(), p.version())),
            mapOrNull(getGen(), SIE4Item.Gen::date),
            mapOrNull(getSietyp(), SIE4Item.Sietyp::typeNo),
            mapOrNull(getProsa(), SIE4Item.Prosa::comment),
            mapOrNull(getKptyp(), SIE4Item.Kptyp::type),
            mapOrNull(getValuta(), SIE4Item.Valuta::currencyCode),
            mapOrNull(getTaxar(), SIE4Item.Taxar::year),
            getRar().stream().map(r -> new FinancialYear(r.start(), r.end())).toList(),
            mapOrNull(getAdress(), SIE4Item.Adress::contact),
            mapOrNull(getAdress(), addr -> "%s %s".formatted(addr.distributionAddress(), addr.postalAddress())),
            mapOrNull(getAdress(), SIE4Item.Adress::tel),
            mapOrNull(getBkod(), SIE4Item.Bkod::sniCode),
            mapOrNull(getFtyp(), SIE4Item.Ftyp::companyType),
            mapOrNull(getFnamn(), SIE4Item.Fnamn::companyName),
            mapOrNull(getOrgnr(), SIE4Item.OrgNr::orgNr)
        );
    }

    /**
     * Get all items in this document in a list.
     *
     * @return An immutable list with all items
     */
    public List<SIE4Item> getItems() {
        List<SIE4Item> result = new ArrayList<>();
        result.add(flagga);
        result.add(program);
        result.add(format);
        result.add(gen);
        result.add(sietyp);
        result.add(prosa);
        result.add(ftyp);
        result.add(fnr);
        result.add(orgnr);
        result.add(bkod);
        result.add(adress);
        result.add(fnamn);
        result.addAll(rar);
        result.add(taxar);
        result.add(omfattn);
        result.add(kptyp);
        result.add(valuta);
        result.addAll(konto);
        result.addAll(ktyp);
        result.addAll(enhet);
        result.addAll(sru);
        result.addAll(dim);
        result.addAll(underdim);
        result.addAll(objekt);
        result.addAll(ib);
        result.addAll(ub);
        result.addAll(oib);
        result.addAll(oub);
        result.addAll(res);
        result.addAll(psaldo);
        result.addAll(pbudget);
        result.addAll(ver);
        return result.stream().filter(Objects::nonNull).toList();
    }

    private <I, O> O mapOrNull(I item, Function<I, O> mapper) {
        if (item == null) {
            return null;
        }
        return mapper.apply(item);
    }

    private static <T extends SIE4Item> Optional<T> findItem(List<SIE4Item> items, Class<T> clazz) {
        List<T> found = items.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
        if (found.size() > 1) {
            throw new SIE4Exception("Expected at most one #" + clazz.getSimpleName().toUpperCase()
                    + " item, found " + found.size());
        }
        return found.stream().findFirst();
    }

    private static <T extends SIE4Item> List<T> findItems(List<SIE4Item> items, Class<T> clazz) {
        return items.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public SIE4Item.Flagga getFlagga() {
        return flagga;
    }

    public SIE4Item.Program getProgram() {
        return program;
    }

    public SIE4Item.Format getFormat() {
        return format;
    }

    public SIE4Item.Gen getGen() {
        return gen;
    }

    public SIE4Item.Sietyp getSietyp() {
        return sietyp;
    }

    public SIE4Item.Prosa getProsa() {
        return prosa;
    }

    public SIE4Item.Ftyp getFtyp() {
        return ftyp;
    }

    public SIE4Item.Fnr getFnr() {
        return fnr;
    }

    public SIE4Item.OrgNr getOrgnr() {
        return orgnr;
    }

    public SIE4Item.Bkod getBkod() {
        return bkod;
    }

    public SIE4Item.Adress getAdress() {
        return adress;
    }

    public SIE4Item.Fnamn getFnamn() {
        return fnamn;
    }

    public List<SIE4Item.Rar> getRar() {
        return rar;
    }

    public SIE4Item.Taxar getTaxar() {
        return taxar;
    }

    public SIE4Item.Omfattn getOmfattn() {
        return omfattn;
    }

    public SIE4Item.Kptyp getKptyp() {
        return kptyp;
    }

    public SIE4Item.Valuta getValuta() {
        return valuta;
    }

    public List<SIE4Item.Konto> getKonto() {
        return konto;
    }

    public List<SIE4Item.Ktyp> getKtyp() {
        return ktyp;
    }

    public List<SIE4Item.Enhet> getEnhet() {
        return enhet;
    }

    public List<SIE4Item.Sru> getSru() {
        return sru;
    }

    public List<SIE4Item.Dim> getDim() {
        return dim;
    }

    public List<SIE4Item.Underdim> getUnderdim() {
        return underdim;
    }

    public List<SIE4Item.Objekt> getObjekt() {
        return objekt;
    }

    public List<SIE4Item.Ib> getIb() {
        return ib;
    }

    public List<SIE4Item.Ub> getUb() {
        return ub;
    }

    public List<SIE4Item.Oib> getOib() {
        return oib;
    }

    public List<SIE4Item.Oub> getOub() {
        return oub;
    }

    public List<SIE4Item.Res> getRes() {
        return res;
    }

    public List<SIE4Item.Psaldo> getPsaldo() {
        return psaldo;
    }

    public List<SIE4Item.Pbudget> getPbudget() {
        return pbudget;
    }

    public List<SIE4Item.Ver> getVer() {
        return ver;
    }

    @SuppressWarnings("java:S107")
    private SIE4Document(SIE4Item.Flagga flagga,
                         SIE4Item.Program program,
                         SIE4Item.Format format,
                         SIE4Item.Gen gen,
                         SIE4Item.Sietyp sietyp,
                         SIE4Item.Prosa prosa,
                         SIE4Item.Ftyp ftyp,
                         SIE4Item.Fnr fnr,
                         SIE4Item.OrgNr orgnr,
                         SIE4Item.Bkod bkod,
                         SIE4Item.Adress adress,
                         SIE4Item.Fnamn fnamn,
                         List<SIE4Item.Rar> rar,
                         SIE4Item.Taxar taxar,
                         SIE4Item.Omfattn omfattn,
                         SIE4Item.Kptyp kptyp,
                         SIE4Item.Valuta valuta,
                         List<SIE4Item.Konto> konto,
                         List<SIE4Item.Ktyp> ktyp,
                         List<SIE4Item.Enhet> enhet,
                         List<SIE4Item.Sru> sru,
                         List<SIE4Item.Dim> dim,
                         List<SIE4Item.Underdim> underdim,
                         List<SIE4Item.Objekt> objekt,
                         List<SIE4Item.Ib> ib,
                         List<SIE4Item.Ub> ub,
                         List<SIE4Item.Oib> oib,
                         List<SIE4Item.Oub> oub,
                         List<SIE4Item.Res> res,
                         List<SIE4Item.Psaldo> psaldo,
                         List<SIE4Item.Pbudget> pbudget,
                         List<SIE4Item.Ver> ver) {
        this.flagga = flagga;
        this.program = program;
        this.format = format;
        this.gen = gen;
        this.sietyp = sietyp;
        this.prosa = prosa;
        this.ftyp = ftyp;
        this.fnr = fnr;
        this.orgnr = orgnr;
        this.bkod = bkod;
        this.adress = adress;
        this.fnamn = fnamn;
        this.rar = rar == null ? List.of() : List.copyOf(rar);
        this.taxar = taxar;
        this.omfattn = omfattn;
        this.kptyp = kptyp;
        this.valuta = valuta;
        this.konto = konto == null ? List.of() : List.copyOf(konto);
        this.ktyp = ktyp == null ? List.of() : List.copyOf(ktyp);
        this.enhet = enhet == null ? List.of() : List.copyOf(enhet);
        this.sru = sru == null ? List.of() : List.copyOf(sru);
        this.dim = dim == null ? List.of() : List.copyOf(dim);
        this.underdim = underdim == null ? List.of() : List.copyOf(underdim);
        this.objekt = objekt == null ? List.of() : List.copyOf(objekt);
        this.ib = ib == null ? List.of() : List.copyOf(ib);
        this.ub = ub == null ? List.of() : List.copyOf(ub);
        this.oib = oib == null ? List.of() : List.copyOf(oib);
        this.oub = oub == null ? List.of() : List.copyOf(oub);
        this.res = res == null ? List.of() : List.copyOf(res);
        this.psaldo = psaldo == null ? List.of() : List.copyOf(psaldo);
        this.pbudget = pbudget == null ? List.of() : List.copyOf(pbudget);
        this.ver = ver == null ? List.of() : List.copyOf(ver);
    }

    /**
     * Returns a new {@link Builder} pre-populated with standard defaults for a SIE4 export document:
     * {@code FLAGGA 0}, {@code FORMAT PC8}, {@code GEN} set to today, {@code SIETYP 4}.
     * This is the recommended starting point for creating new documents.
     */
    public static Builder newDocument() {
        return builder()
                .flagga(SIE4Item.Flagga.UNSET)
                .format(SIE4Item.Format.pc8())
                .gen(SIE4Item.Gen.now())
                .sietyp(SIE4Item.Sietyp.SIE_4);
    }

    /**
     * Returns an empty {@link Builder} with no defaults set.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns a {@link Builder} pre-populated with all fields from this document.
     * Useful for creating a modified copy of an existing document.
     */
    public Builder toBuilder() {
        return builder()
                .flagga(flagga).program(program).format(format).gen(gen).sietyp(sietyp)
                .prosa(prosa).ftyp(ftyp).fnr(fnr).orgnr(orgnr).bkod(bkod)
                .adress(adress).fnamn(fnamn).rar(rar).taxar(taxar).omfattn(omfattn)
                .kptyp(kptyp).valuta(valuta).konto(konto).ktyp(ktyp).enhet(enhet)
                .sru(sru).dim(dim).underdim(underdim).objekt(objekt).ib(ib).ub(ub)
                .oib(oib).oub(oub).res(res).psaldo(psaldo).pbudget(pbudget).ver(ver);
    }

    public static class Builder {
        private SIE4Item.Flagga flagga;
        private SIE4Item.Program program;
        private SIE4Item.Format format;
        private SIE4Item.Gen gen;
        private SIE4Item.Sietyp sietyp;
        private SIE4Item.Prosa prosa;
        private SIE4Item.Ftyp ftyp;
        private SIE4Item.Fnr fnr;
        private SIE4Item.OrgNr orgnr;
        private SIE4Item.Bkod bkod;
        private SIE4Item.Adress adress;
        private SIE4Item.Fnamn fnamn;
        private List<SIE4Item.Rar> rar;
        private SIE4Item.Taxar taxar;
        private SIE4Item.Omfattn omfattn;
        private SIE4Item.Kptyp kptyp;
        private SIE4Item.Valuta valuta;
        private List<SIE4Item.Konto> konto;
        private List<SIE4Item.Ktyp> ktyp;
        private List<SIE4Item.Enhet> enhet;
        private List<SIE4Item.Sru> sru;
        private List<SIE4Item.Dim> dim;
        private List<SIE4Item.Underdim> underdim;
        private List<SIE4Item.Objekt> objekt;
        private List<SIE4Item.Ib> ib;
        private List<SIE4Item.Ub> ub;
        private List<SIE4Item.Oib> oib;
        private List<SIE4Item.Oub> oub;
        private List<SIE4Item.Res> res;
        private List<SIE4Item.Psaldo> psaldo;
        private List<SIE4Item.Pbudget> pbudget;
        private List<SIE4Item.Ver> ver;

        public Builder flagga(SIE4Item.Flagga flagga) {
            this.flagga = flagga;
            return this;
        }

        public Builder program(SIE4Item.Program program) {
            this.program = program;
            return this;
        }

        public Builder format(SIE4Item.Format format) {
            this.format = format;
            return this;
        }

        public Builder gen(SIE4Item.Gen gen) {
            this.gen = gen;
            return this;
        }

        public Builder sietyp(SIE4Item.Sietyp sietyp) {
            this.sietyp = sietyp;
            return this;
        }

        public Builder prosa(SIE4Item.Prosa prosa) {
            this.prosa = prosa;
            return this;
        }

        public Builder ftyp(SIE4Item.Ftyp ftyp) {
            this.ftyp = ftyp;
            return this;
        }

        public Builder fnr(SIE4Item.Fnr fnr) {
            this.fnr = fnr;
            return this;
        }

        public Builder orgnr(SIE4Item.OrgNr orgnr) {
            this.orgnr = orgnr;
            return this;
        }

        public Builder orgnr(String orgNr) {
            this.orgnr = SIE4Item.OrgNr.of(orgNr);
            return this;
        }

        public Builder bkod(SIE4Item.Bkod bkod) {
            this.bkod = bkod;
            return this;
        }

        public Builder adress(SIE4Item.Adress adress) {
            this.adress = adress;
            return this;
        }

        public Builder fnamn(SIE4Item.Fnamn fnamn) {
            this.fnamn = fnamn;
            return this;
        }

        public Builder fnamn(String companyName) {
            this.fnamn = new SIE4Item.Fnamn(companyName);
            return this;
        }

        public Builder rar(List<SIE4Item.Rar> rar) {
            this.rar = rar;
            return this;
        }

        public Builder taxar(SIE4Item.Taxar taxar) {
            this.taxar = taxar;
            return this;
        }

        public Builder omfattn(SIE4Item.Omfattn omfattn) {
            this.omfattn = omfattn;
            return this;
        }

        public Builder kptyp(SIE4Item.Kptyp kptyp) {
            this.kptyp = kptyp;
            return this;
        }

        public Builder valuta(SIE4Item.Valuta valuta) {
            this.valuta = valuta;
            return this;
        }

        public Builder konto(List<SIE4Item.Konto> konto) {
            this.konto = konto;
            return this;
        }

        public Builder ktyp(List<SIE4Item.Ktyp> ktyp) {
            this.ktyp = ktyp;
            return this;
        }

        public Builder enhet(List<SIE4Item.Enhet> enhet) {
            this.enhet = enhet;
            return this;
        }

        public Builder sru(List<SIE4Item.Sru> sru) {
            this.sru = sru;
            return this;
        }

        public Builder dim(List<SIE4Item.Dim> dim) {
            this.dim = dim;
            return this;
        }

        public Builder underdim(List<SIE4Item.Underdim> underdim) {
            this.underdim = underdim;
            return this;
        }

        public Builder objekt(List<SIE4Item.Objekt> objekt) {
            this.objekt = objekt;
            return this;
        }

        public Builder ib(List<SIE4Item.Ib> ib) {
            this.ib = ib;
            return this;
        }

        public Builder ub(List<SIE4Item.Ub> ub) {
            this.ub = ub;
            return this;
        }

        public Builder oib(List<SIE4Item.Oib> oib) {
            this.oib = oib;
            return this;
        }

        public Builder oub(List<SIE4Item.Oub> oub) {
            this.oub = oub;
            return this;
        }

        public Builder res(List<SIE4Item.Res> res) {
            this.res = res;
            return this;
        }

        public Builder psaldo(List<SIE4Item.Psaldo> psaldo) {
            this.psaldo = psaldo;
            return this;
        }

        public Builder pbudget(List<SIE4Item.Pbudget> pbudget) {
            this.pbudget = pbudget;
            return this;
        }

        public Builder ver(List<SIE4Item.Ver> ver) {
            this.ver = ver;
            return this;
        }

        public SIE4Document build() {
            return new SIE4Document(
                flagga,
                program,
                format,
                gen,
                sietyp,
                prosa,
                ftyp,
                fnr,
                orgnr,
                bkod,
                adress,
                fnamn,
                rar,
                taxar,
                omfattn,
                kptyp,
                valuta,
                konto,
                ktyp,
                enhet,
                sru,
                dim,
                underdim,
                objekt,
                ib,
                ub,
                oib,
                oub,
                res,
                psaldo,
                pbudget,
                ver);
        }
    }
}
