package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

@AllArgsConstructor
public class RouteFacade {

    private final UserFacade userFacade;
    private final RouteFactory routeFactory;
    private final RouteRepositoryAdapter routeRepositoryAdapter;

    public Either<FailureResponse, NewRouteDto> create(String token, NewRouteDto newRouteDto) {
        // TODO refactor
        var tokenValidation = userFacade.validateAndGetUuidFromToken(token)
                .map(newRouteDto::setOwner);
        return tokenValidation.isRight() ? routeFactory.create(newRouteDto) : tokenValidation;
    }

    public Either<FailureResponse, RouteLocationEntity> addLocation(String token, UUID routeUuid,
                                                                    RouteLocationEntity routeLocationEntity) {
        var routeDaoOption = routeRepositoryAdapter.findByUuid(routeUuid)
                .map(RouteDao::get);
        if (routeDaoOption.isDefined()) {
            var routeDao = routeDaoOption.get();
            return userFacade.authorize(token, routeDao.getOwnerUuid(), routeLocationEntity)
                    .peek(routeLocation -> {
                        routeDao.addRouteLocation(routeLocation);
                        routeRepositoryAdapter.save(routeDao.get());
                    });
        }
        return Either.left(RouteFailureResponse.uuidNotFound(routeUuid));
    }
}
