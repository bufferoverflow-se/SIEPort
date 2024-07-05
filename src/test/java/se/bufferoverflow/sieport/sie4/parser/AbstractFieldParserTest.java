package se.bufferoverflow.sieport.sie4.parser;

import org.junit.jupiter.api.Test;
import se.bufferoverflow.sieport.sie4.ObjectReference;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AbstractFieldParserTest {
    @Test
    void shouldTokenizeQuotedFieldsCorrectly() {
        String fields = "\"ab\" \"cd ef\" \"gh\\\"i j\"";
        List<String> expectedTokens = List.of("ab", "cd ef", "gh\"i j");

        List<String> tokens = AbstractFieldParser.tokenizeFields(fields);

        assertEquals(expectedTokens, tokens);
    }

    @Test
    void shouldTokenizeUnquotedFieldsCorrectly() {
        String fields = "ab cd ef gh ij";
        List<String> expectedTokens = List.of("ab", "cd", "ef", "gh", "ij");

        List<String> tokens = AbstractFieldParser.tokenizeFields(fields);

        assertNotNull(tokens);
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void shouldTokenizeEmptyFieldsCorrectly() {
        List<String> tokens = AbstractFieldParser.tokenizeFields("");

        assertNotNull(tokens);
        assertThat(tokens).isEmpty();
    }

    @Test
    void shouldTokenizeEmptyObjectCorrectly() {
        List<String> tokens = AbstractFieldParser.tokenizeFields("{}");

        assertNotNull(tokens);
        assertThat(tokens).isEqualTo(List.of(""));
    }

    @Test
    void shouldTokenizeFieldsWithBracesCorrectly() {
        String fields = "{ab} {cd ef} {gh ij kl mn}";
        List<String> expectedTokens = List.of("ab", "cd ef", "gh ij kl mn");

        List<String> tokens = AbstractFieldParser.tokenizeFields(fields);

        assertNotNull(tokens);
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void shouldTokenizeFieldsWithMixedTokensCorrectly() {
        String fields = "\"ab\" {cd ef} gh ij \"kl mn\" {op qr} st {} uv";
        List<String> expectedTokens = List.of("ab", "cd ef", "gh", "ij", "kl mn", "op qr", "st", "", "uv");

        List<String> tokens = AbstractFieldParser.tokenizeFields(fields);

        assertNotNull(tokens);
        assertEquals(expectedTokens, tokens);
    }

    @Test
    void shouldParseEmptyObjectReferenceCorrectly() {
        String objectReference = "";
        List<ObjectReference> parsedObjectReference = AbstractFieldParser.parseObjectReferences(objectReference);
        assertThat(parsedObjectReference).isEmpty();
   }

   @Test
   void shouldParseValidObjectReferenceCorrectly() {
       String objectReference = "5 \"10\"";
       List<ObjectReference> expectedObjectReference = List.of(ObjectReference.of(5, "10"));
       List<ObjectReference> parsedObjectReferences = AbstractFieldParser.parseObjectReferences(objectReference);
       assertEquals(expectedObjectReference, parsedObjectReferences);
   }

    @Test
    void shouldParseValidObjectReferenceListCorrectly() {
        String objectReference = "1 Nord 6 \"0010\"";
        List<ObjectReference> expectedObjectReference = List.of(
                ObjectReference.of(1, "Nord"),
                ObjectReference.of(6, "0010")
        );
        List<ObjectReference> parsedObjectReferences = AbstractFieldParser.parseObjectReferences(objectReference);
        assertEquals(expectedObjectReference, parsedObjectReferences);
    }

   @Test
   void shouldThrowExceptionForInvalidObjectReference() {
       String objectReference = "5";
       assertThrows(IllegalArgumentException.class, () -> AbstractFieldParser.parseObjectReferences(objectReference));

       String objectReferenceList = "5 Nord 6";
       assertThrows(IllegalArgumentException.class, () -> AbstractFieldParser.parseObjectReferences(objectReferenceList));

       String nullReference = null;
       assertThrows(NullPointerException.class, () -> AbstractFieldParser.parseObjectReferences(nullReference));
   }

   @Test
   void shouldParseNonEmptyFieldCorrectly() {
       String field = "Testing Optional Field";
       Optional<String> parsedField = AbstractFieldParser.parseOptionalField(field);

       assertThat(parsedField).hasValue(field);
   }

   @Test
   void shouldParseEmptyFieldCorrectly() {
       String field = "";
       Optional<String> parsedField = AbstractFieldParser.parseOptionalField(field);

       assertThat(parsedField).isEmpty();
   }
}
