package pl.tscript3r.tracciato.route;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.UNAUTHORIZED_FAILURE;

@AllArgsConstructor
public class RouteFacade {

    private final UserFacade userFacade;
    private final RouteFactory routeFactory;
    private final RouteRepositoryAdapter routeRepositoryAdapter;
    private final LocationFacade locationFacade;

    public InternalResponse<RouteDto> create(String token, NewRouteDto newRouteDto) {
        return userFacade.validateAndGetUuidFromToken(token)
                .map(newRouteDto::setOwner)
                .flatMap(routeFactory::create);
    }

    public InternalResponse<RouteDto> addLocation(String token, UUID routeUuid,
                                                  RouteLocationEntity routeLocationEntity) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .peek(routeDao -> {
                    routeDao.addRouteLocation(routeLocationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                })
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

    private InternalResponse<RouteDao> authorizeAndGetRouteDao(String token, UUID routeUuid) {
        return InternalResponse.fromOption(routeRepositoryAdapter.findByUuid(routeUuid),
                RouteFailureResponse.uuidNotFound(routeUuid))
                .filterOrElse(routeEntity -> userFacade.authorize(token, routeEntity.getOwnerUuid()), routeEntity -> UNAUTHORIZED_FAILURE)
                .map(RouteDao::get);
    }

    public InternalResponse<RouteDto> setStartLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .flatMap(routeDao -> assignNewStartLocation(token, routeDao, locationDto))
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

    private InternalResponse<RouteDao> assignNewStartLocation(String token, RouteDao routeDao, LocationDto locationDto) {
        return locationFacade.addLocation(token, locationDto)
                .map(locationEntity -> {
                    routeDao.setStartLocation(locationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                    return routeDao;
                });
    }

    public InternalResponse<RouteDto> setEndLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .flatMap(routeDao -> assignNewEndLocation(token, routeDao, locationDto))
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

    private InternalResponse<RouteDao> assignNewEndLocation(String token, RouteDao routeDao, LocationDto locationDto) {
        return locationFacade.addLocation(token, locationDto)
                .map(locationEntity -> {
                    routeDao.setEndLocation(locationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                    return routeDao;
                });
    }

    public InternalResponse<RouteDto> setStartLocation(String token, UUID routeUuid, UUID locationUuid) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .flatMap(routeDao -> assignExistingStartLocation(routeDao, locationUuid))
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

    private InternalResponse<RouteDao> assignExistingStartLocation(RouteDao routeDao, UUID locationUuid) {
        return locationFacade.getLocationEntityByUuid(locationUuid)
                .map(locationEntity -> {
                    routeDao.setStartLocation(locationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                    return routeDao;
                });
    }

    public InternalResponse<RouteDto> setEndLocation(String token, UUID routeUuid, UUID locationUuid) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .flatMap(routeDao -> assignExistingEndLocation(routeDao, locationUuid))
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

    private InternalResponse<RouteDao> assignExistingEndLocation(RouteDao routeDao, UUID locationUuid) {
        return locationFacade.getLocationEntityByUuid(locationUuid)
                .map(locationEntity -> {
                    routeDao.setEndLocation(locationEntity);
                    routeRepositoryAdapter.save(routeDao.get());
                    return routeDao;
                });
    }

    public InternalResponse<RouteDto> getRoute(String token, UUID routeUuid) {
        return authorizeAndGetRouteDao(token, routeUuid)
                .map(routeDao -> RouteMapper.map(routeDao.get()));
    }

}
