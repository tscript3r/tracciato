package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class RouteScheduleResults {

    private final Optional<RoutePermutation> mostAccurateRoute;
    private final Optional<RoutePermutation> mostOptimalRoute;

    public RoutePermutation getMostAccurateRoute() {
        return mostAccurateRoute.orElse(null);
    }

    public RoutePermutation getMostOptimalRoute() {
        return mostOptimalRoute.orElse(null);
    }

}
