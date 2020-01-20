package pl.tscript3r.tracciato.route;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.route.location.RouteLocationEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.UNAUTHORIZED_FAILURE;

@AllArgsConstructor
public class RouteFacade {

    private final UserFacade userFacade;
    private final RouteFactory routeFactory;
    private final LocationFacade locationFacade;
    private final RouteDao routeDao;

    public InternalResponse<RouteDto> create(String token, NewRouteDto newRouteDto) {
        return userFacade.validateAndGetUuidFromToken(token)
                .map(newRouteDto::setOwner)
                .flatMap(routeFactory::create);
    }

    public InternalResponse<RouteDto> addLocation(String token, UUID routeUuid,
                                                  RouteLocationEntity routeLocationEntity) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .flatMap(dto -> routeDao.addRouteLocation(routeUuid, routeLocationEntity));
    }

    private InternalResponse<RouteEntity> authorizeAndGetRouteEntity(String token, UUID routeUuid) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(dto -> userFacade.authorize(token, dto.getOwnerUuid()), UNAUTHORIZED_FAILURE);
    }

    public InternalResponse<RouteDto> setNewStartLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE)
                .flatMap(routeEntity -> locationFacade.addLocation(token, locationDto))
                .flatMap(locationEntity -> routeDao.setStartLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setExistingStartLocation(String token, UUID routeUuid, UUID locationUuid) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE)
                .flatMap(routeEntity -> locationFacade.getLocationEntityByUuid(locationUuid))
                .flatMap(locationEntity -> routeDao.setStartLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setNewEndLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE)
                .flatMap(routeEntity -> locationFacade.addLocation(token, locationDto))
                .flatMap(locationEntity -> routeDao.setEndLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setExistingEndLocation(String token, UUID routeUuid, UUID locationUuid) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE)
                .flatMap(routeEntity -> locationFacade.getLocationEntityByUuid(locationUuid))
                .flatMap(locationEntity -> routeDao.setEndLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> getRoute(String token, UUID routeUuid) {
        return routeDao.get(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE);
    }

    public InternalResponse<RouteDto> addAvailability(String token, UUID routeUuid, AvailabilityEntity availabilityEntity) {
        return routeDao.getEntity(routeUuid)
                .filterInternal(entity -> userFacade.authorize(token, entity.getOwnerUuid()), UNAUTHORIZED_FAILURE)
                .flatMap(entity -> routeDao.addAvailability(routeUuid, availabilityEntity));
    }

}
