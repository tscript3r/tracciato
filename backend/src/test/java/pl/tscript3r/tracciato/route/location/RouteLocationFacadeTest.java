package pl.tscript3r.tracciato.route.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.LocationInMemoryRepositoryAdapter;
import pl.tscript3r.tracciato.location.LocationSpringConfiguration;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.RouteFacadeTest;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import javax.validation.Validation;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;
import static pl.tscript3r.tracciato.route.RouteConst.getValidNewRouteDto;
import static pl.tscript3r.tracciato.route.location.RouteLocationConst.getValidRouteLocationDtoWithNewLocation;
import static pl.tscript3r.tracciato.user.UserConst.JOHNS_USERNAME;
import static pl.tscript3r.tracciato.user.UserFacadeTest.getUserFacadeWithRegisteredJohn;

@DisplayName("Route location facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteLocationFacadeTest {

    UserFacade userFacade;

    String token;

    LocationFacade locationFacade;

    RouteFacade routeFacade;

    RouteLocationFacade routeLocationFacade;

    NewRouteDto newRouteDto;

    RouteDto createdRouteDto;


    @BeforeEach
    void setUp() {
        userFacade = getUserFacadeWithRegisteredJohn();
        token = TOKEN_PREFIX + userFacade.getToken(JOHNS_USERNAME).get();
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var routeLocationValidator = new DefaultValidator<RouteLocationDto>(validator);
        newRouteDto = getValidNewRouteDto();
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade, new LocationInMemoryRepositoryAdapter());
        routeFacade = RouteFacadeTest.getRouteFacade(userFacade, locationFacade);
        createdRouteDto = routeFacade.create(token, getValidNewRouteDto()).get();
        routeLocationFacade = new RouteLocationFacade(routeFacade, locationFacade, routeLocationValidator);
    }

    @Test
    void add_Should_SuccessfullySaveNewRouteLocationEntity_When_RouteLocationDtoWithNewLocationIsValid() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();

        // when
        var results = routeLocationFacade.add(token, createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_SuccessfullySaveNewRouteLocationEntity_When_RouteLocationDtoWithExistingLocationIsValid() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        validRouteLocationDto.setLocation(null);
        var validLocationDto = getValidLocationDto();
        var existingLocationUuid = locationFacade.addLocation(token, validLocationDto).get().getUuid();
        validRouteLocationDto.setExistingLocationUuid(existingLocationUuid);

        // when
        var results = routeLocationFacade.add(token, createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_AuthorizationFails() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();

        // when
        var results = routeLocationFacade.add("invalidToken", createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_ValidationFails() {
        // given
        var invalidRouteLocationDto = RouteLocationConst.getValidRouteLocationWithLocationUuid(null);

        // when
        var results = routeLocationFacade.add(token, newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_RouteLocationWithNonExistingLocationUuidGiven() {
        // given
        var nonExistingLocationUuid = UUID.randomUUID();
        var validRouteLocationDto = RouteLocationConst.getValidRouteLocationWithLocationUuid(nonExistingLocationUuid);

        // when
        var results = routeLocationFacade.add(token, newRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_RouteLocationHasNoNewLocationAndNotUuidGiven() {
        // given
        var invalidRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        invalidRouteLocationDto.setLocation(null);

        // when
        var result = routeLocationFacade.add(token, newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(result.isLeft());
    }

}