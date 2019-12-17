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
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.RouteFacadeTest;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.Validation;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.tracciato.route.RouteConst.getValidNewRouteDto;
import static pl.tscript3r.tracciato.route.location.RouteLocationConst.getValidRouteLocationDtoWithNewLocation;

@DisplayName("Route location facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RouteLocationFacadeTest {

    @Mock
    UserFacade userFacade;

    RouteFacade routeFacade;

    RouteLocationFacade routeLocationFacade;

    NewRouteDto newRouteDto;

    @BeforeEach
    void setUp() {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var routeLocationValidator = new DefaultValidator<RouteLocationDto>(validator);
        newRouteDto = getValidNewRouteDto();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(Either.right(UUID.randomUUID()));
        routeFacade = RouteFacadeTest.getRouteFacade(userFacade);
        newRouteDto = routeFacade.create(any(), getValidNewRouteDto()).get();
        routeLocationFacade = new RouteLocationFacade(routeFacade, routeLocationValidator);
        when(userFacade.authorize(any(), any(), any())).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            return Either.right((RouteLocationEntity) args[args.length - 1]);
        });
    }

    @Test
    void add_Should_SuccessfullySaveNewRouteLocationEntity_When_NewRouteLocationDtoIsValid() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();

        // when
        var results = routeLocationFacade.add("mocked", newRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_AuthorizationFails() {
        // given
        var validRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        when(userFacade.authorize(any(), any(), any())).thenReturn(Either.left(GlobalFailureResponse.UNAUTHORIZED_ERROR));

        // when
        var results = routeLocationFacade.add("mocked", newRouteDto.getUuid(), validRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_ValidationFails() {
        // given
        var invalidRouteLocationDto = getValidRouteLocationDtoWithNewLocation();
        invalidRouteLocationDto.setLocation(null);

        // when
        var results = routeLocationFacade.add("mocked", newRouteDto.getUuid(), invalidRouteLocationDto);

        // then
        assertTrue(results.isLeft());
    }

}