package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.route.location.RouteLocationConst;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Route facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RouteFacadeTest {

    @Mock
    UserFacade userFacade;

    RouteFacade routeFacade;

    RouteRepositoryAdapter routeRepositoryAdapter;

    final UUID userUuid = UUID.randomUUID();

    public static RouteFacade getRouteFacade(UserFacade userFacade, RouteRepositoryAdapter routeRepositoryAdapter) {
        return RouteSpringConfiguration.getInMemoryRouteFacade(userFacade, routeRepositoryAdapter);
    }

    public static RouteFacade getRouteFacade(UserFacade userFacade) {
        return RouteSpringConfiguration.getInMemoryRouteFacade(userFacade, new RouteInMemoryRepositoryAdapter());
    }

    @BeforeEach
    void setUp() {
        routeRepositoryAdapter = new RouteInMemoryRepositoryAdapter();
        routeFacade = getRouteFacade(userFacade, routeRepositoryAdapter);
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(Either.right(userUuid));
    }

    @Test
    void create_Should_CreateNewRoute_When_RouteDtoAndTokenAreValid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();

        // when
        var results = routeFacade.create("mocked", newRouteDto);

        // then
        assertTrue(results.isRight());
        assertNotNull(results.get().getUuid());
    }

    @Test
    void create_Should_RejectNewRoute_When_TokenIsInvalid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(Either.left(GlobalFailureResponse.INTERNAL_SERVER_ERROR));

        // when
        var results = routeFacade.create("mocked", newRouteDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void create_Should_RejectNewRoute_When_NewRouteIsInvalid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();
        newRouteDto.setStartDate(null);

        // when
        var results = routeFacade.create("mocked", newRouteDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void addLocation_Should_SaveNewRouteLocation_When_GivenLocationIsValid() {
        // given
        var routeLocationEntity = RouteLocationConst.getValidRouteLocationEntity();
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any(), any())).thenReturn(Either.right(routeLocationEntity));

        // when
        var results = routeFacade.addLocation("mocked", existingRoute.getUuid(), routeLocationEntity);

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertEquals(1, routeEntity.get().getLocations().size());
    }

    @Test
    void addLocation_Should_Fail_When_RouteUuidIsNotExisting() {
        // given
        var nonExistingUuid = UUID.randomUUID();

        // when
        var results = routeFacade.addLocation("mocked", nonExistingUuid, RouteLocationConst.getValidRouteLocationEntity());

        // then
        assertTrue(results.isLeft());
    }

}