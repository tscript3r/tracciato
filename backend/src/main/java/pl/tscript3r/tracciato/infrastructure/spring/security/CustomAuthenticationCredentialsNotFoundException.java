package pl.tscript3r.tracciato.infrastructure.spring.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

class CustomAuthenticationCredentialsNotFoundException extends AuthenticationCredentialsNotFoundException {

    private final FailureResponse failureResponse;

    CustomAuthenticationCredentialsNotFoundException(FailureResponse failureResponse) {
        super(failureResponse.getReason());
        this.failureResponse = failureResponse;
    }

    FailureResponse getFailureResponse() {
        return failureResponse;
    }

}
