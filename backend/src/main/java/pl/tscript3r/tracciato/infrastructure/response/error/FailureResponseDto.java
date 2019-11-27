package pl.tscript3r.tracciato.infrastructure.response.error;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class FailureResponseDto {

    @Getter
    private final String reason;
    private final Map<String, Object> additionalFields = new HashMap<>();

    private FailureResponseDto(String reason) {
        this.reason = reason;
    }

    public static FailureResponseDto get(String reason) {
        return new FailureResponseDto(reason);
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return additionalFields;
    }

    public FailureResponseDto add(String field, Object value) {
        additionalFields.put(field, value);
        return this;
    }

}
