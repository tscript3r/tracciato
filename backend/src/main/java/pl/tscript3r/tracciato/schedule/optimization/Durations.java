package pl.tscript3r.tracciato.schedule.optimization;

import io.vavr.Tuple3;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.duration.provider.DurationDto;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.duration.provider.LocationDto2StringLocation;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@ToString
@Slf4j
class Durations {

    private final Set<Tuple3<LocationDto, LocationDto, Future<DurationDto>>> durations = new HashSet<>();
    private final DurationProvider durationProvider;

    private Durations(DurationProvider durationProvider) {
        this.durationProvider = durationProvider;
    }

    public static Durations get(DurationProvider durationProvider, Collection<LocationDto> locations) {
        var instance = new Durations(durationProvider);
        locations.forEach(locationDto -> instance.getDurationsFor(locations, locationDto));
        log.debug("Requested {} durations between locations", instance.durations.size());
        return instance;
    }

    private void getDurationsFor(Collection<LocationDto> locations, LocationDto fromLocation) {
        locations.forEach(toLocation -> {
            if (!fromLocation.equals(toLocation) && !exists(fromLocation, toLocation))
                addDuration(fromLocation, toLocation);
        });
    }

    private boolean exists(LocationDto fromLocation, LocationDto toLocation) {
        for (Tuple3<LocationDto, LocationDto, Future<DurationDto>> tuple : durations)
            if ((tuple._1.equals(fromLocation) && tuple._2.equals(toLocation)) ||
                    tuple._1.equals(toLocation) && tuple._2.equals(fromLocation))
                return true;
        return false;
    }

    private void addDuration(LocationDto from, LocationDto to) {
        var fromStr = LocationDto2StringLocation.map(from);
        var toStr = LocationDto2StringLocation.map(to);
        var tuple = new Tuple3<>(from, to, durationProvider.getQuickestTravelDuration(fromStr, toStr));
        durations.add(tuple);
    }

    private DurationDto unwrap(Future<DurationDto> durationDtoFuture) {
        try {
            return durationDtoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TracciatoSchedulerException("Unable to get travel duration between locations", e);
        }
    }

    public DurationDto getDuration(LocationDto first, LocationDto second) {
        return unwrap(durations.stream()
                .filter(t -> (t._1.equals(first) && t._2.equals(second)) || (t._1.equals(second) && t._2.equals(first)))
                .findFirst()
                .orElseThrow(() -> new TracciatoSchedulerException(String.format("Duration between %s and %s not found", first, second)))
                ._3);
    }

}
