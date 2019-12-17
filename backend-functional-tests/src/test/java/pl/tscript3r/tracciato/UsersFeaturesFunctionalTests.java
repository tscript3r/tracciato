package pl.tscript3r.tracciato;

import org.junit.jupiter.api.Test;

public class UsersFeaturesFunctionalTests extends BaseFunctionalTests {

    private final String userRegisterJson = readFile("json/user-register.json");

    @Test
    public void userRegister_Should_SuccessfullyRegisterNewUser_WhenUserDtoIsValid() {
        userFeatures.registerUser(userRegisterJson);
    }

}
