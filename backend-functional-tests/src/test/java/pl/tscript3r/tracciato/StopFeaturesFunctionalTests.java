package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.location.LocationFeatures;
import pl.tscript3r.tracciato.location.LocationJson;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.stop.StopFeatures;
import pl.tscript3r.tracciato.stop.StopJson;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Route location features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class StopFeaturesFunctionalTests extends AbstractFunctionalTests {

    UUID routeUuid;

    @Autowired
    StopFeatures stopFeatures;

    @Autowired
    LocationFeatures locationFeatures;

    @BeforeAll
    public void before() throws JSONException {
        registerUserAndLogin();
        routeUuid = UUID.fromString(routeFeatures.postRoute(token, RouteJson.newValid().json(), 201).getString("uuid"));
    }

    @Test
    void addStop_Should_RejectRequest_When_TokenHeaderIsMissing() throws JSONException {
        stopFeatures.addStop(null, routeUuid, StopJson.newValid().json(), 403);
    }

    @Test
    void addStop_Should_Fail_When_NoLocationDtoAndNoExistingLocationIdIsPassed() throws JSONException {
        var routeLocationJson = StopJson.newValid();
        routeLocationJson.setLocationDto(null);

        stopFeatures.addStop(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addStop_Should_Fail_When_InvalidLocationDtoIsPassed() throws JSONException {
        var routeLocationJson = StopJson.newValid();
        routeLocationJson.getLocationDto().setCity("");

        stopFeatures.addStop(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addStop_Should_Fail_When_ExistingLocationIdAndLocationDtoIsSet() throws JSONException {
        var routeLocationJson = StopJson.newValid();
        var uuid = UUID.randomUUID();

        routeLocationJson.setExistingLocationId(uuid);

        stopFeatures.addStop(token, routeUuid, routeLocationJson.json(), 400);
    }

    @Test
    void addStop_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        stopFeatures.addStop(token, routeUuid, StopJson.newValid().json(), 201);
    }


    @Test
    void addRouteStartLocation_Should_Fail_When_TokenHeaderIsMissing() throws JSONException {
        stopFeatures.setStartLocation(null, routeUuid, LocationJson.valid().json(), 403);
    }

    @Test
    void addRouteStartLocation_Should_Succeed_When_ExistingLocationUuidIsGiven() throws JSONException {
        // given
        var locationDto = LocationJson.valid();
        var addedLocation = locationFeatures.addLocation(token, locationDto.json(), 201);
        var addedLocationUuid = UUID.fromString(addedLocation.getString("uuid"));

        // when
        var result = stopFeatures.setRouteExistingStartLocation(token, routeUuid, addedLocationUuid, 200);

        // then
        assertNotNull(result.get("startLocation"));
    }

    @Test
    void addRouteStartLocation_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        var locationJson = LocationJson.valid();
        stopFeatures.setStartLocation(token, routeUuid, locationJson.json(), 201);
    }

    @Test
    void addRouteEndLocation_Should_Fail_When_TokenHeaderIsMissing() throws JSONException {
        stopFeatures.setRouteEndLocation(null, routeUuid, StopJson.newValid().json(), 403);
    }

    @Test
    void addRouteEndLocation_Should_Succeed_When_ValidRouteLocationJsonIsPassed() throws JSONException {
        var locationJson = LocationJson.valid();
        stopFeatures.setRouteEndLocation(token, routeUuid, locationJson.json(), 201);
    }


    @Test
    void addRouteEndLocation_Should_Succeed_When_ExistingLocationUuidIsGiven() throws JSONException {
        // given
        var locationDto = LocationJson.valid();
        var addedLocation = locationFeatures.addLocation(token, locationDto.json(), 201);
        var addedLocationUuid = UUID.fromString(addedLocation.getString("uuid"));

        // when
        var result = stopFeatures.setRouteExistingEndLocation(token, routeUuid, addedLocationUuid, 200);

        // then
        assertNotNull(result.get("endLocation"));
    }


}
