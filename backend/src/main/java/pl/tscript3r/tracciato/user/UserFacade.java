package pl.tscript3r.tracciato.user;

import io.jsonwebtoken.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserFacade {

    // TODO by editing, deleting, locking user remove cached jwt token

    private final UserRegistration userRegistration;
    private final UserAuthentication userAuthentication;
    private final JWTTokenResolver jwtTokenResolver;
    private final UserResourceAuthorization userResourceAuthorization;
    private final UserDao userDao;

    public InternalResponse<UserDto> register(UserDto userDto) {
        return userRegistration.register(userDto);
    }

    public InternalResponse<UserDto> login(String username, String password) {
        return userAuthentication.login(username, password);
    }

    public InternalResponse<String> getToken(String username) {
        try {
            return userDao.getByUsername(username, UserFailureResponse.invalidCredentials())
                    .flatMap(userDto -> InternalResponse.payload(jwtTokenResolver.getToken(userDto.getUuid())));
        } catch (InvalidKeyException e) {
            log.error(e.getMessage(), e);
            return InternalResponse.failure(GlobalFailureResponse.INTERNAL_SERVER_ERROR);
        }
    }

    public InternalResponse<UUID> validateAndGetUuidFromToken(String token) {
        return InternalResponse.ofOption(jwtTokenResolver.getUuidAndValidateToken(token),
                UserFailureResponse.invalidCredentials());
    }

    public Boolean authorize(String token, UUID resourceOwnerUuid) {
        return validateAndGetUuidFromToken(token)
                .filter(uuid -> userResourceAuthorization.authorize(uuid, resourceOwnerUuid))
                .isDefined();
    }

}