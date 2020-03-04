package pl.tscript3r.tracciato.route.schedule.scheduler;

import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.schedule.scheduler.api.ScheduleRequestDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class RouteSchedulerFacade {

    private final Map<UUID, RouteScheduler> scheduleLMap = new LinkedHashMap<>();
    private final DurationProvider durationProvider;

    public RouteSchedulerFacade(DurationProvider durationProvider) {
        this.durationProvider = durationProvider;
    }

    public ScheduleRequestDto schedule(RouteDto routeDto) {
        return null;
    }

}
