package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
class RouteSimulationsCallable implements Callable<RouteSimulationsResults> {

    private final RoutePermutationsGroup routePermutationsGroup;

    @Override
    public RouteSimulationsResults call() {
        return routePermutationsGroup.executeAndGetSimulationsResults();
    }

}
