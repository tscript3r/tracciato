package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.route.schedule.scheduled.RouteScheduledResultsEntity;

import java.util.UUID;
import java.util.function.Supplier;

import static pl.tscript3r.tracciato.route.schedule.scheduler.RouteSimulationsResults2Entity.map;


@AllArgsConstructor
public
class RouteSimulationsSupplier implements Supplier<RouteScheduledResultsEntity> {

    private final UUID requestId;
    private final RoutePermutationsGroup routePermutationsGroup;

    @Override
    public RouteScheduledResultsEntity get() {
        return map(requestId, routePermutationsGroup.executeAndGetSimulationsResults());
    }

}
