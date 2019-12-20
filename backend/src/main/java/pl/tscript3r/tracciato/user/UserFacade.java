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
    private final UserResourceAuthorization userResourceAuthorization;

    public Either<FailureResponse, UserDto> register(UserDto userDto) {
        return userRegistration.register(userDto);
    }

    public Either<FailureResponse, UserDto> login(String username, String password) {
        return userAuthentication.login(username, password);
    }

    public Either<FailureResponse, String> getToken(String username) {
        try {
            return userAuthentication.findByUsername(username)
                    .toEither(UserFailureResponse.invalidCredentials())
                    .flatMap(userDto -> Either.right(jwtTokenResolver.getToken(userDto.getUuid())));
        } catch (InvalidKeyException e) {
            return Either.left(GlobalFailureResponse.INTERNAL_SERVER_ERROR);
        }
    }

    public Either<FailureResponse, UUID> validateAndGetUuidFromToken(String token) {
        return jwtTokenResolver.getUuidAndValidateToken(token)
                .toEither(UserFailureResponse.invalidCredentials());
    }

    public Boolean authorize(String token, UUID resourceOwnerUuid) {
        return validateAndGetUuidFromToken(token)
                .filter(uuid -> userResourceAuthorization.authorize(uuid, resourceOwnerUuid))
                .isDefined();
    }

}