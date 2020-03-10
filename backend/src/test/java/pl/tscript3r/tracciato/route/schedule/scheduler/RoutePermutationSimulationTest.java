package pl.tscript3r.tracciato.route.schedule.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.RouteLocationConst;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.route.location.RouteLocationConst.*;
import static pl.tscript3r.tracciato.route.schedule.scheduler.DurationsTest.getAllLocations;

@DisplayName("Route permutation")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RoutePermutationSimulationTest {

    RoutePermutationSimulation routePermutationSimulation;
    RouteDto routeDto;
    Durations durations;
    List<RouteLocationDto> orderedRoute;
    UUID ownerUuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setLocations(getRouteLocationsSet(ownerUuid));
        durations = Durations.get(new FakeDurationProvider(), getAllLocations(routeDto));
        orderedRoute = new ArrayList<>(routeDto.getLocations());
        routePermutationSimulation = RoutePermutationSimulation.simulate(routeDto, new ArrayList<>(routeDto.getLocations()), durations);
    }

    Set<RouteLocationDto> getRouteLocationsSet(UUID ownerUuid) {
        return new LinkedHashSet<>(getRouteLocationsList(ownerUuid)); // LinkedHashSet to not lose current order
    }

    List<RouteLocationDto> getRouteLocationsList(UUID ownerUuid) {
        return Arrays.asList(getStuttgartRouteLocationWithAppointmentWindow(ownerUuid), getBrunszwikRouteLocationDto(ownerUuid),
                getBrunszwikRouteLocationDto(ownerUuid), getGetyngaWithMissedAppointmentWindow(ownerUuid));
    }

    private RouteLocationDto getGetyngaWithMissedAppointmentWindow(UUID ownerUuid) {
        var result = getGetyngaRouteLocationDto(ownerUuid);
        result.getAvailability().add(RouteConst.getAvailability(routeDto.getStartDate().plusDays(5).toLocalDate()));
        return result;
    }

    private RouteLocationDto getStuttgartRouteLocationWithAppointmentWindow(UUID ownerUuid) {
        var result = getStuttgartRouteLocationDto(ownerUuid);
        result.getAvailability().add(RouteConst.getAvailability(routeDto.getStartDate().toLocalDate()));
        return result;
    }

    @Test
    void simulate_Should_ThrowAssertionError_When_LocationsListIsEmpty() {
        assertThrows(AssertionError.class, () -> RoutePermutationSimulation.simulate(routeDto, Collections.emptyList(), durations));
    }

    @Test
    void simulate_Should_ThrowAssertionError_When_LocationListSizeIsLowerThanTwo() {
        assertThrows(AssertionError.class, () -> RoutePermutationSimulation.simulate(routeDto,
                Collections.singletonList(RouteLocationConst.getBerlinRouteLocationDto(UUID.randomUUID())), durations));
    }

    @Test
    void getOrder_Should_ReturnSameOrderedLocationsListAsGiven_When_Called() {
        assertEquals(orderedRoute, routePermutationSimulation.getOrderedRoute());
    }

    @Test
    void getTravelledMeters_Should_ReturnLocationsCountPlusOneX100_When_Called() {
        assertEquals((orderedRoute.size() + 1) * 100, routePermutationSimulation.getTravelledMeters());
    }

    @Test
    void getEndingDate_Should_ReturnStartDatePlus4DaysAndPlus7Hours_When_Called() {
        assertEquals(routeDto.getStartDate().plusDays(4).plusHours(7), routePermutationSimulation.getEndingDate());
    }

    @Test
    void getRouteDto_Should_ReturnSameRouteDtoAsGivenOnCreation_When_Called() {
        assertEquals(routeDto, routePermutationSimulation.getRouteDto());
    }

    @Test
    void getMissedAppointmentsCount_Should_Return1BecauseGetyngaHasNotReachableAppointmentDate_When_Called() {
        assertEquals(1, routePermutationSimulation.getMissedAppointmentsCount());
    }

    @Test
    void missedAppointmentsList_Should_ContainGetyngaLocation_When_ItsAppointmentsDidNotFitToCurrentRouteOrder() {
        assertTrue(routePermutationSimulation.getMissedAppointments().contains(getGetyngaWithMissedAppointmentWindow(ownerUuid)));
    }

    @Test
    void missedAppointmentsList_Should_NotContainStuttgartLocation_When_StuttgartsAppointmentFitsToCurrentRouteOrder() {
        assertFalse(routePermutationSimulation.getMissedAppointments().contains(getStuttgartRouteLocationWithAppointmentWindow(ownerUuid)));
    }

}