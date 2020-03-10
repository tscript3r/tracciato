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

import static pl.tscript3r.tracciato.infrastructure.EnvironmentConst.GOOGLE_API_KEY;

@Slf4j
@DisplayName("Route schedule live test (with Google API key)")
class RouteScheduleGoogleApiLiveTest {

    RouteDto validRoute;
    DurationProvider durationProvider;
    RoutePermutationsFactory routePermutationsFactory;
    RouteSimulationsCallable routeSimulationsCallable;

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
        routePermutationsFactory = new RoutePermutationsFactory(durationProvider);
        routeSimulationsCallable = new RouteSimulationsCallable(routePermutationsFactory.get(validRoute));
    }

    @Test
    void call() {
        var results = routeSimulationsCallable.call();
        log.debug("\r\n\r\nMost accurate route");
        logOut(results.getMostAccurateRoute());
        log.debug("\r\n\r\nMost optimal route");
        logOut(results.getMostOptimalRoute());
    }

    private void logOut(RoutePermutationSimulation routePermutationSimulation) {
        logOrder(routePermutationSimulation);
        logMissedAppointments(routePermutationSimulation);
        logTimeline(routePermutationSimulation);
    }

    private void logOrder(RoutePermutationSimulation routePermutationSimulation) {
        log.debug("\r\nTotal distance: {}km", routePermutationSimulation.getTravelledMeters() / 1000);
        log.debug("\r\nOrder:");
        log.debug("- {}", routePermutationSimulation.getRouteDto().getStartLocation().getCity());
        routePermutationSimulation.getOrderedRoute()
                .forEach(routeLocationDto -> log.debug("- {}", routeLocationDto.getLocation().getCity()));
        log.debug("- {}", routePermutationSimulation.getRouteDto().getEndLocation().getCity());
    }

    private void logMissedAppointments(RoutePermutationSimulation routePermutationSimulation) {
        if (routePermutationSimulation.getMissedAppointmentsCount() == 0)
            log.debug("\r\nNo appointments missed.");
        else {
            log.debug("\r\nMissed appointments:");
            routePermutationSimulation.getMissedAppointments().forEach(routeLocationDto -> {
                log.debug("- {}: {}", routeLocationDto.getLocation().getCity(), routeLocationDto.getAvailability().toString());
            });
        }
    }

    private void logTimeline(RoutePermutationSimulation routePermutationSimulation) {
        log.debug("\r\nTimeline:");
        routePermutationSimulation.getRouteTime().getRouteTimeline().getEvents().forEach(timelineEvent -> {
            log.debug(timelineEvent.toString());
        });
    }

}