package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
class RoutePermutationsGroup {

    @Getter
    private final RouteDto routeDto;
    private final Durations durations;
    private final List<List<RouteLocationDto>> permutationsGroup;
    private final List<RoutePermutation> simulations = new ArrayList<>();

    public RouteScheduleResults getResults() {
        executeSimulations();
        return new RouteScheduleResults(findMostAccurateRoute(), findMostOptimalRoute());
    }

    private void executeSimulations() {
        permutationsGroup.forEach(c -> simulations.add(RoutePermutation.simulate(routeDto, c, durations)));
    }

    private Optional<RoutePermutation> findMostAccurateRoute() {
        return simulations.stream()
                .min(Comparator.comparing(RoutePermutation::getMissedAppointmentsCount)
                        .thenComparing(RoutePermutation::getEndingDate));
    }

    private Optional<RoutePermutation> findMostOptimalRoute() {
        return simulations.stream()
                .min(Comparator.comparing(RoutePermutation::getEndingDate));
    }

}