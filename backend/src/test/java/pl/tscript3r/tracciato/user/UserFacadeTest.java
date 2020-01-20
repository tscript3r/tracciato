package pl.tscript3r.tracciato.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ConcurrentStressTest;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.user.api.UserDto;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.user.UserConst.*;

@Slf4j
@DisplayName("User facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
public class UserFacadeTest implements ConcurrentStressTest {

    public static final int THREADS_COUNT = 10;

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
        var userDto = UserConst.getValidEdyUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertTrue(result.isRight());
    }

    @Test
    void register_Should_SetUsersUuid_When_SuccessfullyRegistered() {
        // given
        var userDto = UserConst.getValidEdyUserDto();

        // when
        var result = userFacade.register(userDto);

        // then
        assertNotNull(result.get().getUuid());
    }

    @Test
    void register_Should_EncodeUsersPassword_When_SuccessfullyRegistered() {
        // given
        var userDto = UserConst.getValidEdyUserDto();
        var originPassword = EDY_PASSWORD;

        // when
        var result = userFacade.register(userDto);

        // then
        var savedUserDto = result.get();
        assertNotNull(savedUserDto.getPassword());
        assertNotEquals("", savedUserDto.getPassword());
        assertNotEquals(originPassword, savedUserDto.getPassword());
    }

    @Test
    void register_Should_ReturnUserFailureResponseAndNotRegisterNewUser_When_NonValidUserDtoIsPassed() {
        // given
        var nonValidUserDto = UserConst.getValidJohnsUserDto();
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
        var results = userFacade.authorize(invalidToken, JOHNS_UUID);

        // then
        assertTrue(results);
    }

    @Test
    void authorize_Should_ReturnFailureResponse_When_ResourceOwnerUuidIsDifferentThenUsersUuidFromToken() {
        // given
        var validToken = userFacade.getToken(JOHNS_USERNAME);

        // when
        var results = userFacade.authorize(validToken.get(), UUID.randomUUID());

        // then
        assertTrue(results);
    }

    @Test
    void authorize_Should_ReturnRight_When_ResourceOwnerUuidIsSameAsUsersUuidFromToken() {
        // given
        var validToken = userFacade.getToken(JOHNS_USERNAME);

        // when
        var results = userFacade.authorize("Bearer " + validToken.get(), existingJohnUserEntity.getUuid());

        // then
        assertTrue(results);
    }

    @Test
    void register_Should_Create120Users_When_HasMax10SecondsForIt() throws ExecutionException {
        var results = concurrentStressTest(THREADS_COUNT, 120, 10_000, () -> {
            var userDto = new UserDto();
            userDto.setUsername(RandomStringUtils.randomAlphanumeric(10));
            userDto.setPassword(RandomStringUtils.randomAlphanumeric(10));
            userDto.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@mail.com");
            return userFacade.register(userDto);
        });
        if (results.getUncompletedCount() > 0)
            log.warn("Method has not completed required tasks in time ({} unfinished)",
                    results.getUncompletedCount());
        log.info(results.toString());
    }

    @Test
    void login_Should_Validate150LoginAttempts_When_HasMax10SecondsForIt() throws ExecutionException {
        var results = concurrentStressTest(THREADS_COUNT, 150, 10_000, () ->
                userFacade.login(existingJohnUserEntity.getUsername(), JOHNS_PASSWORD));
        if (results.getUncompletedCount() > 0)
            log.warn("Method has not completed required tasks in time ({} unfinished)",
                    results.getUncompletedCount());
        log.info(results.toString());
    }

    @Test
    void getToken_Should_Create1000JWTTokens_When_HasMax10SecondsForIt() throws ExecutionException {
        var results = concurrentStressTest(THREADS_COUNT, 1000, 10_000, () ->
                userFacade.getToken(existingJohnUserEntity.getUsername()));
        if (results.getUncompletedCount() > 0)
            log.warn("Method has not completed required tasks in time ({} unfinished)",
                    results.getUncompletedCount());
        log.info(results.toString());
    }

    @Test
    void validateAndGetUuidFromToken_Should_Validate10000JwtTokens_When_HasMax10SecondsForIt() throws ExecutionException {
        var results = concurrentStressTest(THREADS_COUNT, 10000, 10_000, () ->
                userFacade.validateAndGetUuidFromToken(userFacade.getToken(existingJohnUserEntity.getUsername()).get()));
        if (results.getUncompletedCount() > 0)
            log.warn("Method has not completed required tasks in time ({} unfinished)",
                    results.getUncompletedCount());
        log.info(results.toString());
    }

    @Test
    void authorize_Should_Validate10000Requests_When_HasMax10SecondsForIt() throws ExecutionException {
        var results = concurrentStressTest(THREADS_COUNT, 10000, 10_000, () ->
                userFacade.authorize(userFacade.getToken(existingJohnUserEntity.getUsername()).get(), existingJohnUserEntity.getUuid()));
        if (results.getUncompletedCount() > 0)
            log.warn("Method has not completed required tasks in time ({} unfinished)",
                    results.getUncompletedCount());
        log.info(results.toString());
    }

}