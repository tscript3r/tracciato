package pl.tscript3r.tracciato.route;

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
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.LocationInMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.location.LocationSpringConfiguration;
import pl.tscript3r.tracciato.route.location.RouteLocationConst;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.UserFailureResponse;

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

    LocationFacade locationFacade;

    RouteRepositoryAdapter routeRepositoryAdapter;

    final UUID userUuid = UUID.randomUUID();

    public static RouteFacade getRouteFacade(UserFacade userFacade, RouteRepositoryAdapter routeRepositoryAdapter,
                                             LocationFacade locationFacade) {
        return RouteSpringConfiguration.getInMemoryRouteFacade(userFacade, routeRepositoryAdapter, locationFacade);
    }

    public static RouteFacade getRouteFacade(UserFacade userFacade, LocationFacade locationFacade) {
        return RouteSpringConfiguration.getInMemoryRouteFacade(userFacade, new RouteInMemoryRepositoryAdapter(),
                locationFacade);
    }

    @BeforeEach
    void setUp() {
        routeRepositoryAdapter = new RouteInMemoryRepositoryAdapter();
        var inMemoryLocationRepositoryAdapter = new LocationInMemoryRepositoryAdapter();
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade, inMemoryLocationRepositoryAdapter);
        routeFacade = getRouteFacade(userFacade, routeRepositoryAdapter, locationFacade);
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.payload(userUuid));
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
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.failure(GlobalFailureResponse.INTERNAL_SERVER_ERROR));

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
        when(userFacade.authorize(any(), any())).thenReturn(true);

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

    @Test
    void setStartLocation_Should_SuccessfullySetNewStartLocation_When_ExistingRouteUuidGiven() {
        // given
        var locationDto = LocationConst.getValidLocationDto();
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(true);

        // when
        var results = routeFacade.setStartLocation("mocked", existingRoute.getUuid(), locationDto);

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getStartLocation());
    }

    @Test
    void setStartLocation_Should_SuccessfullySetExistingStartLocation_When_ExistingRouteUuidAndLocationUuidGiven() {
        // given
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(true);
        var userUuid = UUID.randomUUID();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.payload(userUuid));
        var addedLocation = locationFacade.addLocation("mocked", LocationConst.getValidLocationDto());

        // when
        var results = routeFacade.setStartLocation("mocked", existingRoute.getUuid(), addedLocation.get().getUuid());

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getStartLocation());
    }

    @Test
    void setEndLocation_Should_SuccessfullySetNewEndLocation_When_ExistingRouteUuidGiven() {
        // given
        var locationDto = LocationConst.getValidLocationDto();
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(true);

        // when
        var results = routeFacade.setEndLocation("mocked", existingRoute.getUuid(), locationDto);

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getEndLocation());
    }


    @Test
    void setEndLocation_Should_SuccessfullySetExistingEndLocation_When_ExistingRouteUuidAndLocationUuidGiven() {
        // given
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(true);
        var userUuid = UUID.randomUUID();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.payload(userUuid));
        var addedLocation = locationFacade.addLocation("mocked", LocationConst.getValidLocationDto());

        // when
        var results = routeFacade.setEndLocation("mocked", existingRoute.getUuid(), addedLocation.get().getUuid());

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getEndLocation());
    }

    @Test
    void getRoute_Should_Fail_When_InvalidTokenIsGiven() {
        // given
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.failure(UserFailureResponse.invalidCredentials()));

        // when
        var results = routeFacade.getRoute("mocked", existingRoute.getUuid());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_Fail_When_UserRequestsRouteWhichIsOwnedByOtherUser() {
        // given
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(false);

        // when
        var results = routeFacade.getRoute("mocked", existingRoute.getUuid());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_Fail_When_NotExistingRouteUuidIsGiven() {
        // given
        var nonExistingRouteUuid = UUID.randomUUID();

        // when
        var results = routeFacade.getRoute("mocked", nonExistingRouteUuid);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_SuccessfullyReturnRoute_When_ExistingRouteUuidIsGiven() {
        // given
        var existingRoute = routeFacade.create("mocked", RouteConst.getValidNewRouteDto()).get();
        when(userFacade.authorize(any(), any())).thenReturn(true);

        // when
        var results = routeFacade.getRoute("mocked", existingRoute.getUuid());

        // then
        assertTrue(results.isRight());
    }

}