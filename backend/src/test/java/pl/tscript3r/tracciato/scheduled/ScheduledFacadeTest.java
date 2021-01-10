package pl.tscript3r.tracciato.scheduled;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulation;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulationTest;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class ScheduledFacadeTest {

    ScheduledFacade scheduledFacade;
    InMemoryRepositoryAdapter<ScheduledResultsEntity> repository;
    PermutationSimulation tunedVariationSimulation;
    PermutationSimulation optimalVariationSimulation;
    SimulationsResults simulationsResults;
    UUID ownerUuid;
    RouteDto routeDto;
    ScheduleRequestDto scheduleRequestDto;

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setId(1L);
        repository = new InMemoryRepositoryAdapter<>();
        scheduledFacade = ScheduledSpringConfiguration.getInMemoryScheduledFacade(repository);
        tunedVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        optimalVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        simulationsResults = new SimulationsResults(tunedVariationSimulation, optimalVariationSimulation);
        scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setRequestUuid(UUID.randomUUID());
        scheduleRequestDto.setRouteUuid(UUID.randomUUID());
    }

    @Test
    void save_Should_SuccessfullySaveValidSimulationResults_When_Called() {
        // when
        var results = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // then
        assertNotNull(results);
        assertTrue(repository.findByUuid(results.getUuid()).isDefined());
    }

}