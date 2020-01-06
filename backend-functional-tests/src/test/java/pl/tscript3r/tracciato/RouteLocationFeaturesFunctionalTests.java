package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.location.LocationFeatures;
import pl.tscript3r.tracciato.location.LocationJson;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.route.location.RouteLocationFeatures;
import pl.tscript3r.tracciato.route.location.RouteLocationJson;
import pl.tscript3r.tracciato.user.UserJson;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_PASSWORD;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_USERNAME;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Route location features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class RouteLocationFeaturesFunctionalTests extends AbstractFunctionalTests {

    String token;
    UUID routeUuid;

    @Autowired
    RouteLocationFeatures routeLocationFeatures;

    @Autowired
    LocationFeatures locationFeatures;

    @BeforeAll
    public void before() throws JSONException {
        if (!userFeatures.isUsernameExisting(EXISTING_USERNAME))
            userFeatures.registerUser(UserJson.existing().json(), 201);
        token = userFeatures.loginUser(EXISTING_USERNAME, EXISTING_PASSWORD, 200);
        routeUuid = UUID.fromString(routeFeatures.addRoute(token, RouteJson.newValid().json(), 201).getString("uuid"));
    }

    @Test
    void addRouteLocation_Should_RejectRequest_When_TokenHeaderIsMissing() throws JSONException {
        routeLocationFeatures.addRouteLocation(null, routeUuid, RouteLocationJson.newValid().json(), 403);
    }

    @Test
    void addRouteLocation_Should_Fail_When_NoLocationDtoAndNoExistingLocationIdIsPassed() throws JSONException {
        var routeLocationJson = RouteLocationJson.newValid();
        routeLocationJson.setLocationDto(null);

        routeLocationFeatures.addRouteLocation(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addRouteLocation_Should_Fail_When_InvalidLocationDtoIsPassed() throws JSONException {
        var routeLocationJson = RouteLocationJson.newValid();
        routeLocationJson.getLocationDto().setCity("");

        routeLocationFeatures.addRouteLocation(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addRouteLocation_Should_Fail_When_ExistingLocationIdAndLocationDtoIsSet() throws JSONException {
        var routeLocationJson = RouteLocationJson.newValid();
        var uuid = UUID.randomUUID();

        routeLocationJson.setExistingLocationId(uuid);

        routeLocationFeatures.addRouteLocation(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addRouteLocation_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        routeLocationFeatures.addRouteLocation(token, routeUuid, RouteLocationJson.newValid().json(), 201);
    }


    @Test
    void addRouteStartLocation_Should_Fail_When_TokenHeaderIsMissing() throws JSONException {
        routeLocationFeatures.setRouteStartLocation(null, routeUuid, LocationJson.valid().json(), 403);
    }

    @Test
    void addRouteStartLocation_Should_Succeed_When_ExistingLocationUuidIsGiven() throws JSONException {
        // given
        var locationDto = LocationJson.valid();
        var addedLocation = locationFeatures.addLocation(token, locationDto.json(), 201);
        var addedLocationUuid = UUID.fromString(addedLocation.getString("uuid"));

        // when
        var result = routeLocationFeatures.setRouteExistingStartLocation(token, routeUuid, addedLocationUuid, 200);

        // then
        assertNotNull(result.get("startLocation"));
    }

    @Test
    void addRouteStartLocation_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        var locationJson = LocationJson.valid();
        routeLocationFeatures.setRouteStartLocation(token, routeUuid, locationJson.json(), 201);
    }

    @Test
    void addRouteEndLocation_Should_Fail_When_TokenHeaderIsMissing() throws JSONException {
        routeLocationFeatures.setRouteEndLocation(null, routeUuid, RouteLocationJson.newValid().json(), 403);
    }

    @Test
    void addRouteEndLocation_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        var locationJson = LocationJson.valid();
        routeLocationFeatures.setRouteEndLocation(token, routeUuid, locationJson.json(), 201);
    }


    @Test
    void addRouteEndLocation_Should_Succeed_When_ExistingLocationUuidIsGiven() throws JSONException {
        // given
        var locationDto = LocationJson.valid();
        var addedLocation = locationFeatures.addLocation(token, locationDto.json(), 201);
        var addedLocationUuid = UUID.fromString(addedLocation.getString("uuid"));

        // when
        var result = routeLocationFeatures.setRouteExistingEndLocation(token, routeUuid, addedLocationUuid, 200);

        // then
        assertNotNull(result.get("endLocation"));
    }


}
