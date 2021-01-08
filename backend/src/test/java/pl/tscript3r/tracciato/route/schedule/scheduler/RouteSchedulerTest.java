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
public class RouteSchedulerTest {

    RoutePermutationsFactory routePermutationsFactory;
    RouteScheduler routeScheduler;
    ExecutorService executorService;

    public static RouteScheduler getRouteScheduler(RoutePermutationsFactory routePermutationsFactory,
                                                   ExecutorService executorService) {
        return new RouteScheduler(routePermutationsFactory, executorService);
    }

    public static RouteScheduler getFakeRouteScheduler() {
        var routePermutationsFactory = new RoutePermutationsFactory(new FakeDurationProvider());
        var executorService = Executors.newSingleThreadExecutor();
        return getRouteScheduler(routePermutationsFactory, executorService);
    }

    @BeforeEach
    void setUp() {
        routePermutationsFactory = new RoutePermutationsFactory(new FakeDurationProvider());
        executorService = Executors.newSingleThreadExecutor();
        routeScheduler = getRouteScheduler(routePermutationsFactory, executorService);
    }

    @Test
    void schedule_Should_ReturnRequestDtoWithSameRouteUuidAsRouteDtoUuid_When_Called() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var results = routeScheduler.schedule(routeDto);

        // then
        assertEquals(routeDto.getUuid(), results.getRouteUuid());
    }

    @Test
    void schedule_Should_IgnoreScheduleRequest_When_SameRouteDtoIsCurrentlyProcessed() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = routeScheduler.schedule(routeDto);
        var secondCallRequest = routeScheduler.schedule(routeDto);

        // then
        assertEquals(routeScheduler.getRequestSupplier(firstCallRequest.getRequestUuid()),
                routeScheduler.getRequestSupplier(secondCallRequest.getRequestUuid()));
    }

    @Test
    void schedule_Should_ScheduleRequestAgain_When_PreviousRequestIsAlreadyFinished() throws ExecutionException, InterruptedException {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = routeScheduler.schedule(routeDto);
        var firstCallFuture = routeScheduler.getRequestSupplier(firstCallRequest.getRequestUuid());
        firstCallFuture.get();
        var secondCallRequest = routeScheduler.schedule(routeDto);

        // then
        assertNotEquals(firstCallFuture, routeScheduler.getRequestSupplier(secondCallRequest.getRequestUuid()));
    }

    @Test
    void getRequestFuture_Should_ReturnFutureOfRouteSimulationResults_When_ExistingRequestUuidIsGiven() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        var request = routeScheduler.schedule(routeDto);


        // when
        var results = routeScheduler.getRequestSupplier(request.getRequestUuid());

        // then
        assertNotNull(results);
    }

    @Test
    void getRequestFuture_Should_ReturnNull_When_NonExistingRequestUuidIsGiven() {
        // given
        var results = routeScheduler.getRequestSupplier(UUID.randomUUID());

        // then
        assertNull(results);
    }

    @Test
    void destroy_Should_ShutdownExecutorService_When_Called() {
        // given
        routeScheduler.destroy();

        // then
        assertTrue(executorService.isShutdown());
    }

}