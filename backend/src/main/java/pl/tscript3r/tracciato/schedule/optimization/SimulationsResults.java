package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.tscript3r.tracciato.route.api.RouteDto;

@AllArgsConstructor
public class SimulationsResults {

    @Getter
    private final RouteDto routeDto;
    private final PermutationSimulation mostTunedRoute;
    private final PermutationSimulation mostOptimalRoute;

    public PermutationSimulation getMostTunedRoute() {
        return mostTunedRoute;
    }

    public PermutationSimulation getMostOptimalRoute() {
        return mostOptimalRoute;
    }

}
