package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import lombok.Getter;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
public class TravelLocationTimelineEvent extends DurationLocationTimelineEvent {

    private final LocationDto destination;

    public TravelLocationTimelineEvent(RouteEvent routeEvent, LocalDateTime beginning,
                                       Duration duration, LocationDto fromLocation, LocationDto destination) {
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

}
