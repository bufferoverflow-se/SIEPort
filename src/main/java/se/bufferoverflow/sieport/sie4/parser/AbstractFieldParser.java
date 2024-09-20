package se.bufferoverflow.sieport.sie4.parser;

import se.bufferoverflow.sieport.sie4.SIE4Exception;
import se.bufferoverflow.sieport.sie4.SIE4Item;
import se.bufferoverflow.sieport.sie4.ObjectReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

abstract class AbstractFieldParser<T extends SIE4Item> {

    private static final Pattern FIELD_PATTERN = Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"|(?:\\{([^{}]*)})|(\\S+)");

    static List<String> tokenizeFields(String fields) {
        List<String> tokens = new ArrayList<>();
        Matcher m = FIELD_PATTERN.matcher(fields);

        while (m.find()) {
            if (m.group(1) != null) {
                // field is quoted, remove backslashes preceding quotes
                tokens.add(m.group(1).replace("\\\"", "\""));
            } else if (m.group(2) != null) {
                // field is an object surrounded with braces
                tokens.add(m.group(2));
            } else {
                tokens.add(m.group(3));
            }
        }
        return tokens;
    }

    static List<ObjectReference> parseObjectReferences(String objectReferences) {
        Objects.requireNonNull(objectReferences, "objectReferences string must not be null");

        List<String> tokens = tokenizeFields(objectReferences);

        if (tokens.isEmpty()) {
            return List.of();
        }

        if (tokens.size() % 2 != 0) {
            throw new SIE4Exception("Invalid object reference list: " + objectReferences);
        }

        return IntStream.range(0, tokens.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(i -> ObjectReference.of(Integer.parseInt(tokens.get(i)), tokens.get(i + 1)))
                .toList();
    }

    static Optional<String> parseOptionalField(String field) {
        Objects.requireNonNull(field, "field must not be null");
        return field.isEmpty() ? Optional.empty() : Optional.of(field);
    }

    T parseFields(String fields) {
        try {
            return parseFields(tokenizeFields(fields), List.of());
        } catch (Exception e) {
            throw new SIE4Exception("Could not parse fields " + fields, e);
        }
    }

    T parseFields(String fields, List<SIE4Item> subItems) {
        return parseFields(tokenizeFields(fields), subItems);
    }

    abstract T parseFields(List<String> fields, List<SIE4Item> subItems);
}
