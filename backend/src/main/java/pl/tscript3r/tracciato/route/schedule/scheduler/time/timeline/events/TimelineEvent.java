package pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
public class TimelineEvent {

    final LocalDateTime beginning;
    final RouteEvent routeEvent;

}
