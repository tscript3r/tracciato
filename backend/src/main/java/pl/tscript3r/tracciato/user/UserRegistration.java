package pl.tscript3r.tracciato.user;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;

@RequiredArgsConstructor
class UserRegistration {

    private final UserValidator userValidator;
    private final PasswordEncrypt passwordEncoder;
    private final UserDao userDao;

    InternalResponse<UserDto> register(UserDto userDto) {
        return userValidator.validate(userDto)
                .peek(dto -> {
                    var password = dto.getPassword();
                    dto.setPassword(passwordEncoder.encryptPassword(password));
                    dto.setUuid(UUID.randomUUID());
                })
                .flatMap(userDao::save);
    }

}
