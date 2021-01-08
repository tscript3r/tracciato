package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
class RoutePermutationsGroup {

    @Getter
    private final RouteDto routeDto;
    private final Durations durations;
    private final List<List<RouteLocationDto>> permutationsGroup;
    private final List<RoutePermutationSimulation> simulations = new ArrayList<>();

    public RouteSimulationsResults executeAndGetSimulationsResults() {
        executeSimulations();
        return new RouteSimulationsResults(findMostAccurateRoute(), findMostOptimalRoute());
    }

    private void executeSimulations() {
        permutationsGroup.forEach(permutation ->
                simulations.add(RoutePermutationSimulation.simulate(routeDto, permutation, durations))
        );
    }

    private RoutePermutationSimulation findMostAccurateRoute() {
        return simulations.stream()
                .min(Comparator.comparing(RoutePermutationSimulation::getMissedAppointmentsCount)
                        .thenComparing(RoutePermutationSimulation::getEndingDate))
                .orElse(null);
    }

    private RoutePermutationSimulation findMostOptimalRoute() {
        return simulations.stream()
                .min(Comparator.comparing(RoutePermutationSimulation::getEndingDate))
                .orElse(null);
    }

}