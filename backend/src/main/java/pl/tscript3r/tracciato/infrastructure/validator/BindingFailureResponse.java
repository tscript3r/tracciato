package pl.tscript3r.tracciato.infrastructure.validator;

import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.HashMap;
import java.util.Map;

public class BindingFailureResponse implements FailureResponse {

    private static final String REASON = "Validation";
    private static final int HTTP_STATUS = 400;

    private final Map<String, Object> additionalFields = new HashMap<>();

    public static BindingFailureResponse get(Map<String, String> bindingFails) {
        return new BindingFailureResponse(bindingFails);
    }

    private BindingFailureResponse(Map<String, String> bindingFails) {
        additionalFields.put("fields", bindingFails);
    }

    @Override
    public String getReason() {
        return REASON;
    }

    @Override
    public Integer getHttpStatus() {
        return HTTP_STATUS;
    }

    @Override
    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

}
