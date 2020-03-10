package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
class RouteScheduleCallable implements Callable<RouteScheduleResults> {

    private final RoutePermutationsGroup routePermutationsGroup;

    @Override
    public RouteScheduleResults call() {
        return routePermutationsGroup.getResults();
    }

}
