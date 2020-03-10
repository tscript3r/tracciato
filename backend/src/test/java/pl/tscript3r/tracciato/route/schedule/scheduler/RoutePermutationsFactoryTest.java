package pl.tscript3r.tracciato.route.schedule.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.FakeDurationProvider;
import pl.tscript3r.tracciato.route.RouteConst;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Route permutations factory")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RoutePermutationsFactoryTest {

    FakeDurationProvider fakeDurationProvider;
    RoutePermutationsFactory routePermutationsFactory;

    @BeforeEach
    void setUp() {
        fakeDurationProvider = new FakeDurationProvider();
        routePermutationsFactory = new RoutePermutationsFactory(fakeDurationProvider);
    }

    @Test
    void get_Should_ReturnReadyToSimulateRoutePermutationsGroup_When_Called() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var routePermutationsGroup = routePermutationsFactory.get(routeDto);

        // then
        assertNotNull(routePermutationsGroup);
        assertEquals((routeDto.getLocations().size() + 1) * 4, fakeDurationProvider.travelDurationCallsCount);
    }

}