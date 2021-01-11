package pl.tscript3r.tracciato.scheduled;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulationTest;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.user.UserFacadeTest;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.user.UserConst.*;

@DisplayName("Scheduled Facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class ScheduledFacadeTest {

    ScheduledFacade scheduledFacade;
    InMemoryRepositoryAdapter<ScheduledResultsEntity> repository;
    SimulationsResults simulationsResults;
    UUID ownerUuid;
    RouteDto routeDto;
    ScheduleRequestDto scheduleRequestDto;
    String registeredUserToken;
    String otherRegisteredUserToken;

    @BeforeEach
    void setUp() {
        var userFacade = UserFacadeTest.getUserFacadeWithRegisteredJohn();
        userFacade.register(getValidEdyUserDto());
        otherRegisteredUserToken = userFacade.getToken(EDY_USERNAME).get();
        registeredUserToken = TOKEN_PREFIX + userFacade.getToken(JOHNS_USERNAME).get();
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setId(1L);
        repository = new InMemoryRepositoryAdapter<>();
        scheduledFacade = ScheduledSpringConfiguration.getInMemoryScheduledFacade(userFacade, repository);
        var tunedVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        var optimalVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        simulationsResults = new SimulationsResults(tunedVariationSimulation, optimalVariationSimulation);
        scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setRequestUuid(UUID.randomUUID());
        scheduleRequestDto.setRouteUuid(UUID.randomUUID());
        var response = userFacade.validateAndGetUuidFromToken(registeredUserToken);
        scheduleRequestDto.setOwnerUuid(response.get());
    }

    @Test
    void save_Should_SuccessfullySaveValidSimulationResults_When_Called() {
        // when
        var results = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // then
        assertNotNull(results);
        assertTrue(repository.findByUuid(results.getUuid()).isDefined());
    }

    @Test
    void getScheduledResults_Should_ReturnScheduledResults_When_ValidTokenOfResourceOwnerIsPassed() {
        // given
        var request = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // when
        var results = scheduledFacade.getScheduledResults(registeredUserToken,
                request.getUuid());

        // then
        assertTrue(results.isRight());
    }

    @Test
    void getScheduledResults_Should_ReturnUnauthorizedResponse_When_ValidTokenOfNotResourceOwnerIsPassed() {
        // given
        var request = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // when
        var results = scheduledFacade.getScheduledResults(otherRegisteredUserToken,
                request.getUuid());

        // then
        assertTrue(results.isLeft());
        assertEquals(400, results.getLeft().getHttpStatus());
    }

    @Test
    void getScheduledResults_Should_ReturnNotFoundFailureResponse_When_NonExistingUuidPassed() {
        // when
        var results = scheduledFacade.getScheduledResults(registeredUserToken,
                UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(404, results.getLeft().getHttpStatus());
    }

}