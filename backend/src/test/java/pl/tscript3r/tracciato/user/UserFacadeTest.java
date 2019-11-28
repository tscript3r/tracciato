package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class UserFacadeTest {

    UserFacade userFacade;

    @BeforeEach
    void setUp() {
        userFacade = UserSpringConfiguration.getInMemoryUserFacade();
    }

    @Test
    void register_Should_SuccessfullyRegisterNewUser_When_ValidUserDtoIsPassed() {
        // given
        var userDto = UserValidatorTest.getValidJohnsUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertTrue(result.isRight());
    }

    @Test
    void register_Should_SetUsersUuid_When_SuccessfullyRegistered() {
        // given
        var userDto = UserValidatorTest.getValidJohnsUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertNotNull(result.get().getUuid());
    }

    @Test
    void register_Should_EncodeUsersPassword_When_SuccessfullyRegistered() {
        // given
        var userDto = UserValidatorTest.getValidJohnsUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        var savedUserDto = result.get();
        assertNotNull(savedUserDto.getPassword());
        assertNotEquals("", savedUserDto.getPassword());
        assertNotEquals(userDto.getPassword(), savedUserDto.getPassword());
    }

    @Test
    void register_Should_ReturnUserFailureResponseAndNotRegisterNewUser_When_NonValidUserDtoIsPassed() {
        // given
        var nonValidUserDto = UserValidatorTest.getValidJohnsUserDto();
        nonValidUserDto.setEmail("not valid email");

        // when
        var result = userFacade.register(nonValidUserDto);

        // then
        assertTrue(result.isLeft());
    }

}