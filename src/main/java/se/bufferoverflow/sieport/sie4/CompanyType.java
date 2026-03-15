package se.bufferoverflow.sieport.sie4;

/**
 * Legal form of the exported company, as used in the {@code #FTYP} item.
 */
public enum CompanyType {
    /** Aktiebolag — limited company. */
    AB,
    /** Enskild firma — sole trader. */
    E,
    /** Handelsbolag — general partnership. */
    HB,
    /** Kommanditbolag — limited partnership. */
    KB,
    /** Ekonomisk förening — cooperative society. */
    EK,
    /** Kooperativ hyresrättsförening — cooperative tenancy association. */
    KHF,
    /** Bostadsrättsförening — tenant-owner association. */
    BRF,
    /** Bostadsförening — housing association. */
    BF,
    /** Sambruksförening — joint cultivation association. */
    SF,
    /** Ideell förening — non-profit association. */
    I,
    /** Stiftelse — foundation. */
    S,
    /** Filial — foreign branch office. */
    FL,
    /** Bankaktiebolag — joint-stock bank. */
    BAB,
    /** Medlemsbank — member bank. */
    MB,
    /** Sparbank — savings bank. */
    SB,
    /** Bankfilial — foreign bank branch. */
    BFL,
    /** Försäkringsaktiebolag — insurance joint-stock company. */
    FAB,
    /** Ömsesidigt försäkringsbolag — mutual insurance company. */
    OFB,
    /** Europabolag (SE) — Societas Europaea. */
    SE,
    /** Europakooperativ (SCE) — Societas Cooperativa Europaea. */
    SCE,
    /** Trygghetsstiftelse — occupational pension foundation. */
    TSF,
    /** Övriga/Other — legal form not covered by the above values. */
    X
}
