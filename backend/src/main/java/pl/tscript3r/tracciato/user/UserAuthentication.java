package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

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

}
