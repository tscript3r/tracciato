package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.Duration;
import java.time.LocalDateTime;

public class TravelLocationTimelineEvent extends DurationLocationTimelineEvent {

    private final LocationDto destination;

    public TravelLocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning,
                                       Duration duration, LocationDto locationDto, LocationDto destination) {
        super(routeEvent, beginning, locationDto, duration);
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

}
