package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Route features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class RouteFeaturesFunctionalTests extends AbstractFunctionalTests {

    @BeforeAll
    public void before() throws JSONException {
        registerUserAndLogin();
    }

    @Test
    void addRoute_Should_RejectRequest_When_TokenHeaderIsMissing() throws JSONException {
        routeFeatures.postRoute(null, RouteJson.newValid().json(), 403);
    }

    @Test
    void addRoute_Should_Fail_When_RouteNameIsToShort() throws JSONException {
        routeFeatures.postRoute(token, RouteJson.newValid().name("12").json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_RouteNameIsToLong() throws JSONException {
        routeFeatures.postRoute(token, RouteJson.newValid().name(new String(new char[256]).replace('\0', ' ')).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_StartDateIsNotSet() throws JSONException {
        routeFeatures.postRoute(token, RouteJson.newValid().startDate(null).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_StartDateIsBeforeCurrentDate() throws JSONException {
        routeFeatures.postRoute(token, RouteJson.newValid().startDate(LocalDateTime.now().minusDays(1)).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_MaxEndDateIsNotSet() throws JSONException {
        routeFeatures.postRoute(token, RouteJson.newValid().maxEndDate(null).json(), 400);
    }

    @Test
    void addRoute_Should_Fail_When_MaxEndDateIsBeforeStartDateCurrentDate() throws JSONException {
        var routeJson = RouteJson.newValid();
        routeJson.maxEndDate(routeJson.getStartDate().minusDays(1));
        routeFeatures.postRoute(token, routeJson.json(), 400);
    }

    @Test
    void addRoute_Should_SuccessfullySaveNewRoute_When_ValidJsonIsPassed() throws JSONException {
        var uuid = UUID.fromString(routeFeatures.postRoute(token, RouteJson.newValid().json(), 201).getString("uuid"));
        assertTrue(routeFeatures.isRouteUuidExisting(uuid));
    }

    @Test
    void getRoute_Should_Fail_When_TokenIsMissing() throws JSONException {
        routeFeatures.getRoute(null, UUID.randomUUID(), 403);
    }

    @Test
    void getRoute_Should_Fail_When_NonExistingRouteUuidIsPassed() throws JSONException {
        routeFeatures.getRoute(token, UUID.randomUUID(), 404);
    }

    @Test
    void getRoute_Should_SuccessfullyReturnRoute_When_ValidTokenAndExistingRouteUuidIsPassed() throws JSONException {
        var uuid = UUID.fromString(routeFeatures.postRoute(token, RouteJson.newValid().json(), 201).getString("uuid"));
        routeFeatures.getRoute(token, uuid, 200);
    }

}
