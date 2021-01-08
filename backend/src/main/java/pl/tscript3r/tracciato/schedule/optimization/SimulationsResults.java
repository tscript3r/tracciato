package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimulationsResults {

    private final PermutationSimulation mostTunedRoute;
    private final PermutationSimulation mostOptimalRoute;

    public PermutationSimulation getMostTunedRoute() {
        return mostTunedRoute;
    }

    public PermutationSimulation getMostOptimalRoute() {
        return mostOptimalRoute;
    }

}
