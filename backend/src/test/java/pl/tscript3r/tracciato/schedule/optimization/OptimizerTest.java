package pl.tscript3r.tracciato.schedule.optimization;

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

@DisplayName("Optimizer")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
public class OptimizerTest {

    PermutationsFactory permutationsFactory;
    Optimizer optimizer;
    ExecutorService executorService;

    public static Optimizer getOptimizer(PermutationsFactory permutationsFactory,
                                         ExecutorService executorService) {
        return new Optimizer(permutationsFactory, executorService);
    }

    public static Optimizer getFakeOptimizer() {
        var routePermutationsFactory = new PermutationsFactory(new FakeDurationProvider());
        var executorService = Executors.newSingleThreadExecutor();
        return getOptimizer(routePermutationsFactory, executorService);
    }

    @BeforeEach
    void setUp() {
        permutationsFactory = new PermutationsFactory(new FakeDurationProvider());
        executorService = Executors.newSingleThreadExecutor();
        optimizer = getOptimizer(permutationsFactory, executorService);
    }

    @Test
    void optimize_Should_ReturnRequestDtoWithSameRouteUuidAsRouteDtoUuid_When_Called() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var results = optimizer.optimize(routeDto);

        // then
        assertEquals(routeDto.getUuid(), results.getRouteUuid());
    }

    @Test
    void optimize_Should_IgnoreScheduleRequest_When_SameRouteDtoIsCurrentlyProcessed() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = optimizer.optimize(routeDto);
        var secondCallRequest = optimizer.optimize(routeDto);

        // then
        assertEquals(optimizer.getRequestSupplier(firstCallRequest.getRequestUuid()),
                optimizer.getRequestSupplier(secondCallRequest.getRequestUuid()));
    }

    @Test
    void optimize_Should_ScheduleRequestAgain_When_PreviousRequestIsAlreadyFinished() throws ExecutionException, InterruptedException {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var firstCallRequest = optimizer.optimize(routeDto);
        var firstCallFuture = optimizer.getRequestSupplier(firstCallRequest.getRequestUuid());
        firstCallFuture.get();
        var secondCallRequest = optimizer.optimize(routeDto);

        // then
        assertNotEquals(firstCallFuture, optimizer.getRequestSupplier(secondCallRequest.getRequestUuid()));
    }

    @Test
    void getRequestFuture_Should_ReturnFutureOfRouteSimulationResults_When_ExistingRequestUuidIsGiven() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        var request = optimizer.optimize(routeDto);


        // when
        var results = optimizer.getRequestSupplier(request.getRequestUuid());

        // then
        assertNotNull(results);
    }

    @Test
    void getRequestFuture_Should_ReturnNull_When_NonExistingRequestUuidIsGiven() {
        // given
        var results = optimizer.getRequestSupplier(UUID.randomUUID());

        // then
        assertNull(results);
    }

    @Test
    void destroy_Should_ShutdownExecutorService_When_Called() {
        // given
        optimizer.destroy();

        // then
        assertTrue(executorService.isShutdown());
    }

}