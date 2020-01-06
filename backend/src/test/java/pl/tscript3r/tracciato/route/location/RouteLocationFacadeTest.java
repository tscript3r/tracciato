package pl.tscript3r.tracciato.route.location;

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

import javax.validation.Validation;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;
import static pl.tscript3r.tracciato.route.RouteConst.getValidNewRouteDto;
import static pl.tscript3r.tracciato.route.location.RouteLocationConst.getValidRouteLocationDtoWithNewLocation;

@DisplayName("Route location facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RouteLocationFacadeTest {

    @Mock
    UserFacade userFacade;

    LocationFacade locationFacade;

    RouteFacade routeFacade;

    RouteLocationFacade routeLocationFacade;

    NewRouteDto newRouteDto;
    RouteDto createdRouteDto;

    @BeforeEach
    void setUp() {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var routeLocationValidator = new DefaultValidator<RouteLocationDto>(validator);
        newRouteDto = getValidNewRouteDto();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(Either.right(UUID.randomUUID()));
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade, new LocationInMemoryRepositoryAdapter());
        routeFacade = RouteFacadeTest.getRouteFacade(userFacade, locationFacade);
        createdRouteDto = routeFacade.create(any(), getValidNewRouteDto()).get();
        routeLocationFacade = new RouteLocationFacade(routeFacade, locationFacade, routeLocationValidator);
        when(userFacade.authorize(any(), any())).thenReturn(true);
    }

    @Test
    void add_Should_SuccessfullySaveNewRouteLocationEntity_When_RouteLocationDtoWithNewLocationIsValid() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();

        // when
        var results = routeLocationFacade.add("mocked", createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_SuccessfullySaveNewRouteLocationEntity_When_RouteLocationDtoWithExistingLocationIsValid() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        validRouteLocationDto.setLocation(null);
        var validLocationDto = getValidLocationDto();
        var existingLocationUuid = locationFacade.addLocation("mocked", validLocationDto).get().getUuid();
        validRouteLocationDto.setExistingLocationUuid(existingLocationUuid);

        // when
        var results = routeLocationFacade.add("mocked", createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_AuthorizationFails() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        when(userFacade.authorize(any(), any())).thenReturn(false);

        // when
        var results = routeLocationFacade.add("mocked", createdRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_ValidationFails() {
        // given
        var invalidRouteLocationDto = RouteLocationConst.getValidRouteLocationWithLocationUuid(null);

        // when
        var results = routeLocationFacade.add("mocked", newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_RouteLocationWithNonExistingLocationUuidGiven() {
        // given
        var nonExistingLocationUuid = UUID.randomUUID();
        var validRouteLocationDto = RouteLocationConst.getValidRouteLocationWithLocationUuid(nonExistingLocationUuid);

        // when
        var results = routeLocationFacade.add("mocked", newRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_RouteLocationHasNoNewLocationAndNotUuidGiven() {
        // given
        var invalidRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        invalidRouteLocationDto.setLocation(null);

        // when
        var result = routeLocationFacade.add("mocked", newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(result.isLeft());
    }

}