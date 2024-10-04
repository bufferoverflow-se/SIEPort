package se.bufferoverflow.sieport.sie4;

import java.util.List;
import java.util.Optional;

/**
 * A wrapper around a {@link SIE4Item} list providing some convenience methods.
 */
public record SIE4Content(List<SIE4Item> items) {

    /**
     * Retrieves all items of the specified type.
     *
     * @param clazz the type of SIE4Item to be retrieved
     * @return the item requested, or {@link Optional#empty()} if not found
     */
    public <T extends SIE4Item> List<T> getItems(Class<T> clazz) {
        return items.stream()
            .filter(clazz::isInstance)
            .map(clazz::cast)
            .toList();
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
        List<T> filteredItems = items.stream()
            .filter(clazz::isInstance)
            .map(clazz::cast)
            .toList();

        if (filteredItems.size() > 1) {
            throw new SIE4Exception("Expected at most one item of type: " + clazz.getName());
        }

        return filteredItems.stream().findFirst();
    }

    /**
     * Retrieves all verification items.
     */
    public List<SIE4Item.Ver> getVerifications() {
        return getItems(SIE4Item.Ver.class);
    }

    /**
     * Retrieves all organization info.
     * This is considered to be the ADRESS, BKOD, FNAMN, FTYP and ORGNR items.
     */
    public OrgInfo getOrgInfo() {
        Optional<SIE4Item.Adress> addressItem = getItem(SIE4Item.Adress.class);
        return new OrgInfo(
            addressItem.map(SIE4Item.Adress::contact).orElse(null),
            addressItem.map(addr -> "%s %s".formatted(addr.distributionAddress(), addr.postalAddress())).orElse(null),
            addressItem.map(SIE4Item.Adress::tel).orElse(null),
            getItem(SIE4Item.Bkod.class).map(SIE4Item.Bkod::sniCode).orElse(null),
            getItem(SIE4Item.Fnamn.class).map(SIE4Item.Fnamn::companyName).orElse(null),
            getItem(SIE4Item.Ftyp.class).map(SIE4Item.Ftyp::companyType).orElse(null),
            getItem(SIE4Item.OrgNr.class).map(SIE4Item.OrgNr::orgNr).orElse(null)
        );
    }

    /**
     * Retrieves information about the file content
     * This is considered to be the items:
     * FLAGGA, PROGRAM, GEN, KPTYP, VALUTA and RAR
     */
    public FileInfo getFileInfo() {
        return new FileInfo(
            getItem(SIE4Item.Flagga.class).orElse(null),
            getItem(SIE4Item.Program.class).orElse(null),
            getItem(SIE4Item.Gen.class).orElse(null),
            getItem(SIE4Item.Kptyp.class).orElse(null),
            getItem(SIE4Item.Valuta.class).orElse(null),
            getItems(SIE4Item.Rar.class)
        );
    }

    public record OrgInfo(
        String contactPerson,
        String address,
        String phoneNumber,
        Integer sniCode,
        String companyName,
        CompanyType companyType,
        String organizationNumber
    ) {
    }

    public record FileInfo(
        SIE4Item.Flagga flag,
        SIE4Item.Program program,
        SIE4Item.Gen gen,
        SIE4Item.Kptyp kptyp,
        SIE4Item.Valuta valuta,
        List<SIE4Item.Rar> rars
    ) {
    }
}
