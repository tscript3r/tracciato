package pl.tscript3r.tracciato.user;

import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.user.api.UserDto;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

class UserValidator extends DefaultValidator<UserDto> {

    private final UserRepositoryAdapter userRepositoryAdapter;

    UserValidator(Validator validator, UserRepositoryAdapter userRepositoryAdapter) {
        super(validator);
        this.userRepositoryAdapter = userRepositoryAdapter;
    }

    @Override
    public Map<String, String> additionalConstraints(UserDto object) {
        final Map<String, String> bindingFails = new HashMap<>();
        if (!isEmailUnique(object))
            bindingFails.put("email", "exists");
        if (!isUsernameUnique(object))
            bindingFails.put("username", "exists");
        return bindingFails;
    }

    private boolean isEmailUnique(UserDto userDto) {
        return !userRepositoryAdapter.emailExists(userDto.getEmail());
    }

    private boolean isUsernameUnique(UserDto userDto) {
        return !userRepositoryAdapter.usernameExists(userDto.getUsername());
    }

}
