package se.bufferoverflow.sieport.sie4;

/**
 * All item types in SIE4. The order of the enum elements reflects their order in the SIE4 file.
 */
public enum SIE4ItemType {
    FLAGGA(SIE4Item.Flagga.class),
    PROGRAM(SIE4Item.Program.class),
    FORMAT(SIE4Item.Format.class),
    GEN(SIE4Item.Gen.class),
    SIETYP(SIE4Item.Sietyp.class),
    PROSA(SIE4Item.Prosa.class),
    FTYP(SIE4Item.Ftyp.class),
    FNR(SIE4Item.Fnr.class),
    ORGNR(SIE4Item.OrgNr.class),
    BKOD(SIE4Item.Bkod.class),
    ADRESS(SIE4Item.Adress.class),
    FNAMN(SIE4Item.Fnamn.class),
    RAR(SIE4Item.Rar.class),
    TAXAR(SIE4Item.Taxar.class),
    OMFATTN(SIE4Item.Omfattn.class),
    KPTYP(SIE4Item.Kptyp.class),
    VALUTA(SIE4Item.Valuta.class),
    KONTO(SIE4Item.Konto.class),
    KTYP(SIE4Item.Ktyp.class),
    ENHET(SIE4Item.Enhet.class),
    SRU(SIE4Item.Sru.class),
    DIM(SIE4Item.Dim.class),
    UNDERDIM(SIE4Item.Underdim.class),
    OBJEKT(SIE4Item.Objekt.class),
    IB(SIE4Item.Ib.class),
    UB(SIE4Item.Ub.class),
    OIB(SIE4Item.Oib.class),
    OUB(SIE4Item.Oub.class),
    RES(SIE4Item.Res.class),
    PSALDO(SIE4Item.Psaldo.class),
    PBUDGET(SIE4Item.Pbudget.class),
    VER(SIE4Item.Ver.class),
    TRANS(SIE4Item.Transaction.Trans.class),
    RTRANS(SIE4Item.Transaction.Rtrans.class),
    BTRANS(SIE4Item.Transaction.Btrans.class);

    private final Class<? extends SIE4Item> implementationClass;

    SIE4ItemType(Class<? extends SIE4Item> implementationClass) {
        this.implementationClass = implementationClass;
    }

    public Class<? extends SIE4Item> getImplementationClass() {
        return implementationClass;
    }
}
