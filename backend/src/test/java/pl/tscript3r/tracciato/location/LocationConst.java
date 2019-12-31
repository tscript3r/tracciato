package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.location.api.LocationDto;

public final class LocationConst {

    public static final String LOCATION_CITY = "Berlin";
    public static final String EXISTING_LOCATION_CITY = "Warsaw";

    public static LocationDto getValidLocationDto() {
        var results = new LocationDto();
        results.setCity(LOCATION_CITY);
        return results;
    }

}
