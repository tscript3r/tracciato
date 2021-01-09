package pl.tscript3r.tracciato.schedule.optimization;

import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class SimulationsSupplier implements Supplier<SimulationsResults> {

    private final PermutationsGroup permutationsGroup;

    @Override
    public SimulationsResults get() {
        return permutationsGroup.executeAndGetSimulationsResults();
    }

}