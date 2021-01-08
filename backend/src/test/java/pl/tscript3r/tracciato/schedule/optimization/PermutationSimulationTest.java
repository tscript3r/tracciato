package pl.tscript3r.tracciato.schedule.optimization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopConst;
import pl.tscript3r.tracciato.stop.StopDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.schedule.optimization.DurationsTest.getAllLocations;
import static pl.tscript3r.tracciato.stop.StopConst.*;

@DisplayName("Route permutation")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
public class PermutationSimulationTest {

    PermutationSimulation permutationSimulation;
    RouteDto routeDto;
    Durations durations;
    List<StopDto> orderedRoute;
    UUID ownerUuid = UUID.randomUUID();

    public static PermutationSimulation getRoutePermutationSimulation(UUID ownerUuid, RouteDto r) {
        r = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        var durations = Durations.get(new FakeDurationProvider(), getAllLocations(r));
        return PermutationSimulation.simulate(r, new ArrayList<>(r.getStops()), durations);
    }

    @BeforeEach
    void setUp() {
        routeDto = RouteConst.getValidRouteDto(ownerUuid, UUID.randomUUID());
        routeDto.setStops(getRouteLocationsSet(ownerUuid));
        durations = Durations.get(new FakeDurationProvider(), getAllLocations(routeDto));
        orderedRoute = new ArrayList<>(routeDto.getStops());
        permutationSimulation = PermutationSimulation.simulate(routeDto, new ArrayList<>(routeDto.getStops()), durations);
    }

    Set<StopDto> getRouteLocationsSet(UUID ownerUuid) {
        return new LinkedHashSet<>(getRouteLocationsList(ownerUuid)); // LinkedHashSet to not lose current order
    }

    List<StopDto> getRouteLocationsList(UUID ownerUuid) {
        return Arrays.asList(getStuttgartRouteLocationWithAppointmentWindow(ownerUuid), getBrunszwikStopDto(ownerUuid),
                getBrunszwikStopDto(ownerUuid), getGetyngaWithMissedAppointmentWindow(ownerUuid));
    }

    private StopDto getGetyngaWithMissedAppointmentWindow(UUID ownerUuid) {
        var result = getGetyngaStopDto(ownerUuid);
        result.getAvailability().add(RouteConst.getAvailability(routeDto.getStartDate().plusDays(5).toLocalDate()));
        return result;
    }

    private StopDto getStuttgartRouteLocationWithAppointmentWindow(UUID ownerUuid) {
        var result = getStuttgartStopDto(ownerUuid);
        result.getAvailability().add(RouteConst.getAvailability(routeDto.getStartDate().toLocalDate()));
        return result;
    }

    @Test
    void simulate_Should_ThrowAssertionError_When_LocationsListIsEmpty() {
        assertThrows(AssertionError.class, () -> PermutationSimulation.simulate(routeDto, Collections.emptyList(), durations));
    }

    @Test
    void simulate_Should_ThrowAssertionError_When_LocationListSizeIsLowerThanTwo() {
        assertThrows(AssertionError.class, () -> PermutationSimulation.simulate(routeDto,
                Collections.singletonList(StopConst.getBerlinStopDto(UUID.randomUUID())), durations));
    }

    @Test
    void getOrder_Should_ReturnSameOrderedLocationsListAsGiven_When_Called() {
        assertEquals(orderedRoute, permutationSimulation.getOrderedRoute());
    }

    @Test
    void getTravelledMeters_Should_ReturnLocationsCountPlusOneX100_When_Called() {
        assertEquals((orderedRoute.size() + 1) * 100, permutationSimulation.getTravelledMeters());
    }

    @Test
    void getEndingDate_Should_ReturnStartDatePlus4DaysAndPlus7Hours_When_Called() {
        assertEquals(routeDto.getStartDate().plusDays(4).plusHours(7), permutationSimulation.getEndingDate());
    }

    @Test
    void getRouteDto_Should_ReturnSameRouteDtoAsGivenOnCreation_When_Called() {
        assertEquals(routeDto, permutationSimulation.getRouteDto());
    }

    @Test
    void getMissedAppointmentsCount_Should_Return1BecauseGetyngaHasNotReachableAppointmentDate_When_Called() {
        assertEquals(1, permutationSimulation.getMissedAppointmentsCount());
    }

    @Test
    void missedAppointmentsList_Should_ContainGetyngaLocation_When_ItsAppointmentsDidNotFitToCurrentRouteOrder() {
        assertTrue(permutationSimulation.getMissedAppointments().contains(getGetyngaWithMissedAppointmentWindow(ownerUuid)));
    }

    @Test
    void missedAppointmentsList_Should_NotContainStuttgartLocation_When_StuttgartsAppointmentFitsToCurrentRouteOrder() {
        assertFalse(permutationSimulation.getMissedAppointments().contains(getStuttgartRouteLocationWithAppointmentWindow(ownerUuid)));
    }

}