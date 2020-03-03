package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.RouteTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent.ONSITE_APPOINTMENTS_MATCHED;
import static pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent.ONSITE_APPOINTMENTS_MISMATCHED;

@Slf4j
@ToString
class CombinationRoute {

    @Getter
    private final RouteDto routeDto;

    @Getter
    private final List<RouteLocationDto> orderedRoute;
    private final Durations durations;

    @Getter
    private final List<RouteLocationDto> missedAppointments = new ArrayList<>();

    @Getter
    private final RouteTime routeTime;

    @Getter
    private long travelledMeters = 0;

    private CombinationRoute(RouteDto routeDto, List<RouteLocationDto> orderedRoute, Durations durations) {
        this.routeDto = routeDto;
        this.orderedRoute = orderedRoute;
        this.durations = durations;
        this.routeTime = new RouteTime(routeDto);
    }

    static CombinationRoute estimate(RouteDto routeDto,
                                     List<RouteLocationDto> orderedRoute,
                                     Durations durations) {
        assert orderedRoute.size() > 0;
        var routeSolution = new CombinationRoute(routeDto, Collections.unmodifiableList(orderedRoute), durations);
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
        var firstOrderedLocation = orderedRoute.get(0);
        addTravelEvent(startLocation, firstOrderedLocation.getLocation());
        addOnsiteEvent(firstOrderedLocation);
        checkForAppointmentWindows(firstOrderedLocation);
    }

    private void addTravelEvent(LocationDto from, LocationDto to) {
        var duration = durations.getDuration(from, to);
        travelledMeters += duration.getMeters();
        routeTime.travel(to, duration.getDuration());
    }

    private void addOnsiteEvent(RouteLocationDto routeLocationDto) {
        routeTime.onsite(routeLocationDto);
    }

    private void all2AllLocations() {
        for (int i = 0; i < orderedRoute.size() - 1; i++) {
            var orderedFrom = orderedRoute.get(i);
            var orderedTo = orderedRoute.get(i + 1);
            addTravelEvent(orderedFrom.getLocation(), orderedTo.getLocation());
            addOnsiteEvent(orderedTo);
            checkForAppointmentWindows(orderedTo);
        }
    }

    private void checkForAppointmentWindows(RouteLocationDto location) {
        var availabilities = location.getAvailability();
        if (!availabilities.isEmpty()) {
            var matchedAppointmentsCount = availabilities.stream()
                    .filter(a -> isAppointmentMatch(location, a))
                    .count();
            if (matchedAppointmentsCount > 0)
                routeTime.addEvent(ONSITE_APPOINTMENTS_MATCHED, location.getLocation());
            else {
                missedAppointments.add(location);
                routeTime.addEvent(ONSITE_APPOINTMENTS_MISMATCHED, location.getLocation());
            }
        }
    }

    private boolean isAppointmentMatch(RouteLocationDto location, AvailabilityDto availability) {
        var currentDate = routeTime.getFutureNow();
        if (!availability.getDate().isEqual(currentDate.toLocalDate()))
            return false;
        var currentTime = currentDate.toLocalTime();
        var currentTimePlusOnsiteDuration = currentTime.plus(Duration.parse(location.getOnsideDuration()));
        return currentTime.isAfter(availability.getFrom()) &&
                currentTimePlusOnsiteDuration.isBefore(availability.getTo());
    }

    private void lastOrderedLocationToEndLocation() {
        var endLocation = routeDto.getEndLocation();
        var lastOrderedLocation = orderedRoute.get(orderedRoute.size() - 1).getLocation();
        addTravelEvent(lastOrderedLocation, endLocation);
        routeTime.finish();
    }

    public LocalDateTime getEndingDate() {
        return routeTime.getFutureNow();
    }

    public int getMissedAppointmentsCount() {
        return missedAppointments.size();
    }

}
