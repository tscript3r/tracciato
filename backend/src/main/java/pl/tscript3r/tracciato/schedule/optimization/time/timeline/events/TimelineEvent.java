package pl.tscript3r.tracciato.schedule.optimization.time.timeline.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@RequiredArgsConstructor
public class TimelineEvent {

    protected final RouteEvent routeEvent;
    protected final LocalDateTime beginning;

}
