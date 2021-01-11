package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.schedule.ScheduleFeatures;
import pl.tscript3r.tracciato.stop.StopFeatures;
import pl.tscript3r.tracciato.stop.StopJson;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Schedule features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class ScheduleFeaturesFunctionalTests extends AbstractFunctionalTests {

    @Autowired
    ScheduleFeatures scheduleFeatures;

    @Autowired
    StopFeatures stopFeatures;

    @BeforeAll
    public void before() throws JSONException {
        registerUserAndLogin();
        registerAndLoginSecondUser();
    }

    @Test
    void validateRoute_Should_RejectRequest_When_TokenMissing() throws JSONException {
        scheduleFeatures.validate(null, UUID.randomUUID(), 403);
    }

    @Test
    void validateRoute_Should_Return404_When_RouteUuidNotExisting() throws JSONException {
        scheduleFeatures.validate(token, UUID.randomUUID(), 404);
    }

    @Test
    void validateRoute_Should_Return400_When_ValidatedRouteIsPlainNew() throws JSONException {
        var uuid = UUID.fromString(
                routeFeatures.postRoute(token, RouteJson.newValid().json(), 201).getString("uuid")
        );
        scheduleFeatures.validate(token, uuid, 400);
    }

    @Test
    void validateRoute_Should_Return401_When_UnauthorizedRequest() throws JSONException {
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        scheduleFeatures.validate(secondUsersToken, routeUuid, 401);
    }

    @Test
    void validateRoute_Should_Return200_When_ValidatedRouteIsReadyToSchedule() throws JSONException {
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        scheduleFeatures.validate(token, routeUuid, 200);
    }

    @Test
    void scheduleRoute_Should_RejectRequest_When_TokenMissing() throws JSONException {
        scheduleFeatures.schedule(null, UUID.randomUUID(), true, 403);
    }

    @Test
    void scheduleRoute_Should_Return404_When_RouteUuidDoesNotExists() throws JSONException {
        scheduleFeatures.schedule(token, UUID.randomUUID(), true, 404);
    }

    @Test
    void scheduleRoute_Should_Return401_When_UnauthorizedRequest() throws JSONException {
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        scheduleFeatures.schedule(secondUsersToken, routeUuid, true, 401);
    }

    @Test
    void scheduleRoute_Should_ScheduleValidRouteSync_When_CalledWithSyncParam() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);

        // when
        var json = scheduleFeatures.schedule(token, routeUuid, true, 200);

        // then
        assertNotNull(json.get("optimal"));
    }

    @Test
    void scheduleRoute_Should_ScheduleValidRouteSync_When_CalledWithAsyncParam() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);

        // when
        var json = scheduleFeatures.schedule(token, routeUuid, false, 200);

        // then
        assertNotNull(json.get("requestUuid"));
    }

    @Test
    void scheduleRoute_Should_ScheduleValidRouteSync_When_CalledWithoutWaitParam() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);

        // when
        var json = scheduleFeatures.schedule(token, routeUuid, null, 200);

        // then
        assertNotNull(json.get("optimal"));
    }

    @Test
    void scheduleRoute_Should_Return400_When_RouteInCurrentVersionHasBeenAlreadyScheduled() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);

        // when & then
        scheduleFeatures.schedule(token, routeUuid, true, 200);
        scheduleFeatures.schedule(token, routeUuid, true, 400);
    }

    @Test
    void scheduleRoute_Should_ScheduleRoute_When_RouteAfterBeingScheduledHasBeenUpdated() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        scheduleFeatures.schedule(token, routeUuid, true, 200);
        stopFeatures.addStop(token, routeUuid, StopJson.newValid().json(), 201);

        // when & then
        scheduleFeatures.schedule(token, routeUuid, true, 200);
    }

}
