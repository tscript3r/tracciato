package pl.tscript3r.tracciato.schedule.optimization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.scheduled.ScheduledFacade;
import pl.tscript3r.tracciato.scheduled.ScheduledResultsEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@RequiredArgsConstructor
public class Optimizer implements DisposableBean {

    private final Map<ScheduleRequestDto, CompletableFuture<ScheduledResultsEntity>> scheduleRequests = new LinkedHashMap<>();
    private final PermutationsFactory permutationsFactory;
    private final ExecutorService executorService;
    private final ScheduledFacade scheduledFacade;

    public ScheduleRequestDto optimize(RouteDto routeDto) {
        return getScheduleRequestAndFilterIfUnderProcessingOptional(routeDto).orElseGet(() -> {
            var results = new ScheduleRequestDto();
            results.setRequestUuid(UUID.randomUUID());
            results.setRouteUuid(routeDto.getUuid());
            submit(results, routeDto);
            return results;
        });
    }

    private Optional<ScheduleRequestDto> getScheduleRequestAndFilterIfUnderProcessingOptional(RouteDto routeDto) {
        return scheduleRequests.keySet()
                .stream()
                .filter(scheduleRequestDto -> scheduleRequestDto.getRouteUuid().equals(routeDto.getUuid()))
                .findFirst()
                .filter(scheduleRequestDto -> !scheduleRequests.get(scheduleRequestDto).isDone());
    }

    private void submit(ScheduleRequestDto scheduleRequestDto, RouteDto routeDto) {
        var supplier = new SimulationsSupplier(permutationsFactory.get(routeDto));
        var future = CompletableFuture.supplyAsync(supplier, executorService)
                .thenApply(scheduledFacade::save);

        scheduleRequests.put(scheduleRequestDto, future);
    }

    public CompletableFuture<ScheduledResultsEntity> getRequestSupplier(UUID requestUuid) {
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
