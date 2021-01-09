package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.route.RouteEntity;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.schedule.optimization.time.RouteTime;
import pl.tscript3r.tracciato.schedule.optimization.time.timeline.events.TimelineEvent;
import pl.tscript3r.tracciato.scheduled.ScheduledResultsEntity;
import pl.tscript3r.tracciato.scheduled.ScheduledVariationEntity;
import pl.tscript3r.tracciato.stop.StopDto;
import pl.tscript3r.tracciato.stop.StopEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
class SimulationsResults2Entity {

    private final RouteFacade routeFacade;

    public ScheduledResultsEntity map(UUID requestUuid, SimulationsResults simulationsResults) {
        var results = new ScheduledResultsEntity();
        results.setRequestUuid(requestUuid);
        if (simulationsResults.getMostTunedRoute() != null) {
            var accurate = simulationsResults.getMostTunedRoute();
            results.setRouteUuid(accurate.getRouteDto().getUuid());
            var routeEntity = routeFacade.getByUuid(accurate.getRouteDto().getUuid());
            results.setTuned(map(accurate, routeEntity));
        }
        if (simulationsResults.getMostOptimalRoute() != null) {
            var optimal = simulationsResults.getMostOptimalRoute();
            results.setRouteUuid(optimal.getRouteDto().getUuid());
            var routeEntity = routeFacade.getByUuid(optimal.getRouteDto().getUuid());
            results.setOptimal(map(optimal, routeEntity));
        }
        return results;
    }

    private ScheduledVariationEntity map(PermutationSimulation permutationSimulation, RouteEntity routeEntity) {
        var results = new ScheduledVariationEntity();
        results.setEndingDate(permutationSimulation.getEndingDate());
        results.setMissedAppointments(map(permutationSimulation.getMissedAppointments(), routeEntity));
        results.setTravelledMeters(permutationSimulation.getTravelledMeters());
        results.setOrder(map(permutationSimulation.getOrderedRoute(), routeEntity));
        results.setTimeline(map(permutationSimulation.getRouteTime()));
        results.setMissedAppointmentsCount(permutationSimulation.getMissedAppointmentsCount());
        return results;
    }

    private List<StopEntity> map(List<StopDto> routeLocations, RouteEntity routeEntity) {
        return routeLocations.stream()
                .map(stopDto ->
                        routeEntity.getStops()
                                .stream()
                                .filter(stopEntity -> stopDto.getUuid().equals(stopEntity.getUuid()))
                                .findFirst()
                                .orElseThrow(() -> new TracciatoSchedulerException("Route stop not found"))
                )
                .collect(Collectors.toList());
    }

    private List<String> map(RouteTime routeTime) {
        return routeTime.getRouteTimeline()
                .getEvents()
                .stream()
                .map(TimelineEvent::toString)
                .collect(Collectors.toList());
    }

}
