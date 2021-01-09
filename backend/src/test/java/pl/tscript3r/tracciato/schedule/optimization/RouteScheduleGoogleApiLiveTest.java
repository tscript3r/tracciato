package pl.tscript3r.tracciato.schedule.optimization;

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
    PermutationsFactory permutationsFactory;
    SimulationsSupplier simulationsSupplier;

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
        permutationsFactory = new PermutationsFactory(durationProvider);
        simulationsSupplier = new SimulationsSupplier(permutationsFactory.get(validRoute));
    }

    @Test
    void call() {
        var results = simulationsSupplier.get();
        log.debug("\r\n\r\nMost tuned route");
        logOut(results.getMostTunedRoute());
        log.debug("\r\n\r\nMost optimal route");
        logOut(results.getMostOptimalRoute());
    }

    private void logOut(PermutationSimulation permutationSimulation) {
        logOrder(permutationSimulation);
        logMissedAppointments(permutationSimulation);
        logTimeline(permutationSimulation);
    }

    private void logOrder(PermutationSimulation permutationSimulation) {
        log.debug("\r\nTotal distance: {}km", permutationSimulation.getTravelledMeters() / 1000);
        log.debug("\r\nOrder:");
        log.debug("- {}", validRoute.getStartLocation().getCity());
        permutationSimulation.getOrderedRoute()
                .forEach(routeLocationDto -> log.debug("- {}", routeLocationDto.getLocation().getCity()));
        log.debug("- {}", validRoute.getEndLocation().getCity());
    }

    private void logMissedAppointments(PermutationSimulation permutationSimulation) {
        if (permutationSimulation.getMissedAppointmentsCount() == 0)
            log.debug("\r\nNo appointments missed.");
        else {
            log.debug("\r\nMissed appointments:");
            permutationSimulation.getMissedAppointments().forEach(routeLocationDto -> {
                log.debug("- {}: {}", routeLocationDto.getLocation().getCity(), routeLocationDto.getAvailability().toString());
            });
        }
    }

    private void logTimeline(PermutationSimulation permutationSimulation) {
        log.debug("\r\nTimeline:");
        permutationSimulation.getRouteTime()
                .getRouteTimeline()
                .getEvents()
                .forEach(timelineEvent -> log.debug(timelineEvent.toString()));
    }

}