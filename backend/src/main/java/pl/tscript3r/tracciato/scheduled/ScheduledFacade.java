package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static pl.tscript3r.tracciato.scheduled.SimulationsResults2Entity.map;

@AllArgsConstructor
public class ScheduledFacade {

    private final ScheduledDao dao;
    private final UserFacade userFacade;
    private final RouteFacade routeFacade;

    public ScheduledResultsDto save(ScheduleRequestDto request, SimulationsResults simulationsResults) {
        routeFacade.setAsScheduled(request.getRouteUuid());
        return dao.save(map(request, simulationsResults));
    }

    public InternalResponse<ScheduledResultsDto> getScheduledResults(String token, UUID scheduleUuid) {
        return dao.get(scheduleUuid)
                .flatMap(scheduled -> userFacade.authorize(token, scheduled.getOwnerUuid(), scheduled));
    }

}