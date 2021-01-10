package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class LocationTimelineEvent extends TimelineEvent {

    @Getter
    protected final UUID location;

    public LocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, UUID location) {
        super(routeEvent, beginning);
        this.location = location;
    }

    @Override
    public String toString() {
        return "LocationTimelineEvent{" +
                "location=" + location +
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
        return map;
    }

}
