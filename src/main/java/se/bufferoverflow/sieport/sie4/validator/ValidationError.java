package se.bufferoverflow.sieport.sie4.validator;

import se.bufferoverflow.sieport.sie4.SIE4ItemType;

import java.util.Set;

public sealed interface ValidationError {
    Set<SIE4ItemType> offendingItems();

    record MissingMandatoryItems(Set<SIE4ItemType> offendingItems) implements ValidationError {}
    record MissingCurrentYearItems(Set<SIE4ItemType> offendingItems) implements ValidationError {}
    record ForbiddenItemsPresent(Set<SIE4ItemType> offendingItems) implements ValidationError {}
}
