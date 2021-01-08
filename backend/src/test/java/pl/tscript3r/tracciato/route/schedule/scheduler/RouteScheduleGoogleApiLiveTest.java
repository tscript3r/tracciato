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
import pl.tscript3r.tracciato.route.schedule.scheduled.RouteScheduledVariationEntity;

import java.util.UUID;
import java.util.concurrent.Executors;

import static pl.tscript3r.tracciato.infrastructure.EnvironmentConst.GOOGLE_API_KEY;

@Slf4j
@DisplayName("Route schedule live test (with Google API key)")
class RouteScheduleGoogleApiLiveTest {

    RouteDto validRoute;
    DurationProvider durationProvider;
    RoutePermutationsFactory routePermutationsFactory;
    RouteSimulationsSupplier routeSimulationsSupplier;

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
        routeSimulationsSupplier = new RouteSimulationsSupplier(UUID.randomUUID(),
                routePermutationsFactory.get(validRoute));
    }

    @Test
    void call() {
        var results = routeSimulationsSupplier.get();
        log.debug("\r\n\r\nMost accurate route");
        logOut(results.getTuned());
        log.debug("\r\n\r\nMost optimal route");
        logOut(results.getOptimal());
    }

    private void logOut(RouteScheduledVariationEntity scheduledVariation) {
        logOrder(scheduledVariation);
        logMissedAppointments(scheduledVariation);
        logTimeline(scheduledVariation);
    }

    private void logOrder(RouteScheduledVariationEntity scheduledVariation) {
        log.debug("\r\nTotal distance: {}km", scheduledVariation.getTravelledMeters() / 1000);
        log.debug("\r\nOrder:");
        log.debug("- {}", validRoute.getStartLocation().getCity());
        scheduledVariation.getOrder()
                .forEach(routeLocationDto -> log.debug("- {}", routeLocationDto.getLocation().getCity()));
        log.debug("- {}", validRoute.getEndLocation().getCity());
    }

    private void logMissedAppointments(RouteScheduledVariationEntity scheduledVariation) {
        if (scheduledVariation.getMissedAppointmentsCount() == 0)
            log.debug("\r\nNo appointments missed.");
        else {
            log.debug("\r\nMissed appointments:");
            scheduledVariation.getMissedAppointments().forEach(routeLocationDto -> {
                log.debug("- {}: {}", routeLocationDto.getLocation().getCity(), routeLocationDto.getAvailability().toString());
            });
        }
    }

    private void logTimeline(RouteScheduledVariationEntity scheduledVariation) {
        log.debug("\r\nTimeline:");
        scheduledVariation.getTimeline()
                .forEach(log::debug);
    }

}