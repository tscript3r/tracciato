package pl.tscript3r.tracciato.infrastructure.response.error;

import pl.tscript3r.tracciato.infrastructure.response.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

public interface FaultResponse {

    ResponseStatus getResponseStatus();

    Integer getHttpStatus();

    String getReason();

    default Map<String, Object> getAdditionalFields() {
        return new HashMap<>();
    }

}
