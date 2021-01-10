package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class DurationLocationTimelineEvent extends LocationTimelineEvent {

    protected final Duration duration;

    public DurationLocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, UUID location, Duration duration) {
        super(routeEvent, beginning, location);
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

    public Map<String, String> toMap() {
        var map = new LinkedHashMap<String, String>();
        map.put("event", routeEvent.name());
        map.put("beginning", beginning.toString());
        if (location != null)
            map.put("location", location.toString());
        map.put("duration", duration.toString());
        return map;
    }

}