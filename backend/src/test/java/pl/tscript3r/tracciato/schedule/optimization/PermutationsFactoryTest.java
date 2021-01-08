package pl.tscript3r.tracciato.schedule.optimization;

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
class PermutationsFactoryTest {

    FakeDurationProvider fakeDurationProvider;
    PermutationsFactory permutationsFactory;

    @BeforeEach
    void setUp() {
        fakeDurationProvider = new FakeDurationProvider();
        permutationsFactory = new PermutationsFactory(fakeDurationProvider);
    }

    @Test
    void get_Should_ReturnReadyToSimulateRoutePermutationsGroup_When_Called() {
        // given
        var routeDto = RouteConst.getValidRouteDto(UUID.randomUUID(), UUID.randomUUID());

        // when
        var routePermutationsGroup = permutationsFactory.get(routeDto);

        // then
        assertNotNull(routePermutationsGroup);
        assertEquals((routeDto.getStops().size() + 1) * 4, fakeDurationProvider.travelDurationCallsCount);
    }

}