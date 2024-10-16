package se.bufferoverflow.sieport.sie4;

import java.util.List;
import java.util.Optional;

/**
 * A wrapper around an {@link SIE4Item} list providing convenience access methods.
 */
public record SIE4Items(List<SIE4Item> items) {

    /**
     * Retrieves all items of the specified type.
     *
     * @param clazz the type of SIE4Item to be retrieved
     * @return items requested, or empty list if none found
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
}
