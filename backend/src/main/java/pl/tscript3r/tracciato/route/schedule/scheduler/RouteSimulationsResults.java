package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RouteSimulationsResults {

    private final RoutePermutationSimulation mostTunedRoute;
    private final RoutePermutationSimulation mostOptimalRoute;

    public RoutePermutationSimulation getMostTunedRoute() {
        return mostTunedRoute;
    }

    public RoutePermutationSimulation getMostOptimalRoute() {
        return mostOptimalRoute;
    }

}
