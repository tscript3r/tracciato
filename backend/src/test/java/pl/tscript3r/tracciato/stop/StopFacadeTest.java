package pl.tscript3r.tracciato.stop;

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
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import javax.validation.Validation;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_PREFIX;
import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;
import static pl.tscript3r.tracciato.route.RouteConst.getValidNewRouteDto;
import static pl.tscript3r.tracciato.stop.StopConst.getValidStopDtoWithNewLocation;
import static pl.tscript3r.tracciato.user.UserConst.JOHNS_USERNAME;
import static pl.tscript3r.tracciato.user.UserFacadeTest.getUserFacadeWithRegisteredJohn;

@DisplayName("Route location facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class StopFacadeTest {

    UserFacade userFacade;

    String token;

    LocationFacade locationFacade;

    RouteFacade routeFacade;

    StopFacade stopFacade;

    NewRouteDto newRouteDto;

    RouteDto createdRouteDto;


    @BeforeEach
    void setUp() {
        userFacade = getUserFacadeWithRegisteredJohn();
        token = TOKEN_PREFIX + userFacade.getToken(JOHNS_USERNAME).get();
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var routeLocationValidator = new DefaultValidator<StopDto>(validator);
        newRouteDto = getValidNewRouteDto();
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade, new LocationInMemoryRepositoryAdapter());
        routeFacade = RouteFacadeTest.getRouteFacade(userFacade, locationFacade);
        createdRouteDto = routeFacade.create(token, getValidNewRouteDto()).get();
        stopFacade = new StopFacade(routeFacade, locationFacade, routeLocationValidator);
    }

    @Test
    void add_Should_SuccessfullySaveNewStopEntity_When_StopDtoWithNewLocationIsValid() {
        // given
        var validRouteLocationDto = getValidStopDtoWithNewLocation();

        // when
        var results = stopFacade.add(token, createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_SuccessfullySaveStopEntity_When_StopDtoWithExistingLocationIsValid() {
        // given
        var validRouteLocationDto = getValidStopDtoWithNewLocation();
        validRouteLocationDto.setLocation(null);
        var validLocationDto = getValidLocationDto();
        var existingLocationUuid = locationFacade.addLocation(token, validLocationDto).get().getUuid();
        validRouteLocationDto.setExistingLocationUuid(existingLocationUuid);

        // when
        var results = stopFacade.add(token, createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_AuthorizationFails() {
        // given
        var validRouteLocationDto = getValidStopDtoWithNewLocation();

        // when
        var results = stopFacade.add("invalidToken", createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_ValidationFails() {
        // given
        var invalidRouteLocationDto = StopConst.getValidStopWithLocationUuid(null);

        // when
        var results = stopFacade.add(token, newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_StopWithNonExistingLocationUuidGiven() {
        // given
        var nonExistingLocationUuid = UUID.randomUUID();
        var validRouteLocationDto = StopConst.getValidStopWithLocationUuid(nonExistingLocationUuid);

        // when
        var results = stopFacade.add(token, newRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_StopHasNoNewLocationAndNotUuidGiven() {
        // given
        var invalidRouteLocationDto = getValidStopDtoWithNewLocation();
        invalidRouteLocationDto.setLocation(null);

        // when
        var result = stopFacade.add(token, newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(result.isLeft());
    }

}