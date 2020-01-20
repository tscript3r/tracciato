package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

@RequiredArgsConstructor
class UserAuthentication {

    private final PasswordEncrypt passwordEncrypt;
    private final UserDao userDao;

    InternalResponse<UserDto> login(String username, String password) {
        return userDao.getByUsername(username, UserFailureResponse.invalidCredentials())
                .filterInternal(userDto -> validateCredentials(userDto.getPassword(), password),
                        UserFailureResponse.invalidCredentials());
    }

    private boolean validateCredentials(String actualPassword, String password) {
        return passwordEncrypt.checkPassword(password, actualPassword);
    }

}
