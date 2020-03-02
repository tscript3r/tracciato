package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.Duration;
import java.time.LocalDateTime;

public class LocationTimelineEvent extends DurationTimelineEvent {

    private final LocationDto locationDto;

    public LocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning, Duration duration, LocationDto locationDto) {
        super(routeEvent, beginning, duration);
        this.locationDto = locationDto;
    }

    @Override
    public String toString() {
        return "LocationTimelineEvent{" +
                "routeEvent=" + routeEvent +
                ", locationDto=" + locationDto +
                ", duration=" + duration +
                ", beginning=" + beginning +
                '}';
    }

}
