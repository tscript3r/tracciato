package pl.tscript3r.tracciato.scheduled.util;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class UuidListConverter implements AttributeConverter<List<UUID>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<UUID> uuids) {
        return uuids != null ? uuids.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(SPLIT_CHAR)) : "";
    }

    @Override
    public List<UUID> convertToEntityAttribute(String string) {
        return string != null && !string.isEmpty() ? Arrays.stream(string.split(SPLIT_CHAR))
                .map(UUID::fromString)
                .collect(Collectors.toList()) : emptyList();
    }

}
