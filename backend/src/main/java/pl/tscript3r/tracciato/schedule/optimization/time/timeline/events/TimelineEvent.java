package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class TimelineEvent {

    protected final RouteEvent routeEvent;
    protected final LocalDateTime beginning;

    public Map<String, String> toMap() {
        var map = new LinkedHashMap<String, String>();
        map.put("event", routeEvent.name());
        map.put("beginning", beginning.toString());
        return map;
    }

}
