package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
class RoutePermutationsFactory {

    private final DurationProvider durationProvider;

    public RoutePermutationsGroup get(RouteDto routeDto) {
        var durations = getDurations(routeDto);
        var permutationsGroup = PermutationsGroupGenerator.generate(routeDto.getLocations())
                .collect(Collectors.toList());
        return new RoutePermutationsGroup(routeDto, durations, permutationsGroup);
    }

    private Durations getDurations(RouteDto routeDto) {
        return Durations.get(durationProvider, getAllLocations(routeDto));
    }

    private Set<LocationDto> getAllLocations(RouteDto routeDto) {
        var locations = routeDto.getLocations()
                .stream()
                .map(RouteLocationDto::getLocation)
                .collect(Collectors.toSet());
        locations.add(routeDto.getStartLocation());
        locations.add(routeDto.getEndLocation());
        return locations;
    }

}
