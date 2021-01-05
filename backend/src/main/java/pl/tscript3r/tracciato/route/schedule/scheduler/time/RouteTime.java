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

public class RouteTime {

    @Getter
    private final RouteTimeline routeTimeline;
    private final RouteDto routeDto;
    @Getter
    private LocalDateTime futureNow;

    public RouteTime(RouteDto routeDto) {
        this.routeDto = routeDto;
        futureNow = routeDto.getStartDate();
        routeTimeline = new RouteTimeline();
        routeTimeline.beginning(futureNow);
    }

    public void travel(LocationDto from, LocationDto destination, Duration travelDuration) {
        routeTimeline.travelStart(futureNow, from, destination, travelDuration);
        addDuration(travelDuration, Duration.ofHours(1), destination);
        routeTimeline.travelArrival(futureNow, destination);
    }

    public void onsite(RouteLocationDto routeLocationDto) {
        var onsiteDuration = Duration.parse(routeLocationDto.getOnsideDuration());
        if (willTravelerDoOvertime(onsiteDuration, Duration.ZERO))
            setNextDay(onsiteDuration, routeLocationDto.getLocation(), false);
        else {
            futureNow = futureNow.plus(onsiteDuration);
            routeTimeline.onsite(futureNow, onsiteDuration, routeLocationDto.getLocation());
        }
    }

    private void addDuration(Duration duration, Duration acceptedOvertime, LocationDto destination) {
        if (willTravelerDoOvertime(duration, acceptedOvertime)) {
            duration = subtractTodaysTimeLeft(duration);
            setNextDay(duration, destination, true);
        } else
            futureNow = futureNow.plus(duration);
    }

    private boolean willTravelerDoOvertime(Duration duration, Duration acceptedOvertime) {
        var incrementedFutureNow = futureNow.plus(duration);
        return futureNow.getDayOfYear() != incrementedFutureNow.getDayOfYear() ||
                incrementedFutureNow.toLocalTime().isAfter(currentDayMaxEndTime(acceptedOvertime));
    }

    private LocalTime currentDayMaxEndTime(Duration acceptedOvertime) {
        // TODO refactor after adding RouteDto field default availability / work hours
        return getAvailability(futureNow.toLocalDate()).getTo().plus(acceptedOvertime);
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

    private void setNextDay(Duration durationLeft, LocationDto destination, boolean travelInterrupted) {
        addNextDay2Timeline(travelInterrupted, destination, durationLeft);
        futureNow = getNextAvailableDate();
        routeTimeline.dayStart(futureNow);
        if (travelInterrupted)
            routeTimeline.travelContinue(futureNow, durationLeft, destination);
        futureNow = futureNow.plus(durationLeft);
        checkMaxRouteEndingIsPassed();
    }

    private void checkMaxRouteEndingIsPassed() {
        if (futureNow.isAfter(routeDto.getMaxEndDate()))
            routeTimeline.routeMaxEndingPassed(futureNow);
    }

    private void addNextDay2Timeline(boolean travelInterrupted, LocationDto destination, Duration durationLeft) {
        var travelUntilTime = getAvailability(futureNow.toLocalDate()).getTo();
        var travelUntilDate = futureNow.toLocalDate();
        var travelUntil = LocalDateTime.of(travelUntilDate, travelUntilTime);
        if (travelInterrupted)
            routeTimeline.travelTimeOver(futureNow, destination, durationLeft);
        routeTimeline.dayOver(travelUntil);
    }

    private LocalDateTime getNextAvailableDate() {
        var subsequentDate = futureNow.toLocalDate().plusDays(1);
        while (true) {
            if (!getAvailability(subsequentDate).getExcluded())
                break;
            else {
                routeTimeline.dayExcluded(subsequentDate);
                subsequentDate = subsequentDate.plusDays(1);
            }
        }
        return LocalDateTime.of(subsequentDate, getAvailability(subsequentDate).getFrom());
    }

    public void finish(LocationDto destination) {
        routeTimeline.finish(futureNow, destination);
    }

    public void appointmentMismatched(LocationDto location) {
        routeTimeline.appointmentMismatched(futureNow, location);
    }

    public void appointmentMatched(LocationDto location) {
        routeTimeline.appointmentMatched(futureNow, location);
    }

    /*
        TODO: Additional fields needed:
        - default availability
        - travel until the location time sensitivity (example: when only 30min overtime left to reach the location then
          it should be allowed)
        - route location travel until location reached (hotel etc)?
     */

}
