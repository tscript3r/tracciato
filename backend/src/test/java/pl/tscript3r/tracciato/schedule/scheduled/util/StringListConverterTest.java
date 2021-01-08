package pl.tscript3r.tracciato.schedule.scheduled.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.scheduled.util.StringListConverter;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("String List Converter")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class StringListConverterTest {

    List<String> stringList;
    String toListString = "A;B;C";
    StringListConverter stringListConverter = new StringListConverter();

    @BeforeEach
    void setup() {
        stringList = Arrays.asList("A", "B", "C");
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnStringWithTwoSeparators_When_CollectionWithThreeElementsPassed() {
        // when
        var results = stringListConverter.convertToDatabaseColumn(stringList);

        // then
        assertNotNull(results);
        assertEquals(2, StringUtils.countMatches(results, ";"));
    }

    @Test
    void convertToDatabaseColumn_Should_ReturnStringWhichCanBeSplitIntoArraySize3_When_CollectionWithThreeElementsPassed() {
        // when
        var results = stringListConverter.convertToDatabaseColumn(stringList);

        // then
        assertNotNull(results);
        assertTrue(() -> results.split(";").length == 3);
    }

    @Test
    void convertToEntityAttribute_Should_ReturnListWith3Elements_When_StringWith2SeparatorsPassed() {
        // when
        var results = stringListConverter.convertToEntityAttribute(toListString);

        // then
        assertEquals(3, results.size());
    }

}