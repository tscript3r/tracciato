package pl.tscript3r.tracciato.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.LocationInMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.location.LocationSpringConfiguration;
import pl.tscript3r.tracciato.route.availability.AvailabilityConst;
import pl.tscript3r.tracciato.route.location.RouteLocationConst;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.user.UserConst.*;
import static pl.tscript3r.tracciato.user.UserFacadeTest.getUserFacadeWithRegisteredJohn;

@DisplayName("Route facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
public class RouteFacadeTest {

    UserFacade userFacade;

    RouteFacade routeFacade;

    LocationFacade locationFacade;

    RouteRepositoryAdapter routeRepositoryAdapter;

    String token;

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
        userFacade = getUserFacadeWithRegisteredJohn();
        token = TOKEN_PREFIX + userFacade.getToken(JOHNS_USERNAME).get();
        routeRepositoryAdapter = new RouteInMemoryRepositoryAdapter();
        var inMemoryLocationRepositoryAdapter = new LocationInMemoryRepositoryAdapter();
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade, inMemoryLocationRepositoryAdapter);
        routeFacade = getRouteFacade(userFacade, routeRepositoryAdapter, locationFacade);
    }

    @Test
    void create_Should_CreateNewRoute_When_RouteDtoAndTokenAreValid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();

        // when
        var results = routeFacade.create(token, newRouteDto);

        // then
        assertTrue(results.isRight());
        assertNotNull(results.get().getUuid());
    }

    @Test
    void create_Should_RejectNewRoute_When_TokenIsInvalid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();

        // when
        var results = routeFacade.create("invalid", newRouteDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void create_Should_RejectNewRoute_When_NewRouteIsInvalid() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();
        newRouteDto.setStartDate(null);

        // when
        var results = routeFacade.create(token, newRouteDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void addLocation_Should_SaveNewRouteLocation_When_GivenLocationIsValid() {
        // given
        var routeLocationEntity = RouteLocationConst.getValidRouteLocationEntity();
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();

        // when
        var results = routeFacade.addLocation(token, existingRoute.getUuid(), routeLocationEntity);

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
        var results = routeFacade.addLocation(token, nonExistingUuid, RouteLocationConst.getValidRouteLocationEntity());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void setStartLocation_Should_SuccessfullySetNewStartLocation_When_ExistingRouteUuidGiven() {
        // given
        var locationDto = LocationConst.getValidLocationDto();
        var tmp = routeFacade.create(token, RouteConst.getValidNewRouteDto());
        var existingRoute = tmp.get();

        // when
        var results = routeFacade.setNewStartLocation(token, existingRoute.getUuid(), locationDto);

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getStartLocation());
    }

    @Test
    void setStartLocation_Should_SuccessfullySetExistingStartLocation_When_ExistingRouteUuidAndLocationUuidGiven() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();
        var addedLocation = locationFacade.addLocation(token, LocationConst.getValidLocationDto());

        // when
        var results = routeFacade.setExistingStartLocation(token, existingRoute.getUuid(), addedLocation.get().getUuid());

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getStartLocation());
    }

    @Test
    void setEndLocation_Should_SuccessfullySetNewEndLocation_When_ExistingRouteUuidGiven() {
        // given
        var locationDto = LocationConst.getValidLocationDto();
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();

        // when
        var results = routeFacade.setNewEndLocation(token, existingRoute.getUuid(), locationDto);

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getEndLocation());
    }


    @Test
    void setEndLocation_Should_SuccessfullySetExistingEndLocation_When_ExistingRouteUuidAndLocationUuidGiven() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();
        var addedLocation = locationFacade.addLocation(token, LocationConst.getValidLocationDto());

        // when
        var results = routeFacade.setExistingEndLocation(token, existingRoute.getUuid(), addedLocation.get().getUuid());

        // then
        assertTrue(results.isRight());
        var routeEntity = routeRepositoryAdapter.findByUuid(existingRoute.getUuid());
        assertNotNull(routeEntity.get().getEndLocation());
    }

    @Test
    void getRoute_Should_Fail_When_InvalidTokenIsGiven() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();

        // when
        var results = routeFacade.getRoute("invalid", existingRoute.getUuid());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_Fail_When_UserRequestsRouteWhichIsOwnedByOtherUser() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();
        userFacade.register(getValidEdyUserDto());
        var otherUsersToken = userFacade.getToken(EDY_USERNAME).get();

        // when
        var results = routeFacade.getRoute(TOKEN_PREFIX + otherUsersToken, existingRoute.getUuid());

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_Fail_When_NotExistingRouteUuidIsGiven() {
        // given
        var nonExistingRouteUuid = UUID.randomUUID();

        // when
        var results = routeFacade.getRoute(token, nonExistingRouteUuid);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getRoute_Should_SuccessfullyReturnRoute_When_ExistingRouteUuidIsGiven() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto()).get();

        // when
        var results = routeFacade.getRoute(token, existingRoute.getUuid());

        // then
        assertTrue(results.isRight());
    }

    @Test
    void addAvailability_Should_SuccessfullyAddAvailability_When_ExistingRouteUuidIsGiven() {
        // given
        var existingRoute = routeFacade.create(token, RouteConst.getValidNewRouteDto());
        var validAvailabilityEntity = AvailabilityConst.getValidAvailabilityEntity();

        // when
        var results = routeFacade.addAvailability(token, existingRoute.get().getUuid(), validAvailabilityEntity);

        // then
        assertTrue(results.isRight());
    }

}