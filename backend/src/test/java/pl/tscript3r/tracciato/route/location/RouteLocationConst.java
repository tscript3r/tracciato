package pl.tscript3r.tracciato.route.location;

import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;

import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;

public final class RouteLocationConst {

    public static final LocationPriority LOCATION_PRIORITY = LocationPriority.MEDIUM;
    public static final String LOCATION_DURATION = "02:00";

    public static final String EXISTING_LOCATION_DURATION = "03:00";
    public static final LocationPriority EXISTING_LOCATION_PRIORITY = LocationPriority.MEDIUM;

    public static RouteLocationDto getValidRouteLocationDtoWithNewLocation() {
        var results = new RouteLocationDto();
        results.setPriority(LOCATION_PRIORITY);
        results.setOnsideDuration(LOCATION_DURATION);
        results.setLocation(getValidLocationDto());
        return results;
    }

    public static RouteLocationDto getValidRouteLocationWithLocationUuid(UUID uuid) {
        var results = new RouteLocationDto();
        results.setPriority(EXISTING_LOCATION_PRIORITY);
        results.setOnsideDuration(EXISTING_LOCATION_DURATION);
        results.setExistingLocationUuid(uuid);
        return results;
    }

    public static RouteLocationEntity getValidRouteLocationEntity() {
        return RouteLocationMapper.map(getValidRouteLocationDtoWithNewLocation());
    }

}
