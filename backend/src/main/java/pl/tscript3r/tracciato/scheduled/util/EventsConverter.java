package pl.tscript3r.tracciato.scheduled.util;

import javax.persistence.AttributeConverter;
import java.util.*;
import java.util.stream.Collectors;


public class EventsConverter implements AttributeConverter<List<Map<String, String>>, String> {

    private static final String EVENT_SPLIT_CHAR = "~";
    private static final String FIELDS_SPLIT_CHAR = ";";
    private static final String KEY_VALUE_SPLIT_CHAR = "@";

    @Override
    public String convertToDatabaseColumn(List<Map<String, String>> attribute) {
        return attribute != null ? attribute.stream()
                .map(map ->
                        map.entrySet()
                                .stream()
                                .map(entry -> entry.getKey() + KEY_VALUE_SPLIT_CHAR + entry.getValue())
                                .collect(Collectors.joining(FIELDS_SPLIT_CHAR)))
                .collect(Collectors.joining(EVENT_SPLIT_CHAR)) : "";
    }

    @Override
    public List<Map<String, String>> convertToEntityAttribute(String string) {
        return string != null && !string.isEmpty() ? Arrays.stream(string.split(EVENT_SPLIT_CHAR))
                .map(eventString ->
                        Arrays.stream(eventString.split(FIELDS_SPLIT_CHAR))
                                .map(s -> s.split(KEY_VALUE_SPLIT_CHAR))
                                .collect(Collectors.toMap(keyStr -> keyStr[0],
                                        valueStr -> valueStr[1],
                                        (o, o2) -> o,
                                        LinkedHashMap::new))
                ).collect(Collectors.toList()) : Collections.emptyList();
    }

}
