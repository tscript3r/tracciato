package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.route.RouteJson;
import pl.tscript3r.tracciato.schedule.ScheduleFeatures;
import pl.tscript3r.tracciato.scheduled.ScheduledFeatures;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Scheduled features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class ScheduledFeaturesFunctionalTests extends AbstractFunctionalTests {

    UUID routeUuid;

    @Autowired
    ScheduledFeatures scheduledFeatures;

    @Autowired
    ScheduleFeatures scheduleFeatures;

    @BeforeAll
    public void before() throws JSONException {
        registerUserAndLogin();
        registerAndLoginSecondUser();
        routeUuid = UUID.fromString(
                routeFeatures.postRoute(token, RouteJson.newValid().json(), 201).getString("uuid")
        );
    }

    @Test
    void getScheduled_Should_RejectRequest_When_TokenMissing() throws JSONException {
        scheduledFeatures.getScheduledJson(null, UUID.randomUUID(), 403);
    }

    @Test
    void getScheduled_Should_Return404_When_InvalidScheduledUuidPassed() throws JSONException {
        scheduledFeatures.getScheduledJson(token, UUID.randomUUID(), 404);
    }

    @Test
    void getScheduled_Should_Return401_When_UnauthorizedRequest() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        var scheduledJson = scheduleFeatures.schedule(token, routeUuid, true, 200);
        var requestUuid = UUID.fromString(scheduledJson.getString("uuid"));

        // when & then
        scheduledFeatures.getScheduledJson(secondUsersToken, requestUuid, 401);
    }

    @Test
    void getScheduled_Should_ReturnScheduledRoute_When_ValidTokenAndRequestUuidPassed() throws JSONException {
        // given
        var routeUuid = routeFeatures.createCompleteValidRoute(token);
        var scheduledJson = scheduleFeatures.schedule(token, routeUuid, true, 200);
        var requestUuid = UUID.fromString(scheduledJson.getString("uuid"));

        // when
        var result = scheduledFeatures.getScheduledJson(token, requestUuid, 200);

        // then
        assertNotNull(result.get("optimal"));
    }

}
