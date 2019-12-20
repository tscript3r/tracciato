package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.UNAUTHORIZED_ERROR;

@AllArgsConstructor
public class RouteFacade {

    private final UserFacade userFacade;
    private final RouteFactory routeFactory;
    private final RouteRepositoryAdapter routeRepositoryAdapter;

    public Either<FailureResponse, NewRouteDto> create(String token, NewRouteDto newRouteDto) {
        return userFacade.validateAndGetUuidFromToken(token)
                .map(newRouteDto::setOwner)
                .flatMap(routeFactory::create);
    }

    public Either<FailureResponse, RouteLocationEntity> addLocation(String token, UUID routeUuid,
                                                                    RouteLocationEntity routeLocationEntity) {
        return routeRepositoryAdapter.findByUuid(routeUuid)
                .toEither(RouteFailureResponse.uuidNotFound(routeUuid))
                .filterOrElse(routeEntity -> userFacade.authorize(token, routeEntity.getOwnerUuid()), routeEntity -> UNAUTHORIZED_ERROR)
                .map(RouteDao::get)
                .peek(routeDao -> {
                    routeDao.addRouteLocation(routeLocationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                })
                .map(routeDao -> routeLocationEntity);
    }

}
