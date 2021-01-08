package pl.tscript3r.tracciato.schedule.optimization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class PermutationsGroup {

    @Getter
    private final RouteDto routeDto;
    private final Durations durations;
    private final List<List<StopDto>> permutationsGroup;
    private final List<PermutationSimulation> simulations = new ArrayList<>();

    public SimulationsResults executeAndGetSimulationsResults() {
        executeSimulations();
        return new SimulationsResults(findMostAccurateRoute(), findMostOptimalRoute());
    }

    private void executeSimulations() {
        permutationsGroup.forEach(permutation ->
                simulations.add(PermutationSimulation.simulate(routeDto, permutation, durations))
        );
    }

    private PermutationSimulation findMostAccurateRoute() {
        return simulations.stream()
                .min(Comparator.comparing(PermutationSimulation::getMissedAppointmentsCount)
                        .thenComparing(PermutationSimulation::getEndingDate))
                .orElse(null);
    }

    private PermutationSimulation findMostOptimalRoute() {
        return simulations.stream()
                .min(Comparator.comparing(PermutationSimulation::getEndingDate))
                .orElse(null);
    }

}