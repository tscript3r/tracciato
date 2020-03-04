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

    private void logOut(RoutePermutation routePermutation) {
        logOrder(routePermutation);
        logMissedAppointments(routePermutation);
        logTimeline(routePermutation);
    }

    private void logOrder(RoutePermutation routePermutation) {
        log.debug("\r\nTotal distance: {}km", routePermutation.getTravelledMeters() / 1000);
        log.debug("\r\nOrder:");
        log.debug("- {}", routePermutation.getRouteDto().getStartLocation().getCity());
        routePermutation.getOrderedRoute()
                .forEach(routeLocationDto -> log.debug("- {}", routeLocationDto.getLocation().getCity()));
        log.debug("- {}", routePermutation.getRouteDto().getEndLocation().getCity());
    }

    private void logMissedAppointments(RoutePermutation routePermutation) {
        if (routePermutation.getMissedAppointmentsCount() == 0)
            log.debug("\r\nNo appointments missed.");
        else {
            log.debug("\r\nMissed appointments:");
            routePermutation.getMissedAppointments().forEach(routeLocationDto -> {
                log.debug("- {}: {}", routeLocationDto.getLocation().getCity(), routeLocationDto.getAvailability().toString());
            });
        }
    }

    private void logTimeline(RoutePermutation routePermutation) {
        log.debug("\r\nTimeline:");
        routePermutation.getRouteTime().getRouteTimeline().getEvents().forEach(timelineEvent -> {
            log.debug(timelineEvent.toString());
        });
    }

}