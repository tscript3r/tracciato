package pl.tscript3r.tracciato.infrastructure.response.error;

import pl.tscript3r.tracciato.infrastructure.response.Response;

import java.util.HashMap;
import java.util.Map;

public interface FailureResponse extends Response {

    String getReason();

    default Map<String, Object> getAdditionalFields() {
        return new HashMap<>();
    }

}
