package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.user.UserJson;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.user.UserJson.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class UsersFeaturesFunctionalTests extends AbstractFunctionalTests {

    public static final String USER_UUID_FIELD = "uuid";

    @BeforeAll
    public void before() throws JSONException {
        if (!userFeatures.isUsernameExisting(UserJson.existing().getUsername()))
            userFeatures.registerUser(UserJson.existing().json(), 201);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_PasswordIsToShort() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().password("123").json(), 400);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_PasswordIsToLong() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().password(new String(new char[33]).replace('\0', ' ')).json(), 400);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_UsernameIsToShort() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().username("123").json(), 400);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_UsernameIsToLong() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().username(new String(new char[13]).replace('\0', ' ')).json(), 400);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_EmailIsToLong() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().email(new String(new char[65]).replace('\0', 'a') + "@email.com").json(), 400);
    }

    @Test
    public void register_Should_SuccessfullyRegisterNewUser_When_JsonIsValid() throws JSONException {
        var response = userFeatures.registerUser(UserJson.newValid().json(), 201);
        assertTrue(userFeatures.isUserUuidExisting(response.getString(USER_UUID_FIELD)));
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_UsernameIsExisting() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().username(EXISTING_USERNAME).json(), 400);
    }

    @Test
    public void register_Should_UnsuccessfullyRegisterNewUser_When_EmailIsExisting() throws JSONException {
        userFeatures.registerUser(UserJson.newValid().username(EXISTING_EMAIL).json(), 400);
    }

    @Test
    public void login_Should_Fail_When_UsernameIsNotGiven() {
        userFeatures.loginUser("", EXISTING_PASSWORD, 400);
    }

    @Test
    public void login_Should_Fail_When_PasswordIsNotGiven() {
        userFeatures.loginUser(EXISTING_USERNAME, "", 400);
    }

    @Test
    public void login_Should_SuccessfullyLogin_When_ValidCredentialsGiven() {
        var token = userFeatures.loginUser(EXISTING_USERNAME, EXISTING_PASSWORD, 200);
        userFeatures.validateToken(token);
    }

}
