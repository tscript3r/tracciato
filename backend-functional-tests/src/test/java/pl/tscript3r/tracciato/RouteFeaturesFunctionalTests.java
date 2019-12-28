package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.user.UserJson;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_PASSWORD;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_USERNAME;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Route features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class RouteFeaturesFunctionalTests extends AbstractFunctionalTests {

    String token;

    @BeforeAll
    public void before() throws JSONException {
        if (!userFeatures.isUsernameExisting(EXISTING_USERNAME))
            userFeatures.registerUser(UserJson.existing().json(), 201);
        token = userFeatures.loginUser(EXISTING_USERNAME, EXISTING_PASSWORD, 200);
    }

    @Test
    void addRoute_Should_Fail_When_RouteNameIsToShort() throws JSONException {
        routeFeatures.addRoute(token, RouteJson.newValid().name("12").json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_RouteNameIsToLong() throws JSONException {
        routeFeatures.addRoute(token, RouteJson.newValid().name(new String(new char[256]).replace('\0', ' ')).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_StartDateIsNotSet() throws JSONException {
        routeFeatures.addRoute(token, RouteJson.newValid().startDate(null).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_StartDateIsBeforeCurrentDate() throws JSONException {
        routeFeatures.addRoute(token, RouteJson.newValid().startDate(LocalDateTime.now().minusDays(1)).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_MaxEndDateIsNotSet() throws JSONException {
        routeFeatures.addRoute(token, RouteJson.newValid().maxEndDate(null).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_MaxEndDateIsBeforeStartDateCurrentDate() throws JSONException {
        var routeJson = RouteJson.newValid();
        routeJson.maxEndDate(routeJson.getStartDate().minusDays(1));
        routeFeatures.addRoute(token, routeJson.json(), 400);
    }

    @Test
    void addRoute_Should_SuccessfullySaveNewRoute_When_ValidJsonIsPassed() throws JSONException {
        var uuid = UUID.fromString(routeFeatures.addRoute(token, RouteJson.newValid().json(), 201).getString("uuid"));
        assertTrue(routeFeatures.isRouteUuidExisting(uuid));
    }

}
