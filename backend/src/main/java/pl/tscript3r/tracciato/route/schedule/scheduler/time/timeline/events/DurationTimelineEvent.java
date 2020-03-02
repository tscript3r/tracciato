package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import java.time.Duration;
import java.time.LocalDateTime;

public class DurationTimelineEvent extends TimelineEvent {

    final Duration duration;

    public DurationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, Duration duration) {
        super(beginning, routeEvent);
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DurationTimelineEvent{" +
                "duration=" + duration +
                ", beginning=" + beginning +
                ", routeEvent=" + routeEvent +
                '}';
    }

}