package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class RouteSchedulerFacade implements DisposableBean {

    private final Map<UUID, Future<RouteScheduleResults>> scheduleRequests = new LinkedHashMap<>();
    private final RoutePermutationsFactory routePermutationsFactory;
    private final ExecutorService executorService;

    public ScheduleRequestDto schedule(RouteDto routeDto) {
        // TODO: for now schedule request uuid == route.uuid
        submit(routeDto.getUuid(), routeDto);
        var results = new ScheduleRequestDto();
        results.setRequestUuid(routeDto.getUuid());
        return results;
    }

    private void submit(UUID uuid, RouteDto routeDto) {
        if ((!scheduleRequests.containsKey(uuid)) ||
                (scheduleRequests.containsKey(uuid) && scheduleRequests.get(uuid).isDone())) {
            var future = executorService.submit(new RouteScheduleCallable(routePermutationsFactory.get(routeDto)));
            scheduleRequests.put(uuid, future);
        } else
            log.warn("Schedule request for route uuid={} rejected (route is already processed)", routeDto.getUuid());
    }

    public Future<RouteScheduleResults> getRequestFuture(UUID requestUuid) {
        return scheduleRequests.get(requestUuid);
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }

}
