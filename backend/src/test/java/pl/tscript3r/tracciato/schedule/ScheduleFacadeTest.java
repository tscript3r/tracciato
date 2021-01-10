package pl.tscript3r.tracciato.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.OptimizerTest;
import pl.tscript3r.tracciato.schedule.optimization.api.ScheduleRequestDto;
import pl.tscript3r.tracciato.scheduled.ScheduledFacade;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Route schedule facade")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class ScheduleFacadeTest {

    @Mock
    RouteFacade routeFacade;

    ScheduleFacade scheduleFacade;
    RouteDto routeDto;

    @Mock
    ScheduledFacade scheduledFacade;

    @BeforeEach
    void setUp() {
        var optimizer = OptimizerTest.getFakeOptimizer(scheduledFacade);
        scheduleFacade = new ScheduleFacade(routeFacade, optimizer);
        routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());
        when(routeFacade.getRoute(any(), any())).thenReturn(InternalResponse.payload(routeDto));
    }

    @Test
    void validate_Should_ReturnValidationError_When_StartDateIsAfterMaxEndDate() {
        // given
        routeDto.setStartDate(routeDto.getMaxEndDate().plusDays(1));

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_SiteAvailabilityHoursAreInvalid() {
        // given
        var availability = routeDto.getAvailabilities()
                .stream()
                .limit(1)
                .findAny();
        availability.ifPresent(a -> a.setFrom(a.getTo().plusHours(2)));
        routeDto.setAvailabilities(Collections.singletonList(availability.get()));

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_TotalDurationIsBellowMinimum() {
        // given
        routeDto.setMaxEndDate(routeDto.getStartDate());

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_TrafficPredictionIsNotSet() {
        // given
        routeDto.setTrafficPrediction(null);

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_StartLocationIsNotSet() {
        // given
        routeDto.setStartLocation(null);

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_EndLocationIsNotSet() {
        // given
        routeDto.setEndLocation(null);

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnValidationError_When_BetweenLocationsAreLessThanTwo() {
        // given
        var locations = routeDto.getStops()
                .stream()
                .limit(1)
                .collect(Collectors.toSet());
        routeDto.setStops(locations);

        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
        assertEquals(1, results.getLeft().getAdditionalFields().size());
    }

    @Test
    void validate_Should_ReturnNoValidationErrors_When_ValidatingValidRouteDto() {
        // when
        var results = scheduleFacade.validate("mocked", UUID.randomUUID());

        // then
        assertTrue(results.isRight());
    }

    @Test
    void schedule_Should_ReturnScheduleRequestWithRouteUuid_When_CalledAsync() {
        // when
        var results = scheduleFacade.schedule("mocked", UUID.randomUUID(), false);

        // then
        assertTrue(results.isRight());
        assertEquals(routeDto.getUuid(), ((ScheduleRequestDto) results.get()).getRouteUuid());
    }

    @Test
    void schedule_Should_ReturnScheduledRoute_When_CalledSync() {
        // when
        var results = scheduleFacade.schedule("mocked", UUID.randomUUID(), true);

        // then
        assertTrue(results.isRight());
    }

}