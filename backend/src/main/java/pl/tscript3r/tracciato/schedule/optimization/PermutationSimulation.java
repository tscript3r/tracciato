package pl.tscript3r.tracciato.schedule.optimization;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.time.RouteTime;
import pl.tscript3r.tracciato.stop.StopDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ToString
public class PermutationSimulation {

    @Getter
    private final RouteDto routeDto;
    @Getter
    private final List<StopDto> orderedRoute;
    @Getter
    private final RouteTime routeTime;
    @Getter
    private final List<StopDto> missedAppointments = new ArrayList<>();
    private final Durations durations;
    @Getter
    private long travelledMeters = 0;

    private PermutationSimulation(RouteDto routeDto, List<StopDto> orderedRoute, Durations durations) {
        this.routeDto = routeDto;
        this.orderedRoute = orderedRoute;
        this.durations = durations;
        this.routeTime = new RouteTime(routeDto);
    }

    public static PermutationSimulation simulate(RouteDto routeDto,
                                                 List<StopDto> orderedRoute,
                                                 Durations durations) {
        assert orderedRoute.size() > 1;
        var routePermutation = new PermutationSimulation(routeDto, Collections.unmodifiableList(orderedRoute), durations);
        routePermutation.createResults();
        return routePermutation;
    }

    private void createResults() {
        startLocationToFirstOrderedLocation();
        iterateLocationsList();
        lastOrderedLocationToEndLocation();
    }

    private void startLocationToFirstOrderedLocation() {
        var startLocation = routeDto.getStartLocation();
        var firstOrderedLocation = orderedRoute.get(0);
        simulate(startLocation, firstOrderedLocation);
    }

    private void simulate(LocationDto from, StopDto destination) {
        travel(from, destination.getLocation());
        onsite(destination);
        stopAppointmentsCheck(destination);
    }

    private void travel(LocationDto from, LocationDto to) {
        var duration = durations.getDuration(from, to);
        travelledMeters += duration.getMeters();
        routeTime.travel(from, to, duration.getDuration());
    }

    private void onsite(StopDto stopDto) {
        routeTime.onsite(stopDto);
    }

    private void stopAppointmentsCheck(StopDto location) {
        var locationAvailabilities = location.getAvailability();
        if (!locationAvailabilities.isEmpty()) {
            var matchedAppointmentsCount = locationAvailabilities.stream()
                    .filter(a -> isAppointmentMatch(location, a))
                    .count();
            if (matchedAppointmentsCount > 0)
                routeTime.appointmentMatched(location.getLocation());
            else {
                missedAppointments.add(location);
                routeTime.appointmentMismatched(location.getLocation());
            }
        }
    }

    private boolean isAppointmentMatch(StopDto location, AvailabilityDto availability) {
        var currentDate = routeTime.getFutureNow();
        if (!availability.getDate().isEqual(currentDate.toLocalDate()))
            return false;
        var currentTime = currentDate.toLocalTime();
        var currentTimePlusOnsiteDuration = currentTime.plus(Duration.parse(location.getOnsideDuration()));
        return currentTime.isAfter(availability.getFrom()) &&
                currentTimePlusOnsiteDuration.isBefore(availability.getTo());
    }

    private void iterateLocationsList() {
        for (int i = 0; i < orderedRoute.size() - 1; i++) {
            var orderedFrom = orderedRoute.get(i);
            var orderedTo = orderedRoute.get(i + 1);
            simulate(orderedFrom.getLocation(), orderedTo);
        }
    }

    private void lastOrderedLocationToEndLocation() {
        var endLocation = routeDto.getEndLocation();
        var lastOrderedLocation = orderedRoute.get(orderedRoute.size() - 1).getLocation();
        travel(lastOrderedLocation, endLocation);
        routeTime.finish(endLocation);
    }

    public LocalDateTime getEndingDate() {
        return routeTime.getFutureNow();
    }

    public int getMissedAppointmentsCount() {
        return missedAppointments.size();
    }

}
