package pl.tscript3r.tracciato.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.infrastructure.db.InMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.PermutationSimulationTest;
import pl.tscript3r.tracciato.schedule.optimization.SimulationsResults;
import pl.tscript3r.tracciato.schedule.optimization.TracciatoSchedulerException;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.UserFacadeTest;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.user.UserConst.*;

@DisplayName("Scheduled Facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ScheduledFacadeTest {

    ScheduledFacade scheduledFacade;
    InMemoryRepositoryAdapter<ScheduledResultsEntity> repository;
    SimulationsResults simulationsResults;
    UUID ownerUuid;
    RouteDto routeDto;
    ScheduleRequestDto scheduleRequestDto;
    UserFacade userFacade;
    String registeredUserToken;
    String otherRegisteredUserToken;
    @Mock
    RouteFacade routeFacade;
    ObjectMapper mapper;
    @Mock
    ObjectMapper mockedMapper;

    @BeforeEach
    void setUp() {
        userFacade = UserFacadeTest.getUserFacadeWithRegisteredJohn();
        userFacade.register(getValidEdyUserDto());
        otherRegisteredUserToken = userFacade.getToken(EDY_USERNAME).get();
        registeredUserToken = TOKEN_PREFIX + userFacade.getToken(JOHNS_USERNAME).get();
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setId(1L);
        repository = new InMemoryRepositoryAdapter<>();
        mapper = new ObjectMapper();
        scheduledFacade = ScheduledSpringConfiguration.getInMemoryScheduledFacade(userFacade, repository, routeFacade, mapper);
        var tunedVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        var optimalVariationSimulation = PermutationSimulationTest.getRoutePermutationSimulation(ownerUuid, routeDto);
        simulationsResults = new SimulationsResults(routeDto, tunedVariationSimulation, optimalVariationSimulation);
        scheduleRequestDto = new ScheduleRequestDto();
        scheduleRequestDto.setRequestUuid(UUID.randomUUID());
        scheduleRequestDto.setRouteUuid(routeDto.getUuid());
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
    void save_Should_InvokeRouteFacadeToSetRouteCurrentVersionAsScheduled_When_Called() {
        // when
        var results = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // then
        verify(routeFacade, times(1)).setAsScheduled(scheduleRequestDto.getRouteUuid());
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

    @Test
    void getScheduledResults_Should_ReturnNotMutatedRouteDto_When_OriginalRouteHasBeenChanged() throws JsonProcessingException {
        // given
        var request = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // when
        var results = scheduledFacade.getScheduledResults(registeredUserToken,
                request.getUuid());
        routeFacade.setNewStartLocation(registeredUserToken, routeDto.getUuid(), LocationConst.getGetyngaLocationDto());
        routeDto.setName("changed name");

        // then
        var mappedRouteDto = new ObjectMapper().readValue(results.get().getRouteDto(), RouteDto.class);
        assertNotEquals(routeDto.getName(), mappedRouteDto.getName());
    }

    @Test
    void save_Should_ReturnResultsDespiteObjectMapperDidThrowJsonProcessingException_When_Called()
            throws JsonProcessingException {
        // given
        scheduledFacade = ScheduledSpringConfiguration.getInMemoryScheduledFacade(userFacade, repository, routeFacade,
                mockedMapper);

        // when
        when(mockedMapper.writeValueAsString(any())).thenThrow(TracciatoSchedulerException.class);
        var results = scheduledFacade.save(scheduleRequestDto, simulationsResults);

        // then
        verify(mockedMapper, times(1)).writeValueAsString(any());
        assertNotNull(results);
    }

}