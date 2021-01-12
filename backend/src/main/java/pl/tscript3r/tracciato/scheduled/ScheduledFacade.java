package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

@AllArgsConstructor
public class ScheduledFacade {

    private final ScheduledDao dao;
    private final UserFacade userFacade;
    private final RouteFacade routeFacade;
    private final SimulationsResults2Entity mapper;

    public ScheduledResultsDto save(ScheduleRequestDto request, SimulationsResults simulationsResults) {
        var results = dao.save(mapper.map(request, simulationsResults));
        routeFacade.setAsScheduled(request.getRouteUuid());
        return results;
    }

    public InternalResponse<ScheduledResultsDto> getScheduledResults(String token, UUID scheduleUuid) {
        return dao.get(scheduleUuid)
                .flatMap(scheduled -> userFacade.authorize(token, scheduled.getOwnerUuid(), scheduled));
    }

}