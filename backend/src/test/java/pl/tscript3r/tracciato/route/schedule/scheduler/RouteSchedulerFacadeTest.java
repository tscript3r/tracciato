package pl.tscript3r.tracciato.route.schedule.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Route scheduler")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteSchedulerFacadeTest {

    RoutePermutationsFactory routePermutationsFactory;
    RouteSchedulerFacade routeSchedulerFacade;
    ExecutorService executorService;

    @BeforeEach
    void setUp() {
        routePermutationsFactory = new RoutePermutationsFactory(new FakeDurationProvider());
        executorService = Executors.newSingleThreadExecutor();
        routeSchedulerFacade = new RouteSchedulerFacade(routePermutationsFactory, executorService);
    }

    @Test
    void schedule_Should_ReturnRequestDtoWithSameUuidAsRouteDtoUuid_When_Called() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var results = routeSchedulerFacade.schedule(routeDto);

        // then
        assertEquals(routeDto.getUuid(), results.getRequestUuid());
    }

    @Test
    void schedule_Should_IgnoreScheduleRequest_When_SameRouteDtoIsCurrentlyProcessed() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = routeSchedulerFacade.schedule(routeDto);
        var secondCallRequest = routeSchedulerFacade.schedule(routeDto);

        // then
        assertEquals(routeSchedulerFacade.getRequestFuture(firstCallRequest.getRequestUuid()),
                routeSchedulerFacade.getRequestFuture(secondCallRequest.getRequestUuid()));
    }

    @Test
    void schedule_Should_ScheduleRequestAgain_When_PreviousRequestIsAlreadyFinished() throws ExecutionException, InterruptedException {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = routeSchedulerFacade.schedule(routeDto);
        var firstCallFuture = routeSchedulerFacade.getRequestFuture(firstCallRequest.getRequestUuid());
        firstCallFuture.get();

        var secondCallRequest = routeSchedulerFacade.schedule(routeDto);

        // then
        assertNotEquals(firstCallFuture, routeSchedulerFacade.getRequestFuture(secondCallRequest.getRequestUuid()));
    }

    @Test
    void getRequestFuture_Should_ReturnFutureOfRouteSimulationResults_When_ExistingRequestUuidIsGiven() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        var request = routeSchedulerFacade.schedule(routeDto);


        // when
        var results = routeSchedulerFacade.getRequestFuture(request.getRequestUuid());

        // then
        assertNotNull(results);
    }

    @Test
    void getRequestFuture_Should_ReturnNull_When_NonExistingRequestUuidIsGiven() {
        // given
        var results = routeSchedulerFacade.getRequestFuture(UUID.randomUUID());

        // then
        assertNull(results);
    }

    @Test
    void destroy_Should_ShutdownExecutorService_When_Called() {
        // given
        routeSchedulerFacade.destroy();

        // then
        assertTrue(executorService.isShutdown());
    }

}