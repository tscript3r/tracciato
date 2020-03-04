package pl.tscript3r.tracciato.route.schedule;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.RouteScheduleResults;
import pl.tscript3r.tracciato.route.schedule.scheduler.RouteSchedulerFacade;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.route.schedule.validator.BeforeScheduleValidator;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@AllArgsConstructor
public class RouteScheduleFacade {

    private final RouteFacade routeFacade;
    private final RouteSchedulerFacade scheduler;

    public InternalResponse<RouteDto> validate(String token, UUID routeUuid) {
        return routeFacade.getRoute(token, routeUuid)
                .flatMap(BeforeScheduleValidator::validate);
    }

    public InternalResponse<?> schedule(String token, UUID routeUuid, boolean await) {
        return validate(token, routeUuid)
                .flatMap(routeDto -> await ? schedule(routeDto) : scheduleAsync(routeDto));
    }

    private InternalResponse<RouteScheduleResults> schedule(RouteDto routeDto) {
        return InternalResponse.payload(scheduler.schedule(routeDto))
                .map(scheduleRequestDto -> scheduler.getRequestFuture(scheduleRequestDto.getRequestUuid()))
                .flatMap(this::unwrap);
    }

    private InternalResponse<RouteScheduleResults> unwrap(Future<RouteScheduleResults> routeScheduleResultsFuture) {
        try {
            return InternalResponse.payload(routeScheduleResultsFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // TODO tmp
            return InternalResponse.failure(GlobalFailureResponse.INTERNAL_SERVER_ERROR);
        }
    }

    private InternalResponse<ScheduleRequestDto> scheduleAsync(RouteDto routeDto) {
        return InternalResponse.payload(scheduler.schedule(routeDto));
    }

}
