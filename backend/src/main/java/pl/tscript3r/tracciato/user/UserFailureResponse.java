package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.infrastructure.response.error.AbstractFailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

public class UserFailureResponse extends AbstractFailureResponse {

    private static final String USERNAME_NOT_FOUND_REASON = "Username not found";
    private static final String USER_ID_NOT_FOUND_REASON = "User id not found";
    private static final String INVALID_CREDENTIALS_REASON = "Invalid credentials";

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
        super(reason, httpStatus);
    }

}
