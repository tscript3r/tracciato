package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.user.api.UserDto;

import javax.validation.Validation;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.tracciato.user.UserConst.USERNAME_FIELD;
import static pl.tscript3r.tracciato.user.UserConst.getValidJohnsUserDto;

@DisplayName("User validator")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    UserValidator userValidator;

    @Mock
    UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        userValidator = new UserValidator(validator, userRepositoryAdapter);
    }

    @Test
    void validate_Should_ReturnUserDto_When_UserDtoContainsOnlyValidValues() {
        // given
        var validUserDto = getValidJohnsUserDto();

        // when
        var validationResults = userValidator.validate(validUserDto);

        // then
        assertTrue(validationResults.contains(validUserDto));
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_EmailAlreadyExistsInDb() {
        // given
        when(userRepositoryAdapter.emailExists(any())).thenReturn(true);
        var validUserDto = getValidJohnsUserDto();

        // when
        var validationResults = userValidator.validate(validUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey("email"));
        assertEquals(1, fieldsMap.size());
    }

    private Map<String, String> getFieldsMap(Either<FailureResponse, UserDto> response) {
        var failureResponse = response.getLeft();
        return (Map<String, String>) failureResponse.getAdditionalFields().get("fields");
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_UsernameAlreadyExistsInDb() {
        // given
        when(userRepositoryAdapter.usernameExists(any())).thenReturn(true);
        var validUserDto = getValidJohnsUserDto();

        // when
        var validationResults = userValidator.validate(validUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey(USERNAME_FIELD));
        assertEquals(1, fieldsMap.size());
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_UsernameIsNull() {
        // given
        var emptyUsernameJohnUserDto = getValidJohnsUserDto();
        emptyUsernameJohnUserDto.setUsername(null);

        // when
        var validationResults = userValidator.validate(emptyUsernameJohnUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey(USERNAME_FIELD));
        assertEquals(1, fieldsMap.size());
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_UsernameIsEmpty() {
        // given
        var emptyUsernameJohnUserDto = getValidJohnsUserDto();
        emptyUsernameJohnUserDto.setUsername("");

        // when
        var validationResults = userValidator.validate(emptyUsernameJohnUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey(USERNAME_FIELD));
        assertEquals(1, fieldsMap.size());
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_UsernameIsToShort() {
        // given
        var emptyUsernameJohnUserDto = getValidJohnsUserDto();
        emptyUsernameJohnUserDto.setUsername("123");

        // when
        var validationResults = userValidator.validate(emptyUsernameJohnUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey(USERNAME_FIELD));
        assertEquals(1, fieldsMap.size());
    }

    @Test
    void validate_Should_ReturnFailureResponse_When_UsernameIsToLong() {
        // given
        var emptyUsernameJohnUserDto = getValidJohnsUserDto();
        emptyUsernameJohnUserDto.setUsername(new String(new char[13]).replace('\0', ' '));

        // when
        var validationResults = userValidator.validate(emptyUsernameJohnUserDto);

        // then
        var fieldsMap = getFieldsMap(validationResults);

        assertTrue(fieldsMap.containsKey(USERNAME_FIELD));
        assertEquals(1, fieldsMap.size());
    }

}