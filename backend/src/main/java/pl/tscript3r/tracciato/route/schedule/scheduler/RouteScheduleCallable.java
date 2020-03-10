package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
class RouteScheduleCallable implements Callable<RouteScheduleResults> {

    private final RouteDto routeDto;
    private final DurationProvider durationProvider;

    public RouteScheduleCallable(RouteDto routeDto, DurationProvider durationProvider) {
        this.routeDto = routeDto;
        this.durationProvider = durationProvider;
    }

    @Override
    public RouteScheduleResults call() {
        var durations = getDurations();
        var permutationsGroup = PermutationsGroupGenerator.generate(routeDto.getLocations())
                .collect(Collectors.toList());
        var processedRoute = new RoutePermutationsGroup(routeDto, durations, permutationsGroup);
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
