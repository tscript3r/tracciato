package pl.tscript3r.tracciato.stop;

import pl.tscript3r.tracciato.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.LocalTime;
import java.util.*;

import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;
import static pl.tscript3r.tracciato.route.RouteConst.START_DATE;

public final class StopConst {

    public static final StopPriority LOCATION_PRIORITY = StopPriority.MEDIUM;
    public static final String LOCATION_DURATION = "PT1H";

    public static final String EXISTING_LOCATION_DURATION = "PT1H";
    public static final StopPriority EXISTING_LOCATION_PRIORITY = StopPriority.MEDIUM;

    public static StopDto getValidStopDtoWithNewLocation() {
        var results = new StopDto();
        results.setPriority(LOCATION_PRIORITY);
        results.setOnsideDuration(LOCATION_DURATION);
        results.setLocation(getValidLocationDto());
        return results;
    }

    public static StopDto getValidStopWithLocationUuid(UUID uuid) {
        var results = new StopDto();
        results.setPriority(EXISTING_LOCATION_PRIORITY);
        results.setOnsideDuration(EXISTING_LOCATION_DURATION);
        results.setExistingLocationUuid(uuid);
        return results;
    }

    public static StopEntity getValidStopEntity() {
        return StopMapper.map(getValidStopDtoWithNewLocation());
    }

    public static StopDto getBerlinStopDto(UUID ownerUuid) {
        var results = getStopDto(ownerUuid, LocationConst.getBerlinLocationDto());
        var availability = new AvailabilityDto();
        availability.setDate(START_DATE.toLocalDate().plusDays(3));
        availability.setFrom(LocalTime.of(8, 0));
        availability.setTo(LocalTime.of(16, 0));
        results.getAvailability().add(availability);
        return results;
    }

    public static StopDto getStopDto(UUID ownerUuid, LocationDto locationDto) {
        var results = new StopDto();
        results.setLocation(locationDto);
        results.setPriority(StopPriority.MEDIUM);
        results.setOnsideDuration(EXISTING_LOCATION_DURATION);
        results.setOwnerUuid(ownerUuid);
        return results;
    }

    public static StopDto getHamburgStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getHamburgLocationDto());
    }

    public static StopDto getEssenStopDto(UUID ownerUuid) {
        var results = getStopDto(ownerUuid, LocationConst.getEssenLocationDto());
        var availability = new AvailabilityDto();
        availability.setDate(START_DATE.toLocalDate().plusDays(4));
        availability.setFrom(LocalTime.of(8, 0));
        availability.setTo(LocalTime.of(16, 0));
        results.getAvailability().add(availability);
        return results;
    }

    public static StopDto getStuttgartStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getStuttgartLocationDto());
    }

    public static StopDto getBrunszwikStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getBrunszwikLocationDto());
    }

    public static StopDto getBremaStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getBremaLocationDto());
    }

    public static StopDto getGetyngaStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getGetyngaLocationDto());
    }

    public static StopDto getLuneburgStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getLuneburgLocationDto());
    }

    public static StopDto getWarsawStopDto(UUID ownerUuid) {
        return getStopDto(ownerUuid, LocationConst.getWarsawLocationDto());
    }

    public static List<StopDto> getValidLocationsList(UUID ownerUuid) {
        return Arrays.asList(getBerlinStopDto(ownerUuid), getHamburgStopDto(ownerUuid),
                getEssenStopDto(ownerUuid), getStuttgartStopDto(ownerUuid),
                getLuneburgStopDto(ownerUuid), getGetyngaStopDto(ownerUuid),
                getBremaStopDto(ownerUuid), getBrunszwikStopDto(ownerUuid));
    }

    public static Set<StopDto> getValidLocationsSet(UUID ownerUuid) {
        return new HashSet<>(getValidLocationsList(ownerUuid));
    }

}
