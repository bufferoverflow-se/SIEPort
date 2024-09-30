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
        return new OrgInfo(
            getItem(SIE4Item.Adress.class).orElse(null),
            getItem(SIE4Item.Bkod.class).orElse(null),
            getItem(SIE4Item.Fnamn.class).orElse(null),
            getItem(SIE4Item.Ftyp.class).orElse(null),
            getItem(SIE4Item.OrgNr.class).orElse(null)
        );
    }

    public record OrgInfo(
        SIE4Item.Adress adress,
        SIE4Item.Bkod bkod,
        SIE4Item.Fnamn fnamn,
        SIE4Item.Ftyp ftyp,
        SIE4Item.OrgNr orgNr
    ) {
    }
}
