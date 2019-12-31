package pl.tscript3r.tracciato.route.location;

import pl.tscript3r.tracciato.AbstractJson;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;

public class RouteLocationJson extends AbstractJson<RouteLocationDto> {

    public static RouteLocationJson newValid() {
        return new RouteLocationJson(RouteLocationConst.getValidRouteLocationDtoWithNewLocation());
    }

    private RouteLocationJson(RouteLocationDto object) {
        super(object);
    }

    public LocationDto getLocationDto() {
        return object.getLocation();
    }

    public RouteLocationJson setLocationDto(LocationDto locationDto) {
        object.setLocation(locationDto);
        return this;
    }

    public RouteLocationJson setExistingLocationId(UUID uuid) {
        object.setExistingLocationUuid(uuid);
        return this;
    }

}
