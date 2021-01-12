package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulation;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulationTest;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Route Simulations Results to Route Scheduled Results")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class SimulationsResults2EntityTest {

    SimulationsResults simulationsResults;
    UUID ownerUuid = UUID.randomUUID();
    RouteDto routeDto;
    Long routeId = 1L;
    PermutationSimulation tunedVariationSimulation;
    PermutationSimulation optimalVariationSimulation;
    ScheduleRequestDto scheduleRequestDto;
    SimulationsResults2Entity simulationsResults2Entity;

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setId(routeId);
        tunedVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        optimalVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        simulationsResults = new SimulationsResults(routeDto, tunedVariationSimulation, optimalVariationSimulation);
        scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setRequestUuid(UUID.randomUUID());
        scheduleRequestDto.setRouteUuid(routeDto.getUuid());
        scheduleRequestDto.setOwnerUuid(routeDto.getUuid());
        simulationsResults2Entity = new SimulationsResults2Entity(new ObjectMapper());
    }

    @Test
    void map_Should_ReturnOnlyRequestUuid_When_SimulationResultsContainsEitherNoOptimalAndAccurateResults() {
        // given
        var routeSimulationsResults = new SimulationsResults(routeDto, null, null);

        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, routeSimulationsResults);

        // then
        assertNotNull(result);
        assertEquals(scheduleRequestDto.getRequestUuid(), result.getRequestUuid());
    }

    @Test
    void map_Should_ReturnMostOptimalRoute_When_SimulationResultsContainsIt() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        assertNotNull(result.getOptimal());
    }

    @Test
    void map_Should_ReturnMostTunedRoute_When_SimulationResultsContainsIt() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        assertNotNull(result.getTuned());
    }

    @Test
    void map_Should_ReturnEqualEndingDate_When_Called() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        var tunedVariation = result.getTuned();
        assertEquals(tunedVariationSimulation.getEndingDate(), tunedVariation.getEndingDate());
    }

    @Test
    void map_Should_ReturnSameSizeMissedAppointmentsList_When_Called() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        var tunedVariation = result.getTuned();
        assertEquals(tunedVariationSimulation.getMissedAppointments().size(),
                tunedVariation.getMissedAppointments().size());
    }

    @Test
    void map_Should_ReturnSameTravelledMetersValue_When_Called() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        var tunedVariation = result.getTuned();
        assertEquals(tunedVariationSimulation.getMissedAppointments().size(),
                tunedVariation.getMissedAppointments().size());
    }

    @Test
    void map_Should_ReturnRouteDtoAsValidJsonString_When_Called() {
        // when
        var result = simulationsResults2Entity.map(scheduleRequestDto, simulationsResults);

        // then
        assertDoesNotThrow(() -> new ObjectMapper().readValue(result.getRouteDto(), RouteDto.class));
    }

}