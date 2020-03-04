package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class RouteScheduleResults {

    private final Optional<RoutePermutation> mostAccurateRoute;
    private final Optional<RoutePermutation> mostOptimalRoute;

}
