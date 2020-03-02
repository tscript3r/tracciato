package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.RouteTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ToString
class RouteSolution {

    @Getter
    private final RouteDto routeDto;

    @Getter
    private final List<RouteLocationDto> orderedRoute;
    private final Durations durations;

    @Getter
    private final List<RouteLocationDto> missedAppointments = new ArrayList<>();

    @Getter
    private LocalDateTime futureNow;

    @Getter
    private final RouteTime routeTime;

    private RouteSolution(RouteDto routeDto, List<RouteLocationDto> orderedRoute, Durations durations) {
        this.routeDto = routeDto;
        this.orderedRoute = orderedRoute;
        this.durations = durations;
        this.futureNow = LocalDateTime.from(routeDto.getStartDate());
        this.routeTime = new RouteTime(routeDto);
    }

    static RouteSolution estimate(RouteDto routeDto,
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
        var firstOrderedLocation = orderedRoute.get(0);
        addTravelEvent(startLocation, firstOrderedLocation.getLocation());
        addOnsiteEvent(firstOrderedLocation);
    }

    private void addTravelEvent(LocationDto from, LocationDto to) {
        routeTime.travel(to, durations.getDuration(from, to).getDuration());
    }

    private void addOnsiteEvent(RouteLocationDto routeLocationDto) {
        routeTime.onsite(routeLocationDto);
    }

    private void all2AllLocations() {
        for (int i = 0; i < orderedRoute.size() - 1; i++) {
            var orderedFrom = orderedRoute.get(i);
            var orderedTo = orderedRoute.get(i + 1);
            addTravelEvent(orderedFrom.getLocation(), orderedTo.getLocation());
            checkForAppointmentWindows(orderedTo);
            addOnsiteEvent(orderedTo);
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

    private void lastOrderedLocationToEndLocation() {
        var endLocation = routeDto.getEndLocation();
        var lastOrderedLocation = orderedRoute.get(orderedRoute.size() - 1).getLocation();
        addTravelEvent(lastOrderedLocation, endLocation);
        routeTime.finish();
    }

}
