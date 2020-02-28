package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ToString
public class RouteSolution {

    @Getter
    private final RouteDto routeDto;

    @Getter
    private final List<RouteLocationDto> orderedRoute;
    private final Durations durations;

    @Getter
    private final List<RouteLocationDto> missedAppointments = new ArrayList<>();

    @Getter
    private LocalDateTime futureNow;

    private RouteSolution(RouteDto routeDto, List<RouteLocationDto> orderedRoute, Durations durations) {
        this.routeDto = routeDto;
        this.orderedRoute = orderedRoute;
        this.durations = durations;
        this.futureNow = LocalDateTime.from(routeDto.getStartDate());
    }

    public static RouteSolution estimate(RouteDto routeDto,
                                         List<RouteLocationDto> orderedRoute,
                                         Durations durations) {
        assert orderedRoute.size() > 0;
        var routeSolution = new RouteSolution(routeDto, Collections.unmodifiableList(orderedRoute), durations);
        routeSolution.createResults();
        return routeSolution;
    }

    private void createResults() {
        startLocationToFirstOrderedLocation();
        all2AllLocations();
        lastOrderedLocationToEndLocation();
    }

    private void startLocationToFirstOrderedLocation() {
        var startLocation = routeDto.getStartLocation();
        var firstOrderedLocation = orderedRoute.get(0).getLocation();
        incrementFutureNowDuration(startLocation, firstOrderedLocation);
    }

    private void incrementFutureNowDuration(LocationDto from, LocationDto to) {
        var durationDto = durations.getDuration(from, to);
        incrementFutureNow(durationDto.getDuration());
    }

    private void incrementFutureNow(Duration duration) {
        if (willTravelerDoOvertime(duration)) {
            duration = travelIfAllowedUntilDayIsOver(duration);
            setNewDay(duration);
        } else
            futureNow = futureNow.plus(duration);
    }

    private boolean willTravelerDoOvertime(Duration plus) {
        var tmp = futureNow.plus(plus);
        return futureNow.getDayOfYear() != tmp.getDayOfYear() || tmp.toLocalTime().isAfter(currentDayMaxEndTime());
    }

    private LocalTime currentDayMaxEndTime() {
        // TODO refactor after adding RouteDto field default availability / work hours
        return getAvailability(futureNow.toLocalDate()).getTo();
    }

    private AvailabilityDto getAvailability(LocalDate day) {
        return routeDto.getAvailabilities()
                .stream()
                .filter(a -> a.getDate().isEqual(day))
                .findFirst()
                .orElse(routeDto.getAvailabilities().get(0)); // TODO tmp solution
    }

    private Duration travelIfAllowedUntilDayIsOver(Duration travelTime) {
        /* TODO RouteDto should have a field for allowing / disabling
                travelling without enough time to get to next location
                or some more advanced approach (limits, exceptions etc)
                So this is only a temporary solution */
        var maxDayTime = getAvailability(futureNow.toLocalDate()).getTo();
        var currentTime = futureNow.toLocalTime();
        var todayLeftTravelTime = Duration.between(currentTime, maxDayTime);
        return travelTime.minus(todayLeftTravelTime);
    }

    private void setNewDay(Duration plus) {
        futureNow = LocalDateTime.of(futureNow.toLocalDate().plusDays(1),
                getAvailability(futureNow.toLocalDate()).getFrom());
        futureNow = futureNow.plus(plus);
    }

    private void all2AllLocations() {
        for (int i = 0; i < orderedRoute.size() - 1; i++) {
            var orderedFrom = orderedRoute.get(i);
            var orderedTo = orderedRoute.get(i + 1);
            incrementFutureNowDuration(orderedFrom.getLocation(), orderedTo.getLocation());
            checkForAppointmentWindows(orderedTo);
            incrementFutureNowWithOnsiteDuration(orderedFrom);
            checkForAppointmentWindows(orderedTo);
        }
    }

    private void checkForAppointmentWindows(RouteLocationDto location) {
        if (!location.getAvailability().isEmpty())
            location.getAvailability()
                    .stream()
                    .filter(a -> a.getFrom().isBefore(futureNow.toLocalTime()) &&
                            a.getTo().isAfter(futureNow.toLocalTime()))
                    .findFirst()
                    .ifPresent(a -> missedAppointments.add(location));
    }

    private void incrementFutureNowWithOnsiteDuration(RouteLocationDto routeLocationDto) {
        var onsiteDuration = Duration.parse(routeLocationDto.getOnsideDuration());
        if (willTravelerDoOvertime(onsiteDuration))
            setNewDay(onsiteDuration);
        else
            futureNow = futureNow.plus(onsiteDuration);
    }

    private void lastOrderedLocationToEndLocation() {
        var endLocation = routeDto.getEndLocation();
        var lastOrderedLocation = orderedRoute.get(orderedRoute.size() - 1).getLocation();
        incrementFutureNowDuration(endLocation, lastOrderedLocation);
    }

}
