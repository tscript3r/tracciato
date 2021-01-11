package pl.tscript3r.tracciato;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.tscript3r.tracciato.route.RouteFeatures;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.UserFeatures;
import pl.tscript3r.tracciato.user.UserJson;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static pl.tscript3r.tracciato.user.UserJson.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"development", "fakeDurationProvider", "errorController"})
public abstract class AbstractFunctionalTests {

    @Autowired
    public UserFacade userFacade;

    public String token;
    public UUID userUuid;
    public String secondUsersToken;
    public UUID secondUsersUuid;

    @Rule
    public WireMockRule contactsServiceMock = new WireMockRule(options().port(8777));

    @Autowired
    protected UserFeatures userFeatures;

    @Autowired
    protected RouteFeatures routeFeatures;

    public void registerUserAndLogin() throws JSONException {
        if (!userFeatures.isUsernameExisting(EXISTING_USERNAME))
            userFeatures.registerUser(UserJson.existing().json(), 201);
        token = userFeatures.loginUser(EXISTING_USERNAME, EXISTING_PASSWORD, 200);
        userUuid = userFacade.validateAndGetUuidFromToken(token).get();
    }

    public void registerAndLoginSecondUser() throws JSONException {
        if (!userFeatures.isUsernameExisting(NEW_USERNAME))
            userFeatures.registerUser(UserJson.newValid()
                    .username(NEW_USERNAME)
                    .password(NEW_PASSWORD)
                    .email(NEW_EMAIL)
                    .json(), 201);
        secondUsersToken = userFeatures.loginUser(NEW_USERNAME, NEW_PASSWORD, 200);
        secondUsersUuid = userFacade.validateAndGetUuidFromToken(secondUsersToken).get();
    }

}