package pl.tscript3r.tracciato.user;

import io.jsonwebtoken.security.InvalidKeyException;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

@RequiredArgsConstructor
public class UserFacade {

    private final UserRegistration userRegistration;
    private final UserAuthentication userAuthentication;
    private final JWTTokenResolver jwtTokenResolver;

    public Either<FailureResponse, UserDto> register(UserDto userDto) {
        return userRegistration.register(userDto);
    }

    public Either<FailureResponse, UserDto> login(String username, String password) {
        return userAuthentication.login(username, password);
    }

    public Either<FailureResponse, String> getToken(String username) {
        var user = userAuthentication.findByUsername(username);
        if (user.isDefined()) {
            var uuid = user.get().getUuid();
            try {
                return Either.right(jwtTokenResolver.getToken(uuid));
            } catch (InvalidKeyException e) {
                return Either.left(GlobalFailureResponse.INTERNAL_SERVER_ERROR);
            }
        } else
            return Either.left(UserFailureResponse.invalidCredentials());
    }

    public Either<FailureResponse, UUID> validateAndGetUuidFromToken(String token) {
        return jwtTokenResolver.getUuidAndValidateToken(token)
                .toEither(UserFailureResponse.invalidCredentials());
    }

}