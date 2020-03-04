package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import lombok.Getter;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.LocalDateTime;

public class LocationTimelineEvent extends TimelineEvent {

    @Getter
    private final LocationDto locationDto;

    public LocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, LocationDto locationDto) {
        super(routeEvent, beginning);
        this.locationDto = locationDto;
    }

    @Override
    public String toString() {
        return "LocationTimelineEvent{" +
                "locationDto=" + locationDto +
                ", routeEvent=" + routeEvent +
                ", beginning=" + beginning +
                '}';
    }

}
