package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;

import static pl.tscript3r.tracciato.scheduled.SimulationsResults2Entity.map;

@AllArgsConstructor
public class ScheduledFacade {

    private final ScheduledDao dao;

    public ScheduledResultsDto save(ScheduleRequestDto request, SimulationsResults simulationsResults) {
        return dao.save(map(request.getRequestUuid(), simulationsResults));
    }

}