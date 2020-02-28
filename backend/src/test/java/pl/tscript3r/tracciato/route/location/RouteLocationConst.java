package pl.tscript3r.tracciato.route.location;

import org.mockito.internal.util.collections.Sets;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;
import static pl.tscript3r.tracciato.route.RouteConst.START_DATE;

public final class RouteLocationConst {

    public static final LocationPriority LOCATION_PRIORITY = LocationPriority.MEDIUM;
    public static final String LOCATION_DURATION = "PT1H";

    public static final String EXISTING_LOCATION_DURATION = "PT1H";
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

    public static RouteLocationDto getBerlinRouteLocationDto(UUID ownerUuid) {
        var results = getRouteLocationDto(ownerUuid, LocationConst.getBerlinLocationDto());
        var availability = new AvailabilityDto();
        availability.setDate(START_DATE.toLocalDate().plusDays(1));
        availability.setFrom(LocalTime.of(8, 0));
        availability.setTo(LocalTime.of(16, 0));
        results.getAvailability().add(availability);
        return results;
    }

    public static RouteLocationDto getRouteLocationDto(UUID ownerUuid, LocationDto locationDto) {
        var results = new RouteLocationDto();
        results.setLocation(locationDto);
        results.setPriority(LocationPriority.MEDIUM);
        results.setOnsideDuration(EXISTING_LOCATION_DURATION);
        results.setOwnerUuid(ownerUuid);
        return results;
    }

    public static RouteLocationDto getHamburgRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getHamburgLocationDto());
    }

    public static RouteLocationDto getEssenRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getEssenLocationDto());
    }

    public static RouteLocationDto getStuttgartRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getStuttgartLocationDto());
    }

    public static RouteLocationDto getBrunszwikRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getBrunszwikLocationDto());
    }

    public static RouteLocationDto getBremaRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getBremaLocationDto());
    }

    public static RouteLocationDto getGetyngaRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getGetyngaLocationDto());
    }

    public static RouteLocationDto getLuneburgRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getLuneburgLocationDto());
    }

    public static RouteLocationDto getWarsawRouteLocationDto(UUID ownerUuid) {
        return getRouteLocationDto(ownerUuid, LocationConst.getWarsawLocationDto());
    }

    public static Set<RouteLocationDto> getValidLocationsLists(UUID ownerUuid) {
        return Sets.newSet(getBerlinRouteLocationDto(ownerUuid), getHamburgRouteLocationDto(ownerUuid),
                getEssenRouteLocationDto(ownerUuid), getStuttgartRouteLocationDto(ownerUuid),
                getLuneburgRouteLocationDto(ownerUuid), getGetyngaRouteLocationDto(ownerUuid),
                getBremaRouteLocationDto(ownerUuid), getBrunszwikRouteLocationDto(ownerUuid));
    }

}
