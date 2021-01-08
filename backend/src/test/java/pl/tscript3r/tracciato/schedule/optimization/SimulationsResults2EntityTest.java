package pl.tscript3r.tracciato.schedule.optimization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

@DisplayName("Route Simulations Results to Route Scheduled Results Entity ")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class SimulationsResults2EntityTest {

    SimulationsResults simulationsResults;
    UUID requestUuid = UUID.randomUUID();
    UUID ownerUuid = UUID.randomUUID();
    RouteDto routeDto;
    Long routeId = 1L;
    PermutationSimulation tunedVariationSimulation;
    PermutationSimulation optimalVariationSimulation;

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setId(routeId);
        tunedVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        optimalVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        simulationsResults = new SimulationsResults(tunedVariationSimulation, optimalVariationSimulation);
    }

//    @Test
//    void map_Should_ReturnOnlyRequestUuid_When_SimulationResultsContainsEitherNoOptimalAndAccurateResults() {
//        // given
//        routeSimulationsResults = new RouteSimulationsResults(null, null);
//
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        assertNotNull(result);
//        assertEquals(requestUuid, result.getRequestUuid());
//    }
//
//    @Test
//    void map_Should_ReturnMostOptimalRoute_When_SimulationResultsContainsIt() {
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        assertNotNull(result.getOptimal());
//    }
//
//    @Test
//    void map_Should_ReturnMostTunedRoute_When_SimulationResultsContainsIt() {
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        assertNotNull(result.getTuned());
//    }
//
//    @Test
//    void map_Should_ReturnEqualEndingDate_When_Called() {
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        var tunedVariation = result.getTuned();
//        assertEquals(tunedVariationSimulation.getEndingDate(), tunedVariation.getEndingDate());
//    }
//
//    @Test
//    void map_Should_ReturnSameSizeMissedAppointmentsList_When_Called() {
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        var tunedVariation = result.getTuned();
//        assertEquals(tunedVariationSimulation.getMissedAppointments().size(),
//                tunedVariation.getMissedAppointments().size());
//    }
//
//    @Test
//    void map_Should_ReturnSameTravelledMetersValue_When_Called() {
//        // when
//        var result = RouteSimulationsResults2Entity.map(requestUuid, routeSimulationsResults);
//
//        // then
//        var tunedVariation = result.getTuned();
//        assertEquals(tunedVariationSimulation.getMissedAppointments().size(),
//                tunedVariation.getMissedAppointments().size());
//    }

}