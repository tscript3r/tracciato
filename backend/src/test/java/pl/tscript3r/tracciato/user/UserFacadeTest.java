package pl.tscript3r.tracciato.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.user.UserConst.*;

@DisplayName("User facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
public class UserFacadeTest {

    UserFacade userFacade;
    UserRepositoryAdapter userRepositoryAdapter;
    UserEntity existingJohnUserEntity = getJohnUserEntity();

    public static UserFacade getUserFacade(UserRepositoryAdapter userInMemoryRepositoryAdapter) {
        var existingJohnUserEntity = getJohnUserEntity();
        existingJohnUserEntity.setPassword(new BCryptPasswordEncrypt().encryptPassword(JOHNS_PASSWORD));
        userInMemoryRepositoryAdapter.save(existingJohnUserEntity);
        return UserSpringConfiguration.getInMemoryUserFacade(userInMemoryRepositoryAdapter);
    }

    @BeforeEach
    void setUp() {
        userRepositoryAdapter = new UserInMemoryRepositoryAdapter();
        userFacade = UserSpringConfiguration.getInMemoryUserFacade(userRepositoryAdapter);
        existingJohnUserEntity = getJohnUserEntity();
        existingJohnUserEntity.setPassword(new BCryptPasswordEncrypt().encryptPassword(JOHNS_PASSWORD));
        userRepositoryAdapter.save(existingJohnUserEntity);
    }

    @Test
    void register_Should_SuccessfullyRegisterNewUser_When_ValidUserDtoIsPassed() {
        // given
        var userDto = UserValidatorTest.getValidEdyUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertTrue(result.isRight());
    }

    @Test
    void register_Should_SetUsersUuid_When_SuccessfullyRegistered() {
        // given
        var userDto = UserValidatorTest.getValidEdyUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertNotNull(result.get().getUuid());
    }

    @Test
    void register_Should_EncodeUsersPassword_When_SuccessfullyRegistered() {
        // given
        var userDto = UserValidatorTest.getValidEdyUserDto();

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

    @Test
    void login_Should_SuccessfullyLogin_When_GivenCredentialsAreExistingAndMatching() {
        // given
        var username = existingJohnUserEntity.getUsername();
        var password = JOHNS_PASSWORD;

        // when
        var results = userFacade.login(username, password);

        // then
        assertTrue(results.isRight());
        assertEquals(existingJohnUserEntity.getUsername(), results.get().getUsername());
    }

    @Test
    void login_Should_Fail_When_GivenUsernameIsExistingButPasswordIsWrong() {
        // given
        var username = existingJohnUserEntity.getUsername();
        var password = "WRONG PASSWORD";

        // when
        var results = userFacade.login(username, password);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void login_Should_Fail_When_GivenUsernameIsNotExisting() {
        // given
        var username = "NotExistingUsername";
        var password = JOHNS_PASSWORD;

        // when
        var results = userFacade.login(username, password);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getToken_Should_ReturnJwtToken_When_ExistingUsernamePassed() {
        // given
        var existingUsername = JOHNS_USERNAME;

        // when
        var tokenEither = userFacade.getToken(existingUsername);

        // then
        assertTrue(tokenEither.isRight());
    }

    @Test
    void validateAndGetUuidFromToken_Should_ReturnExistingUuid_When_ValidTokenIsPassed() {
        // given
        var token = userFacade.getToken(JOHNS_USERNAME).get();

        // when
        var uuidOption = userFacade.validateAndGetUuidFromToken(TOKEN_PREFIX + token);

        // then
        assertTrue(uuidOption.isRight());
        assertEquals(JOHNS_UUID, uuidOption.get());
    }

    @Test
    void authorize_Should_ReturnFailureResponse_When_TokenIsInvalid() {
        // given
        var invalidToken = "Bearer INVALID";

        // when
        var results = userFacade.authorize(invalidToken, JOHNS_UUID, getJohnUserEntity());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void authorize_Should_ReturnFailureResponse_When_ResourceOwnerUuidIsDifferentThenUsersUuidFromToken() {
        // given
        var validToken = userFacade.getToken(JOHNS_USERNAME);

        // when
        var results = userFacade.authorize(validToken.get(), UUID.randomUUID(), getJohnUserEntity());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void authorize_Should_ReturnRight_When_ResourceOwnerUuidIsSameAsUsersUuidFromToken() {
        // given
        var validToken = userFacade.getToken(JOHNS_USERNAME);

        // when
        var results = userFacade.authorize("Bearer " + validToken.get(), existingJohnUserEntity.getUuid(), getJohnUserEntity());

        // then
        assertTrue(results.isRight());
    }

}