package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline;

import lombok.Getter;
import lombok.ToString;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.DurationTimelineEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.LocationTimelineEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.RouteEvent;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.TimelineEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
public class RouteTimeline {

    @Getter
    private final List<TimelineEvent> routeEvents = new ArrayList<>();

    public void addEvent(RouteEvent event, LocalDateTime time) {
        routeEvents.add(new TimelineEvent(time, event));
    }

    public void addDurationEvent(RouteEvent event, LocalDateTime time, Duration duration) {
        routeEvents.add(new DurationTimelineEvent(event, time, duration));
    }

    public void addLocationEvent(RouteEvent event, LocalDateTime time, Duration duration, LocationDto locationDto) {
        routeEvents.add(new LocationTimelineEvent(event, time, duration, locationDto));
    }

}
