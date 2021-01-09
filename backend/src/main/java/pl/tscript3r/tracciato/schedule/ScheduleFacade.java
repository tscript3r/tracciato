package pl.tscript3r.tracciato.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.Optimizer;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.scheduled.ScheduledResultsEntity;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor
public class ScheduleFacade {

    private final RouteFacade routeFacade;
    private final Optimizer optimizer;

    public InternalResponse<RouteDto> validate(String token, UUID routeUuid) {
        return routeFacade.getRoute(token, routeUuid)
                .flatMap(BeforeScheduleValidator::validate);
    }

    public InternalResponse<?> schedule(String token, UUID routeUuid, boolean await) {
        return validate(token, routeUuid)
                .flatMap(routeDto -> await ? schedule(routeDto) : scheduleAsync(routeDto));
    }

    private InternalResponse<ScheduledResultsEntity> schedule(RouteDto routeDto) {
        return InternalResponse.payload(optimizer.optimize(routeDto))
                .map(scheduleRequestDto -> optimizer.getRequestSupplier(scheduleRequestDto.getRequestUuid()))
                .flatMap(this::unwrap);
    }

    private InternalResponse<ScheduledResultsEntity> unwrap(Future<ScheduledResultsEntity> routeScheduleResultsFuture) {
        try {
            return InternalResponse.payload(routeScheduleResultsFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            return InternalResponse.failure(GlobalFailureResponse.INTERNAL_SERVER_ERROR);
        }
    }

    private InternalResponse<ScheduleRequestDto> scheduleAsync(RouteDto routeDto) {
        return InternalResponse.payload(optimizer.optimize(routeDto));
    }

}
