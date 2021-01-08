package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopDto;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
class PermutationsFactory {

    private final DurationProvider durationProvider;

    public PermutationsGroup get(RouteDto routeDto) {
        var durations = getDurations(routeDto);
        var permutationsGroup = PermutationsGroupGenerator.generate(routeDto.getStops())
                .collect(Collectors.toList());
        return new PermutationsGroup(routeDto, durations, permutationsGroup);
    }

    private Durations getDurations(RouteDto routeDto) {
        return Durations.get(durationProvider, getAllLocations(routeDto));
    }

    private Set<LocationDto> getAllLocations(RouteDto routeDto) {
        var locations = routeDto.getStops()
                .stream()
                .map(StopDto::getLocation)
                .collect(Collectors.toSet());
        locations.add(routeDto.getStartLocation());
        locations.add(routeDto.getEndLocation());
        return locations;
    }

}
