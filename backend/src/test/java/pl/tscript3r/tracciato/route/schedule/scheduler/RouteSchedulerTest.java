package pl.tscript3r.tracciato.route.schedule.scheduler;

import com.google.maps.GeoApiContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.duration.provider.google.GoogleMapsDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.LocationTimelineEvent;

import java.util.UUID;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.infrastructure.EnvironmentConst.GOOGLE_API_KEY;

@Slf4j
@DisplayName("Route scheduler")
class RouteSchedulerTest {

    RouteDto validRoute;
    DurationProvider durationProvider;
    RouteScheduler routeScheduler;

    @BeforeEach
    void setUp() {
        var apiKey = System.getenv(GOOGLE_API_KEY);
        Assume.assumeNotNull(apiKey, "No Google API key provided - tests skipped");
        validRoute = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        durationProvider = new GoogleMapsDurationProvider(Executors.newFixedThreadPool(5),
                new GeoApiContext.Builder()
                        .apiKey(apiKey)
                        .build()
        );
        routeScheduler = new RouteScheduler(validRoute, durationProvider);
    }

    @Test
    void call() {
        var results = routeScheduler.call();
        assertTrue(results.isRight());
        var routeSolution = results.get();
        log.debug("\r\n\r\nMost accurate route");
        routeSolution.getMostAccurateRoute()
                .ifPresent(this::logOut);
        log.debug("\r\n\r\nMost optimal route");
        routeSolution.getMostOptimalRoute()
                .ifPresent(this::logOut);
    }

    private void logOut(CombinationRoute combinationRoute) {
        logOrder(combinationRoute);
        logMissedAppointments(combinationRoute);
        logTimeline(combinationRoute);
    }

    private void logOrder(CombinationRoute combinationRoute) {
        log.debug("\r\nTotal distance: {}km", combinationRoute.getTravelledMeters() / 1000);
        log.debug("\r\nOrder:");
        log.debug("- {}", combinationRoute.getRouteDto().getStartLocation().getCity());
        combinationRoute.getOrderedRoute()
                .forEach(routeLocationDto -> log.debug("- {}", routeLocationDto.getLocation().getCity()));
        log.debug("- {}", combinationRoute.getRouteDto().getEndLocation().getCity());
    }

    private void logMissedAppointments(CombinationRoute combinationRoute) {
        if (combinationRoute.getMissedAppointmentsCount() == 0)
            log.debug("\r\nNo appointments missed.");
        else {
            log.debug("\r\nMissed appointments:");
            combinationRoute.getMissedAppointments().forEach(routeLocationDto -> {
                log.debug("- {}: {}", routeLocationDto.getLocation().getCity(), routeLocationDto.getAvailability().toString());
            });
        }
    }

    private void logTimeline(CombinationRoute combinationRoute) {
        log.debug("\r\nTimeline:");
        combinationRoute.getRouteTime().getRouteTimeline().getRouteEvents().forEach(timelineEvent -> {
            if (timelineEvent instanceof LocationTimelineEvent) {
                var locationTimelineEvent = (LocationTimelineEvent) timelineEvent;
                log.debug("- {} [{} - {}] - {}", locationTimelineEvent.getRouteEvent(),
                        locationTimelineEvent.getBeginning(),
                        locationTimelineEvent.getDuration(),
                        locationTimelineEvent.getLocationDto().getCity());
            } else
                log.debug("- {} [{}]", timelineEvent.getRouteEvent(), timelineEvent.getBeginning());
        });
    }

}