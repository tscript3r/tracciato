package pl.tscript3r.tracciato.scheduled.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UUID List Converter")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class UuidListConverterTest {

    final UuidListConverter uuidConverter = new UuidListConverter();
    final UUID first = UUID.randomUUID();
    final UUID second = UUID.randomUUID();
    final UUID third = UUID.randomUUID();
    final String toListString = first.toString() + ";" +
            second.toString() + ";" +
            third.toString();
    List<UUID> uuidList;

    @BeforeEach
    void setUp() {
        uuidList = Arrays.asList(first, second, third);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnEqualString_When_PopulatedListGiven() {
        // given
        var result = uuidConverter.convertToDatabaseColumn(uuidList);

        // then
        assertEquals(toListString, result);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnEmptyString_When_EmptyListGiven() {
        // given
        var result = uuidConverter.convertToDatabaseColumn(Collections.emptyList());

        // then
        assertEquals("", result);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnEmptyString_When_NullArgumentGiven() {
        // given
        var result = uuidConverter.convertToDatabaseColumn(null);

        // then
        assertEquals("", result);
    }

    @Test
    void convertToEntityAttribute_Should_ReturnEmptyList_When_EmptyStringGiven() {
        // given
        var result = uuidConverter.convertToEntityAttribute("");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_Should_ReturnEmptyList_When_NullGiven() {
        // given
        var result = uuidConverter.convertToEntityAttribute(null);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_Should_ReturnListWithThreeExpectedUuids_When_UuidsContainingStringGiven() {
        // given
        var result = uuidConverter.convertToEntityAttribute(toListString);

        // then
        assertTrue(result.contains(first));
        assertTrue(result.contains(second));
        assertTrue(result.contains(third));
    }
}