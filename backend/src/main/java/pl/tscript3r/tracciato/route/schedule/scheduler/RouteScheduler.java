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
class RouteScheduler implements Callable<InternalResponse<RouteScheduleResults>> {

    private final RouteDto routeDto;
    private final DurationProvider durationProvider;

    public RouteScheduler(RouteDto routeDto, DurationProvider durationProvider) {
        this.routeDto = routeDto;
        this.durationProvider = durationProvider;
    }

    @Override
    public InternalResponse<RouteScheduleResults> call() {
        var durations = getDurations();
        var permutationsGroup = Generator.permutation(routeDto.getLocations())
                .simple()
                .stream()
                .collect(Collectors.toList());
        var processedRoute = new RoutePermutationGroup(routeDto, durations, permutationsGroup);
        return processedRoute.getResults();
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
