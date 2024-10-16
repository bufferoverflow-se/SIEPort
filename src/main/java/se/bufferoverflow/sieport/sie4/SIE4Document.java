package se.bufferoverflow.sieport.sie4;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class representing a SIE4 document which is used for transferring
 * accounting data. This class encapsulates various elements of the
 * document such as accounts, transactions, and other relevant metadata.
 */
public class SIE4Document {

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

    public SIE4Document(List<SIE4Item> items) {
        this(new SIE4Items(items));
    }

    public SIE4Document(SIE4Items items) {
        this(
            items.getItem(SIE4Item.Flagga.class).orElse(null),
            items.getItem(SIE4Item.Program.class).orElse(null),
            items.getItem(SIE4Item.Format.class).orElse(null),
            items.getItem(SIE4Item.Gen.class).orElse(null),
            items.getItem(SIE4Item.Sietyp.class).orElse(null),
            items.getItem(SIE4Item.Prosa.class).orElse(null),
            items.getItem(SIE4Item.Ftyp.class).orElse(null),
            items.getItem(SIE4Item.Fnr.class).orElse(null),
            items.getItem(SIE4Item.OrgNr.class).orElse(null),
            items.getItem(SIE4Item.Bkod.class).orElse(null),
            items.getItem(SIE4Item.Adress.class).orElse(null),
            items.getItem(SIE4Item.Fnamn.class).orElse(null),
            items.getItems(SIE4Item.Rar.class),
            items.getItem(SIE4Item.Taxar.class).orElse(null),
            items.getItem(SIE4Item.Omfattn.class).orElse(null),
            items.getItem(SIE4Item.Kptyp.class).orElse(null),
            items.getItem(SIE4Item.Valuta.class).orElse(null),
            items.getItems(SIE4Item.Konto.class),
            items.getItems(SIE4Item.Ktyp.class),
            items.getItems(SIE4Item.Enhet.class),
            items.getItems(SIE4Item.Sru.class),
            items.getItems(SIE4Item.Dim.class),
            items.getItems(SIE4Item.Underdim.class),
            items.getItems(SIE4Item.Objekt.class),
            items.getItems(SIE4Item.Ib.class),
            items.getItems(SIE4Item.Ub.class),
            items.getItems(SIE4Item.Oib.class),
            items.getItems(SIE4Item.Oub.class),
            items.getItems(SIE4Item.Res.class),
            items.getItems(SIE4Item.Psaldo.class),
            items.getItems(SIE4Item.Pbudget.class),
            items.getItems(SIE4Item.Ver.class)
        );
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

    public SIE4Item.Flagga getFlagga() {
        return flagga;
    }

    public void setFlagga(SIE4Item.Flagga flagga) {
        this.flagga = flagga;
    }

    public SIE4Item.Program getProgram() {
        return program;
    }

    public void setProgram(SIE4Item.Program program) {
        this.program = program;
    }

    public SIE4Item.Format getFormat() {
        return format;
    }

    public void setFormat(SIE4Item.Format format) {
        this.format = format;
    }

    public SIE4Item.Gen getGen() {
        return gen;
    }

    public void setGen(SIE4Item.Gen gen) {
        this.gen = gen;
    }

    public SIE4Item.Sietyp getSietyp() {
        return sietyp;
    }

    public void setSietyp(SIE4Item.Sietyp sietyp) {
        this.sietyp = sietyp;
    }

    public SIE4Item.Prosa getProsa() {
        return prosa;
    }

    public void setProsa(SIE4Item.Prosa prosa) {
        this.prosa = prosa;
    }

    public SIE4Item.Ftyp getFtyp() {
        return ftyp;
    }

    public void setFtyp(SIE4Item.Ftyp ftyp) {
        this.ftyp = ftyp;
    }

    public SIE4Item.Fnr getFnr() {
        return fnr;
    }

    public void setFnr(SIE4Item.Fnr fnr) {
        this.fnr = fnr;
    }

    public SIE4Item.OrgNr getOrgnr() {
        return orgnr;
    }

    public void setOrgnr(SIE4Item.OrgNr orgnr) {
        this.orgnr = orgnr;
    }

    public SIE4Item.Bkod getBkod() {
        return bkod;
    }

    public void setBkod(SIE4Item.Bkod bkod) {
        this.bkod = bkod;
    }

    public SIE4Item.Adress getAdress() {
        return adress;
    }

    public void setAdress(SIE4Item.Adress adress) {
        this.adress = adress;
    }

    public SIE4Item.Fnamn getFnamn() {
        return fnamn;
    }

    public void setFnamn(SIE4Item.Fnamn fnamn) {
        this.fnamn = fnamn;
    }

    public List<SIE4Item.Rar> getRar() {
        return rar;
    }

    public void setRar(List<SIE4Item.Rar> rar) {
        this.rar = rar;
    }

    public SIE4Item.Taxar getTaxar() {
        return taxar;
    }

    public void setTaxar(SIE4Item.Taxar taxar) {
        this.taxar = taxar;
    }

    public SIE4Item.Omfattn getOmfattn() {
        return omfattn;
    }

    public void setOmfattn(SIE4Item.Omfattn omfattn) {
        this.omfattn = omfattn;
    }

    public SIE4Item.Kptyp getKptyp() {
        return kptyp;
    }

    public void setKptyp(SIE4Item.Kptyp kptyp) {
        this.kptyp = kptyp;
    }

    public SIE4Item.Valuta getValuta() {
        return valuta;
    }

    public void setValuta(SIE4Item.Valuta valuta) {
        this.valuta = valuta;
    }

    public List<SIE4Item.Konto> getKonto() {
        return konto;
    }

    public void setKonto(List<SIE4Item.Konto> konto) {
        this.konto = konto;
    }

    public List<SIE4Item.Ktyp> getKtyp() {
        return ktyp;
    }

    public void setKtyp(List<SIE4Item.Ktyp> ktyp) {
        this.ktyp = ktyp;
    }

    public List<SIE4Item.Enhet> getEnhet() {
        return enhet;
    }

    public void setEnhet(List<SIE4Item.Enhet> enhet) {
        this.enhet = enhet;
    }

    public List<SIE4Item.Sru> getSru() {
        return sru;
    }

    public void setSru(List<SIE4Item.Sru> sru) {
        this.sru = sru;
    }

    public List<SIE4Item.Dim> getDim() {
        return dim;
    }

    public void setDim(List<SIE4Item.Dim> dim) {
        this.dim = dim;
    }

    public List<SIE4Item.Underdim> getUnderdim() {
        return underdim;
    }

    public void setUnderdim(List<SIE4Item.Underdim> underdim) {
        this.underdim = underdim;
    }

    public List<SIE4Item.Objekt> getObjekt() {
        return objekt;
    }

    public void setObjekt(List<SIE4Item.Objekt> objekt) {
        this.objekt = objekt;
    }

    public List<SIE4Item.Ib> getIb() {
        return ib;
    }

    public void setIb(List<SIE4Item.Ib> ib) {
        this.ib = ib;
    }

    public List<SIE4Item.Ub> getUb() {
        return ub;
    }

    public void setUb(List<SIE4Item.Ub> ub) {
        this.ub = ub;
    }

    public List<SIE4Item.Oib> getOib() {
        return oib;
    }

    public void setOib(List<SIE4Item.Oib> oib) {
        this.oib = oib;
    }

    public List<SIE4Item.Oub> getOub() {
        return oub;
    }

    public void setOub(List<SIE4Item.Oub> oub) {
        this.oub = oub;
    }

    public List<SIE4Item.Res> getRes() {
        return res;
    }

    public void setRes(List<SIE4Item.Res> res) {
        this.res = res;
    }

    public List<SIE4Item.Psaldo> getPsaldo() {
        return psaldo;
    }

    public void setPsaldo(List<SIE4Item.Psaldo> psaldo) {
        this.psaldo = psaldo;
    }

    public List<SIE4Item.Pbudget> getPbudget() {
        return pbudget;
    }

    public void setPbudget(List<SIE4Item.Pbudget> pbudget) {
        this.pbudget = pbudget;
    }

    public List<SIE4Item.Ver> getVer() {
        return ver;
    }

    public void setVer(List<SIE4Item.Ver> ver) {
        this.ver = ver;
    }

    @SuppressWarnings({"java:S107", "java:S3776"})
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
        this.flagga = flagga == null ? SIE4Item.Flagga.UNSET : flagga;
        this.program = program;
        this.format = format == null ? SIE4Item.Format.pc8() : format;
        this.gen = gen;
        this.sietyp = sietyp;
        this.prosa = prosa;
        this.ftyp = ftyp;
        this.fnr = fnr;
        this.orgnr = orgnr;
        this.bkod = bkod;
        this.adress = adress;
        this.fnamn = fnamn;
        this.rar = rar == null ? new ArrayList<>() : rar;
        this.taxar = taxar;
        this.omfattn = omfattn;
        this.kptyp = kptyp;
        this.valuta = valuta;
        this.konto = konto == null ? new ArrayList<>() : konto;
        this.ktyp = ktyp == null ? new ArrayList<>() : ktyp;
        this.enhet = enhet == null ? new ArrayList<>() : enhet;
        this.sru = sru == null ? new ArrayList<>() : sru;
        this.dim = dim == null ? new ArrayList<>() : dim;
        this.underdim = underdim == null ? new ArrayList<>() : underdim;
        this.objekt = objekt == null ? new ArrayList<>() : objekt;
        this.ib = ib == null ? new ArrayList<>() : ib;
        this.ub = ub == null ? new ArrayList<>() : ub;
        this.oib = oib == null ? new ArrayList<>() : oib;
        this.oub = oub == null ? new ArrayList<>() : oub;
        this.res = res == null ? new ArrayList<>() : res;
        this.psaldo = psaldo == null ? new ArrayList<>() : psaldo;
        this.pbudget = pbudget == null ? new ArrayList<>() : pbudget;
        this.ver = ver == null ? new ArrayList<>() : ver;
    }

    public static Builder builder() {
        return new Builder();
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
