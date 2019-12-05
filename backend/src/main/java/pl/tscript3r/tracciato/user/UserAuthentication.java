package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

@RequiredArgsConstructor
class UserAuthentication {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final PasswordEncrypt passwordEncrypt;

    synchronized Either<FailureResponse, UserDto> login(String username, String password) {
        return userRepositoryAdapter.findByUsername(username)
                .filter(userEntity -> validateCredentials(userEntity, password))
                .map(UserMapper::map)
                .toEither(UserFailureResponse.invalidCredentials());
    }

    private boolean validateCredentials(UserEntity userEntity, String password) {
        return passwordEncrypt.checkPassword(password, userEntity.getPassword());
    }

    synchronized Option<UserDto> findByUsername(String username) {
        return userRepositoryAdapter.findByUsername(username)
                .map(UserMapper::map);
    }

    synchronized Option<UserDto> findByUuid(UUID uuid) {
        return userRepositoryAdapter.findByUuid(uuid)
                .map(UserMapper::map);
    }

}
