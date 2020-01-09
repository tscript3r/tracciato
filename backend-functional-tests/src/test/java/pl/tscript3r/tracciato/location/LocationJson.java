package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.AbstractJson;
import pl.tscript3r.tracciato.location.api.LocationDto;

public class LocationJson extends AbstractJson<LocationDto> {

    public static LocationJson valid() {
        return new LocationJson(LocationConst.getValidLocationDto());
    }

    public static LocationJson invalid() {
        var locationDto = LocationConst.getValidLocationDto();
        locationDto.setCity("");
        return new LocationJson(locationDto);
    }

    LocationJson(LocationDto object) {
        super(object);
    }

}
