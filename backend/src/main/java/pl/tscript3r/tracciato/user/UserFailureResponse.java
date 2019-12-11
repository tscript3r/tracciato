package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.HashMap;
import java.util.Map;

public class UserFailureResponse implements FailureResponse {

    static final String USERNAME_NOT_FOUND_REASON = "Username not found";
    static final String USER_ID_NOT_FOUND_REASON = "User id not found";
    static final String INVALID_CREDENTIALS_REASON = "Invalid credentials";

    private final String reason;
    private final int httpStatus;
    private final Map<String, Object> additionalFields = new HashMap<>();

    static FailureResponse idNotFound(long id) {
        return new UserFailureResponse(USER_ID_NOT_FOUND_REASON, 404)
                .addField("id", id);
    }

    static FailureResponse usernameNotFound(String username) {
        return new UserFailureResponse(USERNAME_NOT_FOUND_REASON, 404)
                .addField("username", username);
    }

    public static FailureResponse invalidCredentials() {
        return new UserFailureResponse(INVALID_CREDENTIALS_REASON, 400);
    }

    private UserFailureResponse(String reason, int httpStatus) {
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

    private UserFailureResponse addField(String key, Object value) {
        additionalFields.put(key, value);
        return this;
    }

}
