package pl.tscript3r.tracciato.schedule.optimization;

import pl.tscript3r.tracciato.schedule.optimization.time.RouteTime;
import pl.tscript3r.tracciato.schedule.optimization.time.timeline.events.TimelineEvent;
import pl.tscript3r.tracciato.scheduled.ScheduledResultsEntity;
import pl.tscript3r.tracciato.scheduled.ScheduledVariationEntity;
import pl.tscript3r.tracciato.stop.StopDto;
import pl.tscript3r.tracciato.stop.StopEntity;
import pl.tscript3r.tracciato.stop.StopMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

final class SimulationsResults2Entity {

    private SimulationsResults2Entity() {
    }

    public static ScheduledResultsEntity map(UUID requestUuid, SimulationsResults simulationsResults) {
        var results = new ScheduledResultsEntity();
        results.setRequestUuid(requestUuid);
        if (simulationsResults.getMostTunedRoute() != null) {
            var accurate = simulationsResults.getMostTunedRoute();
            results.setRouteUuid(accurate.getRouteDto().getUuid());
            results.setTuned(map(accurate));
        }
        if (simulationsResults.getMostOptimalRoute() != null) {
            var optimal = simulationsResults.getMostOptimalRoute();
            results.setRouteUuid(optimal.getRouteDto().getUuid());
            results.setOptimal(map(optimal));
        }
        return results;
    }

    private static ScheduledVariationEntity map(PermutationSimulation permutationSimulation) {
        var results = new ScheduledVariationEntity();
        results.setEndingDate(permutationSimulation.getEndingDate());
        results.setMissedAppointments(map(permutationSimulation.getMissedAppointments()));
        results.setTravelledMeters(permutationSimulation.getTravelledMeters());
        results.setOrder(map(permutationSimulation.getOrderedRoute()));
        results.setTimeline(map(permutationSimulation.getRouteTime()));
        results.setMissedAppointmentsCount(permutationSimulation.getMissedAppointmentsCount());
        return results;
    }

    private static List<StopEntity> map(List<StopDto> routeLocations) {
        return routeLocations.stream()
                .map(StopMapper::map)
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
