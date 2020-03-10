package pl.tscript3r.tracciato.route.schedule.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.duration.provider.LocationDto2StringLocation;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Durations")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
class DurationsTest {

    Durations durations;

    FakeDurationProvider durationProvider;

    List<LocationDto> locations;

    static List<LocationDto> getAllLocations(RouteDto routeDto) {
        var results = new ArrayList<LocationDto>();
        results.add(routeDto.getStartLocation());
        results.add(routeDto.getEndLocation());
        results.addAll(routeDto.getLocations()
                .stream()
                .map(RouteLocationDto::getLocation)
                .collect(Collectors.toSet()));
        return results;
    }

    @BeforeEach
    void setUp() {
        locations = LocationConst.getLocationsList();
        durationProvider = new FakeDurationProvider();
        durations = Durations.get(durationProvider, locations);
    }

    @Test
    void get_Should_RequestUnidirectionalDurationsBetweenAllLocations_When_Called() {
        // then
        // locations count == durations requests by unidirectional approach
        assertEquals(locations.size(), durationProvider.travelDurationCallsCount);
    }

    @Test
    void getDuration_Should_ThrowNotFoundException_When_GivenLocationsAreNotOnInitialList() {
        // given
        var fromLocation = LocationConst.getStuttgartLocationDto();
        var toLocation = LocationConst.getWarsawLocationDto();

        // when, then
        assertThrows(TracciatoSchedulerException.class, () -> durations.getDuration(fromLocation, toLocation));
    }

    @Test
    void getDuration_Should_ReturnDuration_When_GivenLocationsWereAddedToInitialList() {
        // given
        var locationIterator = locations.iterator();
        var from = locationIterator.next();
        var to = locationIterator.next();

        // when
        var results = durations.getDuration(from, to);

        // then
        assertNotNull(results);
    }

    @Test
    void getDuration_Should_ThrowNestedExceptionInFuture_When_ExceptionInsideAppeared() {
        // given
        var locationIterator = locations.iterator();
        var from = locationIterator.next();
        var to = locationIterator.next();
        durationProvider.setCallable(LocationDto2StringLocation.map(from),
                LocationDto2StringLocation.map(to),
                () -> {
                    throw new RuntimeException("");
                });
        durations = Durations.get(durationProvider, locations);

        // when, then
        assertThrows(TracciatoSchedulerException.class, () -> durations.getDuration(from, to));
    }

}