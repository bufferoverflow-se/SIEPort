package se.bufferoverflow.sieport.sie4.validator;

import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.SIE4ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {

    public static final Set<SIE4ItemType> MANDATORY_ITEMS_SIE4I = Set.of(
            SIE4ItemType.FLAGGA,
            SIE4ItemType.PROGRAM,
            SIE4ItemType.FORMAT,
            SIE4ItemType.GEN,
            SIE4ItemType.SIETYP,
            SIE4ItemType.FNAMN
    );

    public static final Set<SIE4ItemType> FORBIDDEN_ITEMS_SIE4I = Set.of(
            SIE4ItemType.BKOD,
            SIE4ItemType.OMFATTN,
            SIE4ItemType.IB,
            SIE4ItemType.UB,
            SIE4ItemType.OIB,
            SIE4ItemType.OUB,
            SIE4ItemType.RES,
            SIE4ItemType.PSALDO,
            SIE4ItemType.PBUDGET
    );

    public static final Set<SIE4ItemType> MANDATORY_ITEMS_SIE4E = Set.of(
            SIE4ItemType.FLAGGA,
            SIE4ItemType.PROGRAM,
            SIE4ItemType.FORMAT,
            SIE4ItemType.GEN,
            SIE4ItemType.SIETYP,
            SIE4ItemType.FNAMN,
            SIE4ItemType.RAR,
            SIE4ItemType.KONTO,
            SIE4ItemType.IB,
            SIE4ItemType.UB,
            SIE4ItemType.RES
    );

    private Validator() {
    }

    public static List<ValidationError> validateSie4i(List<SIE4Item> items) {
        List<ValidationError> errors = new ArrayList<>();
        if (!checkMandatoryItems(items, MANDATORY_ITEMS_SIE4I).isEmpty()) {
            errors.add(ValidationError.MISSING_MANDATORY_ITEMS);
        }
        if (!checkForbiddenItems(items, FORBIDDEN_ITEMS_SIE4I).isEmpty()) {
            errors.add(ValidationError.FORBIDDEN_ITEMS_PRESENT);
        }
        return errors;
    }

    public static List<ValidationError> validateSie4e(List<SIE4Item> items) {
        List<ValidationError> errors = new ArrayList<>();
        if (!checkMandatoryItems(items, MANDATORY_ITEMS_SIE4E).isEmpty()) {
            errors.add(ValidationError.MISSING_MANDATORY_ITEMS);
        }
        return errors;
    }

    private static List<SIE4ItemType> checkMandatoryItems(List<SIE4Item> items, Set<SIE4ItemType> mandatory) {
        Set<SIE4ItemType> itemTypes = items.stream()
                .map(SIE4Item::itemType)
                .collect(Collectors.toSet());
        return mandatory.stream()
                .filter(mandatoryItem -> !itemTypes.contains(mandatoryItem))
                .toList();
    }

    private static List<SIE4ItemType> checkForbiddenItems(List<SIE4Item> items, Set<SIE4ItemType> forbidden) {
        Set<SIE4ItemType> itemTypes = items.stream()
                .map(SIE4Item::itemType)
                .collect(Collectors.toSet());
        return forbidden.stream()
                .filter(itemTypes::contains)
                .toList();
    }
}
