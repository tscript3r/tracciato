package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class TravelLocationTimelineEvent extends DurationLocationTimelineEvent {

    private final UUID destination;

    public TravelLocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning,
                                       Duration duration, UUID fromLocation, UUID destination) {
        super(routeEvent, beginning, fromLocation, duration);
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "TravelLocationTimelineEvent{" +
                "destination=" + destination +
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
        if (destination != null)
            map.put("destination", destination.toString());
        map.put("duration", duration.toString());
        return map;
    }

}
