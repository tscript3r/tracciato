package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class DurationLocationTimelineEvent extends LocationTimelineEvent {

    private final Duration duration;

    public DurationLocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, LocationDto locationDto, Duration duration) {
        super(routeEvent, beginning, locationDto);
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "DurationLocationTimelineEvent{" +
                "duration=" + duration +
                ", routeEvent=" + routeEvent +
                ", beginning=" + beginning +
                '}';
    }

}