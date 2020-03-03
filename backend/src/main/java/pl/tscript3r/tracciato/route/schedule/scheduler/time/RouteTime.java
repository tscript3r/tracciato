package pl.tscript3r.tracciato.route.schedule.scheduler.time;

import lombok.Getter;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.RouteTimeline;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent.*;

public class RouteTime {

    private final RouteDto routeDto;

    @Getter
    private final RouteTimeline routeTimeline;

    @Getter
    private LocalDateTime futureNow;

    public RouteTime(RouteDto routeDto) {
        this.routeDto = routeDto;
        futureNow = routeDto.getStartDate();
        routeTimeline = new RouteTimeline();
        routeTimeline.addEvent(BEGINNING, futureNow);
    }

    public void travel(LocationDto destination, Duration travelDuration) {
        routeTimeline.addLocationEvent(TRAVEL_START, futureNow, travelDuration, destination);
        incrementFutureNow(travelDuration);
        routeTimeline.addLocationEvent(TRAVEL_ARRIVAL, futureNow, travelDuration, destination);
    }

    public void onsite(RouteLocationDto routeLocationDto) {
        var onsiteDuration = Duration.parse(routeLocationDto.getOnsideDuration());
        if (willTravelerDoOvertime(onsiteDuration)) {
            setNewDay(onsiteDuration, false);
        } else {
            futureNow = futureNow.plus(onsiteDuration);
            routeTimeline.addLocationEvent(ONSITE, futureNow, onsiteDuration, routeLocationDto.getLocation());
        }
    }

    private void incrementFutureNow(Duration duration) {
        if (willTravelerDoOvertime(duration)) {
            duration = subtractTodaysTimeLeft(duration);
            setNewDay(duration, true);
        } else
            futureNow = futureNow.plus(duration);
    }

    private boolean willTravelerDoOvertime(Duration duration) {
        var incrementedFutureNow = futureNow.plus(duration);
        return futureNow.getDayOfYear() != incrementedFutureNow.getDayOfYear() ||
                incrementedFutureNow.toLocalTime().isAfter(currentDayMaxEndTime());
    }

    private LocalTime currentDayMaxEndTime() {
        // TODO refactor after adding RouteDto field default availability / work hours
        return getAvailability(futureNow.toLocalDate()).getTo().plus(Duration.ofHours(1));
    }

    private AvailabilityDto getAvailability(LocalDate day) {
        return routeDto.getAvailabilities()
                .stream()
                .filter(a -> a.getDate().isEqual(day))
                .findFirst()
                .orElse(routeDto.getAvailabilities().get(0)); // TODO tmp solution
    }

    private Duration subtractTodaysTimeLeft(Duration travelTime) {
        /* TODO RouteDto should have a field for allowing / disabling
                travelling without enough time to get to next location
                or some more advanced approach (limits, exceptions etc)
                So this is only a temporary solution */
        var maxDayTime = getAvailability(futureNow.toLocalDate()).getTo();
        var currentTime = futureNow.toLocalTime();
        var todayLeftTravelTime = Duration.between(currentTime, maxDayTime);
        return travelTime.minus(todayLeftTravelTime);
    }

    private void setNewDay(Duration plus, boolean travelInterrupted) {
        addNewDay2Timeline(travelInterrupted);
        futureNow = getNextAvailableDate();
        routeTimeline.addEvent(DAY_START, futureNow);
        if (travelInterrupted)
            routeTimeline.addEvent(TRAVEL_CONTINUE, futureNow);
        futureNow = futureNow.plus(plus);
        checkMaxRouteEndingIsPassed();
    }

    private void checkMaxRouteEndingIsPassed() {
        if (futureNow.isAfter(routeDto.getMaxEndDate()))
            routeTimeline.addEvent(ROUTE_MAX_ENDING_PASSED, futureNow);
    }

    private void addNewDay2Timeline(boolean travelInterrupted) {
        var travelUntilTime = getAvailability(futureNow.toLocalDate()).getTo();
        var travelUntilDate = futureNow.toLocalDate();
        var travelUntil = LocalDateTime.of(travelUntilDate, travelUntilTime);
        if (travelInterrupted)
            routeTimeline.addEvent(TRAVEL_TIME_OVER, travelUntil);
        routeTimeline.addEvent(DAY_OVER, travelUntil);
    }

    private LocalDateTime getNextAvailableDate() {
        var subsequentDate = futureNow.toLocalDate().plusDays(1);
        while (true) {
            if (!getAvailability(subsequentDate).getExcluded())
                break;
            else {
                routeTimeline.addEvent(DAY_EXCLUDED, subsequentDate.atStartOfDay());
                subsequentDate = subsequentDate.plusDays(1);
            }
        }
        return LocalDateTime.of(subsequentDate, getAvailability(subsequentDate).getFrom());
    }

    public void finish() {
        routeTimeline.addEvent(FINISH, futureNow);
    }

    public void addEvent(RouteEvent event, LocationDto locationDto) {
        routeTimeline.addLocationEvent(event, futureNow, Duration.ZERO, locationDto);
    }

    /*
        TODO: Additional fields needed:
        - default availability
        - travel until the location time sensitivity (example: when only 30min overtime left to reach the location then
          it should be allowed)
        - route location travel until location reached (hotel etc)?
     */

}
