package pl.tscript3r.tracciato.duration;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.duration.provider.DurationDto;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.duration.provider.LocationDto2StringLocation;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.concurrent.Future;

@RequiredArgsConstructor
public class DurationFacade {

    private final DurationProvider durationProvider;

    public Future<DurationDto> getQuickestDuration(LocationDto fromLocation, LocationDto toLocation) {
        String from = LocationDto2StringLocation.map(fromLocation);
        String to = LocationDto2StringLocation.map(toLocation);
        return durationProvider.getQuickestTravelDuration(from, to);
    }

}
