package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline;

import lombok.Getter;
import lombok.ToString;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.DurationLocationTimelineEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.LocationTimelineEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.TimelineEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.TravelLocationTimelineEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent.*;

@ToString
public class RouteTimeline {

    @Getter
    private final List<TimelineEvent> events = new ArrayList<>();

    public void beginning(LocalDateTime date) {
        events.add(new TimelineEvent(BEGINNING, date));
    }

    public void dayStart(LocalDateTime date) {
        events.add(new TimelineEvent(DAY_START, date));
    }

    public void dayOver(LocalDateTime date) {
        events.add(new TimelineEvent(DAY_OVER, date));
    }

    public void travelArrival(LocalDateTime date, LocationDto destination) {
        events.add(new LocationTimelineEvent(TRAVEL_ARRIVAL, date, destination));
    }

    public void finish(LocalDateTime date, LocationDto destination) {
        events.add(new LocationTimelineEvent(FINISH, date, destination));
    }

    public void onsite(LocalDateTime date, Duration duration, LocationDto location) {
        events.add(new DurationLocationTimelineEvent(ONSITE, date, location, duration));
    }

    public void travelContinue(LocalDateTime date, Duration duration, LocationDto location) {
        events.add(new DurationLocationTimelineEvent(TRAVEL_CONTINUE, date, location, duration));
    }

    public void travelStart(LocalDateTime date, LocationDto from, LocationDto destination, Duration travelDuration) {
        events.add(new TravelLocationTimelineEvent(TRAVEL_START, date, travelDuration, from, destination));
    }

    public void routeMaxEndingPassed(LocalDateTime date) {
        events.add(new TimelineEvent(ROUTE_MAX_ENDING_PASSED, date));
    }

    public void travelTimeOver(LocalDateTime date, LocationDto destination, Duration durationLeft) {
        events.add(new DurationLocationTimelineEvent(TRAVEL_TIME_OVER, date, destination, durationLeft));
    }

    public void dayExcluded(LocalDate date) {
        events.add(new TimelineEvent(DAY_EXCLUDED, date.atStartOfDay()));
    }

    public void appointmentMismatched(LocalDateTime date, LocationDto location) {
        events.add(new LocationTimelineEvent(ONSITE_APPOINTMENTS_MISMATCHED, date, location));
    }

    public void appointmentMatched(LocalDateTime date, LocationDto location) {
        events.add(new LocationTimelineEvent(ONSITE_APPOINTMENTS_MATCHED, date, location));
    }

}
