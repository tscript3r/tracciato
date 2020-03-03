package pl.tscript3r.tracciato.route.schedule.scheduler;

import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class RouteSchedulerFacade {

    private final ExecutorService executorService;
    private final Map<UUID, RouteScheduler> scheduleLMap = new LinkedHashMap<>();

    public RouteSchedulerFacade(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ScheduleRequestDto schedule(RouteDto routeDto) {
        return null;
    }

}
