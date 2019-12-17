package pl.tscript3r.tracciato.route.location;

import pl.tscript3r.tracciato.route.location.api.LocationDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

public class RouteLocationConst {

    public static final String LOCATION_CITY = "Berlin";
    public static final LocationPriority LOCATION_PRIORITY = LocationPriority.MEDIUM;
    public static final String LOCATION_DURATION = "02:00";

    public static LocationDto getValidLocationDto() {
        var results = new LocationDto();
        results.setCity(LOCATION_CITY);
        return results;
    }

    public static RouteLocationDto getValidRouteLocationDtoWithNewLocation() {
        var results = new RouteLocationDto();
        results.setPriority(LOCATION_PRIORITY);
        results.setOnsideDuration(LOCATION_DURATION);
        results.setLocation(getValidLocationDto());
        return results;
    }

    public static RouteLocationEntity getValidRouteLocationEntity() {
        return RouteLocationMapper.map(getValidRouteLocationDtoWithNewLocation());
    }

}
