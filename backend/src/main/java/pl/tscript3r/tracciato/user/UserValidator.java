package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class UserValidator {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final Validator validator;

    UserValidator(UserRepositoryAdapter userRepositoryAdapter) {
        this.userRepositoryAdapter = userRepositoryAdapter;
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    Either<FailureResponse, UserDto> validate(UserDto userDto) {
        final Set<ConstraintViolation<UserDto>> validationResults =
                validator.validate(userDto, Default.class);
        if (validationResults.isEmpty() && isUsernameAndEmailUnique(userDto))
            return Either.right(userDto);
        else
            return Either.left(createUserFailureResponse(userDto, validationResults));
    }

    private UserFailureResponse createUserFailureResponse(UserDto userDto,
                                                          Set<ConstraintViolation<UserDto>> validationResults) {
        final Map<String, String> bindingFails = new HashMap<>();
        validationResults.forEach(objectError ->
                bindingFails.put(objectError.getPropertyPath().toString(), objectError.getMessage())
        );

        if (!isEmailUnique(userDto))
            bindingFails.put("email", "exists");
        if (!isUsernameUnique(userDto))
            bindingFails.put("username", "exists");

        return UserFailureResponse.bindingFail(bindingFails);
    }

    private boolean isUsernameAndEmailUnique(UserDto userDto) {
        return isEmailUnique(userDto) && isUsernameUnique(userDto);
    }

    private boolean isEmailUnique(UserDto userDto) {
        return !userRepositoryAdapter.emailExists(userDto.getEmail());
    }

    private boolean isUsernameUnique(UserDto userDto) {
        return !userRepositoryAdapter.usernameExists(userDto.getUsername());
    }

}
