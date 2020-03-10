package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class RouteSimulationsResults {

    private final Optional<RoutePermutationSimulation> mostAccurateRoute;
    private final Optional<RoutePermutationSimulation> mostOptimalRoute;

    public RoutePermutationSimulation getMostAccurateRoute() {
        return mostAccurateRoute.orElse(null);
    }

    public RoutePermutationSimulation getMostOptimalRoute() {
        return mostOptimalRoute.orElse(null);
    }

}
