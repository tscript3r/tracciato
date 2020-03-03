package pl.tscript3r.tracciato.route.schedule;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.RouteSchedulerFacade;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.route.schedule.validator.BeforeScheduleValidator;

import java.util.UUID;

@AllArgsConstructor
public class RouteScheduleFacade {

    private final RouteFacade routeFacade;
    private final RouteSchedulerFacade scheduler;

    public InternalResponse<RouteDto> validate(String token, UUID routeUuid) {
        return routeFacade.getRoute(token, routeUuid)
                .flatMap(BeforeScheduleValidator::validate);
    }

    public InternalResponse<ScheduleRequestDto> schedule(String token, UUID routeUuid) {
        return routeFacade.getRoute(token, routeUuid)
                .map(scheduler::schedule);
    }

}
