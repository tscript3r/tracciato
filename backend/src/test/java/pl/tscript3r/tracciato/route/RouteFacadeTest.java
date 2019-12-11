package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Route facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
class RouteFacadeTest {

    @Mock
    UserFacade userFacade;

    RouteFacade routeFacade;

    RouteRepositoryAdapter routeRepositoryAdapter;

    final UUID userUuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        routeRepositoryAdapter = new RouteInMemoryRepositoryAdapter();
        routeFacade = RouteSpringConfiguration.getInMemoryRouteFacade(userFacade, routeRepositoryAdapter);
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

}