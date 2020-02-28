package pl.tscript3r.tracciato.route.schedule.scheduler;

import com.google.maps.GeoApiContext;
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
        routeScheduler.call();
    }

}