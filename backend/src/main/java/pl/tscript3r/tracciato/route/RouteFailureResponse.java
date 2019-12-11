package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.HashMap;
import java.util.Map;

public class RouteFailureResponse implements FailureResponse {

    private final String reason;
    private final int httpStatus;
    private final Map<String, Object> additionalFields = new HashMap<>();

    private RouteFailureResponse addField(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

    private RouteFailureResponse(String reason, int httpStatus) {
        this.reason = reason;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public Integer getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

}
