package pl.tscript3r.tracciato.scheduled.util;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Events Converter")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class EventsConverterTest {

    final String EVENTS_STRING = "A@A;BB@BB;CCC@CCC~D@D;EE@EE;FFF@FFF";
    final LinkedHashMap<String, String> firstMap = new LinkedHashMap<>();
    final LinkedHashMap<String, String> secondMap = new LinkedHashMap<>();
    final List<Map<String, String>> mapList = new ArrayList<>();
    final EventsConverter eventsConverter = new EventsConverter();

    @BeforeEach
    void beforeEach() {
        firstMap.clear();
        firstMap.put("A", "A");
        firstMap.put("BB", "BB");
        firstMap.put("CCC", "CCC");
        secondMap.clear();
        secondMap.put("D", "D");
        secondMap.put("EE", "EE");
        secondMap.put("FFF", "FFF");
        mapList.clear();
        mapList.add(firstMap);
        mapList.add(secondMap);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnEmptyString_When_NullAttributePassed() {
        // when
        var result = eventsConverter.convertToDatabaseColumn(null);

        assertEquals("", result);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnEmptyString_When_EmptyStringPassed() {
        // when
        var result = eventsConverter.convertToDatabaseColumn(Collections.emptyList());

        assertEquals("", result);
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnExpectedString_When_Called() {
        // when
        var result = eventsConverter.convertToDatabaseColumn(mapList);

        // then
        assertEquals(EVENTS_STRING, result);
    }

    @Test
    void convertToEntityAttribute_Should_ReturnEmptyList_When_NullStringPassed() {
        // when
        var result = eventsConverter.convertToEntityAttribute(null);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_Should_ReturnEmptyList_When_EmptyStringPassed() {
        // when
        var result = eventsConverter.convertToEntityAttribute("");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void convertToEntityAttribute_Should_ReturnListWithTwoEventsMap_When_Called() {
        // when
        var result = eventsConverter.convertToEntityAttribute(EVENTS_STRING);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void convertToEntityAttribute_Should_ReturnListWithTwoEventsWhichAreContainingExceptedValues_When_Called() {
        // when
        var result = eventsConverter.convertToEntityAttribute(EVENTS_STRING);

        // then
        assertEquals(2, result.size());
        assertTrue(Maps.difference(firstMap, result.get(0)).areEqual());
        assertTrue(Maps.difference(secondMap, result.get(1)).areEqual());
    }

}