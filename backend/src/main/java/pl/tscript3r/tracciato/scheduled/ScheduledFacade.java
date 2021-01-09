package pl.tscript3r.tracciato.scheduled;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;

@AllArgsConstructor
public class ScheduledFacade {

    private final ScheduledDao dao;

    public ScheduledResultsEntity save(SimulationsResults simulationsResults) {
        return null;
    }

}
