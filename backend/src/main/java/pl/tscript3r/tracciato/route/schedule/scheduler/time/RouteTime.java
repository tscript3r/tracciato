package pl.tscript3r.tracciato.route.schedule.scheduler.time;

import lombok.Getter;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.RouteTimeline;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent.*;

public class RouteTime {

    private final RouteDto routeDto;

    @Getter
    private final RouteTimeline routeTimeline;

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
        return getAvailability(futureNow.toLocalDate()).getTo();
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
        if (travelInterrupted)
            routeTimeline.addEvent(TRAVEL_TIME_OVER, futureNow);
        routeTimeline.addEvent(DAY_OVER, futureNow);
        futureNow = LocalDateTime.of(futureNow.toLocalDate().plusDays(1),
                getAvailability(futureNow.toLocalDate()).getFrom());
        routeTimeline.addEvent(DAY_START, futureNow);
        if (travelInterrupted)
            routeTimeline.addEvent(TRAVEL_CONTINUE, futureNow);
        futureNow = futureNow.plus(plus);
    }

    public void finish() {
        routeTimeline.addEvent(FINISH, futureNow);
    }

}
