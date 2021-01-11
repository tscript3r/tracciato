package pl.tscript3r.tracciato.scheduled;

import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulation;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.schedule.optimization.time.RouteTime;
import pl.tscript3r.tracciato.schedule.optimization.time.timeline.events.TimelineEvent;
import pl.tscript3r.tracciato.stop.StopDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
final class SimulationsResults2Entity {

    private SimulationsResults2Entity() {
    }

    public static ScheduledResultsEntity map(ScheduleRequestDto request, SimulationsResults simulationsResults) {
        var results = new ScheduledResultsEntity();
        results.setRequestUuid(request.getRequestUuid());
        results.setUuid(UUID.randomUUID());
        results.setOwnerUuid(request.getOwnerUuid());
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

    private static List<UUID> map(List<StopDto> stops) {
        return stops.stream()
                .map(StopDto::getUuid)
                .collect(Collectors.toList());
    }

    private static List<Map<String, String>> map(RouteTime routeTime) {
        return routeTime.getRouteTimeline()
                .getEvents()
                .stream()
                .map(TimelineEvent::toMap)
                .collect(Collectors.toList());
    }

}
