package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.RouteLocationConst;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public final class RouteConst {

    private static final Calendar CALENDAR = Calendar.getInstance();

    public static final LocalDateTime START_DATE = LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1), LocalTime.of(8, 0));
    public static final String ROUTE_NAME = "CW52";
    public static final int START_DATE_YEAR = START_DATE.getYear();
    public static final int START_DATE_MONTH = START_DATE.getDayOfMonth();
    public static final int START_DATE_DAY = START_DATE.getDayOfMonth();
    public static final int START_DATE_HOUR = 8;
    public static final int START_DATE_MINUTE = 0;

    public static final LocalDateTime MAX_END_DATE = START_DATE.plusDays(5).plusHours(8);

    public static final int MAX_END_DATE_YEAR = MAX_END_DATE.getYear();
    public static final int MAX_END_DATE_MONTH = MAX_END_DATE.getDayOfMonth();
    public static final int MAX_END_DATE_DAY = MAX_END_DATE.getDayOfMonth();
    public static final int MAX_END_DATE_HOUR = MAX_END_DATE.getHour();
    public static final int MAX_END_DATE_MINUTE = MAX_END_DATE.getMinute();

    public static final TrafficPrediction TRAFFIC_PREDICTION = TrafficPrediction.BEST_GUESS;

    public static NewRouteDto getValidNewRouteDto() {
        var newRouteDto = new NewRouteDto();
        newRouteDto.setStartDate(START_DATE);
        newRouteDto.setMaxEndDate(MAX_END_DATE);
        newRouteDto.setName(ROUTE_NAME);
        newRouteDto.setTraffic(TRAFFIC_PREDICTION);

        return newRouteDto;
    }

    public static RouteDto getValidRouteDto(UUID ownerUuid, UUID routeUuid) {
        var routeDto = new RouteDto();
        routeDto.setStartDate(START_DATE);
        routeDto.setMaxEndDate(MAX_END_DATE);
        routeDto.setName(ROUTE_NAME);
        routeDto.setUuid(UUID.randomUUID());
        routeDto.setTrafficPrediction(TrafficPrediction.BEST_GUESS);
        routeDto.setCreationTimestamp(LocalDateTime.now());
        routeDto.setOwnerUuid(ownerUuid);
        routeDto.setUuid(routeUuid);
        routeDto.setLocations(RouteLocationConst.getValidLocationsLists(ownerUuid));
        routeDto.setStartLocation(LocationConst.getStartLocationDto());
        routeDto.setEndLocation(LocationConst.getEndLocationDto());
        routeDto.setAvailabilities(getAvailabilities());
        return routeDto;
    }

    private static List<AvailabilityDto> getAvailabilities() {
        List<AvailabilityDto> results = new ArrayList<>();
        results.add(getAvailability(START_DATE.toLocalDate()));
        results.add(getAvailability(START_DATE.plusDays(1).toLocalDate()));
        var excludedAvailability = getAvailability(START_DATE.plusDays(2).toLocalDate());
        excludedAvailability.setExcluded(true);
        results.add(excludedAvailability);
        results.add(getAvailability(START_DATE.plusDays(3).toLocalDate()));
        results.add(getAvailability(START_DATE.plusDays(4).toLocalDate()));
        results.add(getAvailability(START_DATE.plusDays(5).toLocalDate()));
        return results;
    }

    private static AvailabilityDto getAvailability(LocalDate day) {
        var results = new AvailabilityDto();
        results.setDate(day);
        results.setFrom(LocalTime.of(8, 0));
        results.setTo(LocalTime.of(18, 0));
        return results;
    }

    private RouteConst() {
    }

}
