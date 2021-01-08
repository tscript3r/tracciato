package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.scheduled.ScheduledResultsEntity;

import java.util.UUID;
import java.util.function.Supplier;

import static pl.tscript3r.tracciato.schedule.optimization.SimulationsResults2Entity.map;


@AllArgsConstructor
public class SimulationsSupplier implements Supplier<ScheduledResultsEntity> {

    private final UUID requestId;
    private final PermutationsGroup permutationsGroup;

    @Override
    public ScheduledResultsEntity get() {
        return map(requestId, permutationsGroup.executeAndGetSimulationsResults());
    }

}
