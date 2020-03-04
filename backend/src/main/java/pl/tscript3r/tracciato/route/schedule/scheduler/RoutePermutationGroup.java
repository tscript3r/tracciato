package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@ToString
@RequiredArgsConstructor
class RoutePermutationGroup {

    private final RouteDto routeDto;
    private final Durations durations;
    private final List<List<RouteLocationDto>> permutationsGroup;
    private final List<RoutePermutation> simulations = new ArrayList<>();

    public void runSimulations() {
        permutationsGroup.forEach(c -> simulations.add(RoutePermutation.simulate(routeDto, c, durations)));
    }

    public InternalResponse<RouteScheduleResults> getResults() {
        runSimulations();
        return InternalResponse.payload(new RouteScheduleResults(findMostAccurateRoute(), findMostOptimalRoute()));
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