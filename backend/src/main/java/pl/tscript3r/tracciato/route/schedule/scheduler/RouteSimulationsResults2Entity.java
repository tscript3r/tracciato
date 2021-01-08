package pl.tscript3r.tracciato.route.schedule.scheduler;

import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.route.location.RouteLocationMapper;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.schedule.scheduled.RouteScheduledResultsEntity;
import pl.tscript3r.tracciato.route.schedule.scheduled.RouteScheduledVariationEntity;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.RouteTime;
import pl.tscript3r.tracciato.route.schedule.scheduler.time.timeline.events.TimelineEvent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

final class RouteSimulationsResults2Entity {

    private RouteSimulationsResults2Entity() {
    }

    public static RouteScheduledResultsEntity map(UUID requestUuid, RouteSimulationsResults routeSimulationsResults) {
        var results = new RouteScheduledResultsEntity();
        results.setRequestUuid(requestUuid);
        if (routeSimulationsResults.getMostTunedRoute() != null) {
            var accurate = routeSimulationsResults.getMostTunedRoute();
            results.setRouteUuid(accurate.getRouteDto().getUuid());
            results.setTuned(map(accurate));
        }
        if (routeSimulationsResults.getMostOptimalRoute() != null) {
            var optimal = routeSimulationsResults.getMostOptimalRoute();
            results.setRouteUuid(optimal.getRouteDto().getUuid());
            results.setOptimal(map(optimal));
        }
        return results;
    }

    private static RouteScheduledVariationEntity map(RoutePermutationSimulation routePermutationSimulation) {
        var results = new RouteScheduledVariationEntity();
        results.setEndingDate(routePermutationSimulation.getEndingDate());
        results.setMissedAppointments(map(routePermutationSimulation.getMissedAppointments()));
        results.setTravelledMeters(routePermutationSimulation.getTravelledMeters());
        results.setOrder(map(routePermutationSimulation.getOrderedRoute()));
        results.setTimeline(map(routePermutationSimulation.getRouteTime()));
        results.setMissedAppointmentsCount(routePermutationSimulation.getMissedAppointmentsCount());
        return results;
    }

    private static List<RouteLocationEntity> map(List<RouteLocationDto> routeLocations) {
        return routeLocations.stream()
                .map(RouteLocationMapper::map)
                .collect(Collectors.toList());
    }

    private static List<String> map(RouteTime routeTime) {
        return routeTime.getRouteTimeline()
                .getEvents()
                .stream()
                .map(TimelineEvent::toString)
                .collect(Collectors.toList());
    }

}
