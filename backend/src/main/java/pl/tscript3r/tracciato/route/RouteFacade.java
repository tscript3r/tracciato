package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;
import java.util.function.Consumer;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.UNAUTHORIZED_FAILURE;

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
        return authorizeAndGetRouteDao(token, routeUuid, routeLocationEntity, routeDao -> {
            routeDao.addRouteLocation(routeLocationEntity);
            routeRepositoryAdapter.save(routeDao.get());
        });
    }

    private Either<FailureResponse, RouteLocationEntity> authorizeAndGetRouteDao(String token, UUID routeUuid,
                                                                                 RouteLocationEntity routeLocationEntity,
                                                                                 Consumer<? super RouteDao> peek) {
        return routeRepositoryAdapter.findByUuid(routeUuid)
                .toEither(RouteFailureResponse.uuidNotFound(routeUuid))
                .filterOrElse(routeEntity -> userFacade.authorize(token, routeEntity.getOwnerUuid()), routeEntity -> UNAUTHORIZED_FAILURE)
                .map(RouteDao::get)
                .peek(peek)
                .map(routeDao -> routeLocationEntity);
    }

    public Either<FailureResponse, RouteLocationEntity> setStartLocation(String token, UUID routeUuid,
                                                                         RouteLocationEntity routeLocationEntity) {
        return authorizeAndGetRouteDao(token, routeUuid, routeLocationEntity, routeDao -> {
            routeDao.setStartLocation(routeLocationEntity);
            routeRepositoryAdapter.save(routeDao.get());
        });
    }

    public Either<FailureResponse, RouteLocationEntity> setEndLocation(String token, UUID routeUuid,
                                                                       RouteLocationEntity routeLocationEntity) {
        return authorizeAndGetRouteDao(token, routeUuid, routeLocationEntity, routeDao -> {
            routeDao.setEndLocation(routeLocationEntity);
            routeRepositoryAdapter.save(routeDao.get());
        });
    }

}
