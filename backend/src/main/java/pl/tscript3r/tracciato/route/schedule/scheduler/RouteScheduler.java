package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.paukov.combinatorics3.Generator;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class RouteScheduler implements Callable<InternalResponse<ScheduledRoute>> {

    private final RouteDto routeDto;
    private final DurationProvider durationProvider;

    public RouteScheduler(RouteDto routeDto, DurationProvider durationProvider) {
        this.routeDto = routeDto;
        this.durationProvider = durationProvider;
    }

    @Override
    public InternalResponse<ScheduledRoute> call() {
        var durations = getDurations();
        var combinations = Generator.permutation(routeDto.getLocations())
                .simple()
                .stream()
                .collect(Collectors.toList());
        var processedRoute = new ProcessedRoute(routeDto, durations, combinations);
        processedRoute.createSolutions();
        return processedRoute.toScheduledRoute();
    }

    private Durations getDurations() {
        return Durations.get(durationProvider, getAllLocations());
    }

    private Set<LocationDto> getAllLocations() {
        var locations = routeDto.getLocations()
                .stream()
                .map(RouteLocationDto::getLocation)
                .collect(Collectors.toSet());
        locations.add(routeDto.getStartLocation());
        locations.add(routeDto.getEndLocation());
        return locations;
    }

}
