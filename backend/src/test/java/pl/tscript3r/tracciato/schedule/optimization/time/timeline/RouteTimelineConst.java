package pl.tscript3r.tracciato.schedule.optimization.time.timeline;

import pl.tscript3r.tracciato.schedule.optimization.time.timeline.events.RouteEvent;
import pl.tscript3r.tracciato.schedule.optimization.time.timeline.events.TimelineEvent;

public final class RouteTimelineConst {

    private RouteTimelineConst() {
    }

    public static TimelineEvent getEvent(RouteTimeline routeTimeline, RouteEvent routeEvent) {
        return routeTimeline.getEvents()
                .stream()
                .filter(timelineEvent -> timelineEvent.getRouteEvent().equals(routeEvent))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public static boolean isEventAdded(RouteTimeline routeTimeline, RouteEvent routeEvent) {
        return routeTimeline.getEvents()
                .stream()
                .anyMatch(timelineEvent -> timelineEvent.getRouteEvent().equals(routeEvent));
    }

}
