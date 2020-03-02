package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.ArrayList;
import java.util.List;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.NOT_IMPLEMENTED_ERROR;

@Getter
@ToString
@RequiredArgsConstructor
public class ProcessedRoute {

    private final RouteDto routeDto;
    private final Durations durations;
    private final List<List<RouteLocationDto>> combinations;
    private final List<RouteSolution> solutions = new ArrayList<>();

    public void createSolutions() {
        combinations.forEach(c -> solutions.add(RouteSolution.estimate(routeDto, c, durations)));
    }

    public InternalResponse<ScheduledRoute> toScheduledRoute() {
        return InternalResponse.failure(NOT_IMPLEMENTED_ERROR);
    }

}