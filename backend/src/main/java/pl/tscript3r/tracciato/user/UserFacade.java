package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

@RequiredArgsConstructor
public class UserFacade {

    private final UserRegistration userRegistration;

    public Either<FailureResponse, UserDto> register(UserDto userDto) {
        return userRegistration.register(userDto);
    }

}