package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class ScheduledRoute {

    private final Optional<CombinationRoute> mostAccurateRoute;
    private final Optional<CombinationRoute> mostOptimalRoute;

}
