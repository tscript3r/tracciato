package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class RouteScheduler implements DisposableBean {

    private final Map<ScheduleRequestDto, Future<RouteSimulationsResults>> scheduleRequests = new LinkedHashMap<>();
    private final RoutePermutationsFactory routePermutationsFactory;
    private final ExecutorService executorService;

    public ScheduleRequestDto schedule(RouteDto routeDto) {
        return getRouteScheduleRequestAndFilterIfUnderProcessingOptional(routeDto).orElseGet(() -> {
            var results = new ScheduleRequestDto();
            results.setRequestUuid(UUID.randomUUID());
            results.setRouteUuid(routeDto.getUuid());
            submit(results, routeDto);
            return results;
        });
    }

    private Optional<ScheduleRequestDto> getRouteScheduleRequestAndFilterIfUnderProcessingOptional(RouteDto routeDto) {
        return scheduleRequests.keySet()
                .stream()
                .filter(scheduleRequestDto -> scheduleRequestDto.getRouteUuid().equals(routeDto.getUuid()))
                .findFirst()
                .filter(scheduleRequestDto -> !scheduleRequests.get(scheduleRequestDto).isDone());
    }

    private void submit(ScheduleRequestDto scheduleRequestDto, RouteDto routeDto) {
        var future = executorService.submit(new RouteSimulationsCallable(routePermutationsFactory.get(routeDto)));
        scheduleRequests.put(scheduleRequestDto, future);
    }

    public Future<RouteSimulationsResults> getRequestFuture(UUID requestUuid) {
        return scheduleRequests.get(findScheduleRequest(requestUuid).orElse(null));
    }

    private Optional<ScheduleRequestDto> findScheduleRequest(UUID uuid) {
        return scheduleRequests.keySet()
                .stream()
                .filter(scheduleRequestDto -> scheduleRequestDto.getRequestUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }

}
