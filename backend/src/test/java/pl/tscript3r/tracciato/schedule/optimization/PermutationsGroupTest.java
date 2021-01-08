package pl.tscript3r.tracciato.schedule.optimization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Route permutations group")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class PermutationsGroupTest {

    PermutationsGroup permutationsGroup;
    FakeDurationProvider fakeDurationProvider;
    Durations durations;
    RouteDto routeDto;
    List<List<StopDto>> routeLocationPermutationsGroup;

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        fakeDurationProvider = new FakeDurationProvider();
        durations = Durations.get(fakeDurationProvider, DurationsTest.getAllLocations(routeDto));
        routeLocationPermutationsGroup = PermutationsGroupGenerator.generate(routeDto.getStops())
                .collect(Collectors.toList());
        permutationsGroup = new PermutationsGroup(routeDto, durations, routeLocationPermutationsGroup);
    }

    @Test
    void getResults_Should_RequestDurationsXTimes_When_Called() {
        permutationsGroup.executeAndGetSimulationsResults();
        assertTrue(fakeDurationProvider.travelDurationCallsCount > 0);
    }

    @Test
    void getResults_Should_ReturnPopulatedRouteScheduleResults_When_Called() {
        var results = permutationsGroup.executeAndGetSimulationsResults();
        assertNotNull(results);
        assertNotNull(results.getMostTunedRoute());
        assertNotNull(results.getMostOptimalRoute());
    }

    @Test
    void getResults_Should_ReturnMostTunedRouteWithLessMissedAppointmentsThanMostOptimalRoute_When_Called() {
        var results = permutationsGroup.executeAndGetSimulationsResults();
        assertTrue(results.getMostTunedRoute().getMissedAppointmentsCount() <
                results.getMostOptimalRoute().getMissedAppointmentsCount());
    }

    @Test
    void getResults_Should_ReturnMostOptimalRouteWithEndingBeforeMostAccurateRoute_When_Called() {
        var results = permutationsGroup.executeAndGetSimulationsResults();
        assertTrue(results.getMostOptimalRoute()
                .getEndingDate()
                .isBefore(results.getMostTunedRoute().getEndingDate()));
    }

    @Test
    void getRouteDto_ShouldReturnEqualRouteDtoAsGiven_When_Called() {
        assertEquals(routeDto, permutationsGroup.getRouteDto());
    }

}