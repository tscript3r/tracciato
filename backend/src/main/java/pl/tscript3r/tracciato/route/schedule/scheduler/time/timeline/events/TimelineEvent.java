package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@RequiredArgsConstructor
public class TimelineEvent {

    final LocalDateTime beginning;
    final RouteEvent routeEvent;

}
