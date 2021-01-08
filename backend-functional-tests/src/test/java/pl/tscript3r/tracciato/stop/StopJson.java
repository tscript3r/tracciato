package pl.tscript3r.tracciato.stop;

import pl.tscript3r.tracciato.AbstractJson;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.UUID;

public class StopJson extends AbstractJson<StopDto> {

    private StopJson(StopDto object) {
        super(object);
    }

    public static StopJson newValid() {
        return new StopJson(StopConst.getValidStopDtoWithNewLocation());
    }

    public LocationDto getLocationDto() {
        return object.getLocation();
    }

    public StopJson setLocationDto(LocationDto locationDto) {
        object.setLocation(locationDto);
        return this;
    }

    public StopJson setExistingLocationId(UUID uuid) {
        object.setExistingLocationUuid(uuid);
        return this;
    }

}
