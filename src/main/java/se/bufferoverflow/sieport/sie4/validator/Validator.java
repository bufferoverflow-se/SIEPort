package se.bufferoverflow.sieport.sie4.validator;

import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.SIE4ItemType;
import se.bufferoverflow.sieport.sie4.YearNumber;

import java.util.ArrayList;
import java.util.HashSet;
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
        List<SIE4ItemType> missing = checkMandatoryItems(items, MANDATORY_ITEMS_SIE4I);
        if (!missing.isEmpty()) {
            errors.add(new ValidationError.MissingMandatoryItems(Set.copyOf(missing)));
        }
        List<SIE4ItemType> forbidden = checkForbiddenItems(items, FORBIDDEN_ITEMS_SIE4I);
        if (!forbidden.isEmpty()) {
            errors.add(new ValidationError.ForbiddenItemsPresent(Set.copyOf(forbidden)));
        }
        return errors;
    }

    public static List<ValidationError> validateSie4e(List<SIE4Item> items) {
        List<ValidationError> errors = new ArrayList<>();
        List<SIE4ItemType> missing = checkMandatoryItems(items, MANDATORY_ITEMS_SIE4E);
        if (!missing.isEmpty()) {
            errors.add(new ValidationError.MissingMandatoryItems(Set.copyOf(missing)));
        }
        Set<SIE4ItemType> missingCurrentYear = checkCurrentYearBalanceItems(items);
        if (!missingCurrentYear.isEmpty()) {
            errors.add(new ValidationError.MissingCurrentYearItems(missingCurrentYear));
        }
        return errors;
    }

    private static Set<SIE4ItemType> checkCurrentYearBalanceItems(List<SIE4Item> items) {
        Set<SIE4ItemType> missing = new HashSet<>();
        if (items.stream().noneMatch(i -> i instanceof SIE4Item.Ib ib && ib.yearNumber().equals(YearNumber.CURRENT_YEAR))) {
            missing.add(SIE4ItemType.IB);
        }
        if (items.stream().noneMatch(i -> i instanceof SIE4Item.Ub ub && ub.yearNumber().equals(YearNumber.CURRENT_YEAR))) {
            missing.add(SIE4ItemType.UB);
        }
        if (items.stream().noneMatch(i -> i instanceof SIE4Item.Res res && res.yearNumber().equals(YearNumber.CURRENT_YEAR))) {
            missing.add(SIE4ItemType.RES);
        }
        return missing;
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
