package pl.tscript3r.tracciato.user;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

@RequiredArgsConstructor
class UserAuthentication {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final PasswordEncrypt passwordEncrypt;

    synchronized InternalResponse<UserDto> login(String username, String password) {
        return InternalResponse.fromOption(userRepositoryAdapter.findByUsername(username)
                .filter(userEntity -> validateCredentials(userEntity, password))
                .map(UserMapper::map), UserFailureResponse.invalidCredentials());
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
