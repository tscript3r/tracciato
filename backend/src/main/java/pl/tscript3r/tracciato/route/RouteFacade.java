package pl.tscript3r.tracciato.route;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.schedule.optimization.TracciatoSchedulerException;
import pl.tscript3r.tracciato.stop.StopEntity;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.UUID;

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

    public InternalResponse<RouteDto> addStop(String token, UUID routeUuid,
                                              StopEntity stopEntity) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(routeDao.addStop(routeUuid, stopEntity));
    }

    private InternalResponse<RouteEntity> authorizeAndGetRouteEntity(String token, UUID routeUuid) {
        return routeDao.getEntity(routeUuid)
                .flatMap(entity -> userFacade.authorize(token, entity.getOwnerUuid(), entity));
    }

    public InternalResponse<RouteDto> setNewStartLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(locationFacade.addLocation(token, locationDto))
                .flatMap(locationEntity -> routeDao.setStartLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setExistingStartLocation(String token, UUID routeUuid, UUID locationUuid) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(locationFacade.getLocationEntityByUuid(locationUuid))
                .flatMap(locationEntity -> routeDao.setStartLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setNewEndLocation(String token, UUID routeUuid, LocationDto locationDto) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(locationFacade.addLocation(token, locationDto))
                .flatMap(locationEntity -> routeDao.setEndLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> setExistingEndLocation(String token, UUID routeUuid, UUID locationUuid) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(locationFacade.getLocationEntityByUuid(locationUuid))
                .flatMap(locationEntity -> routeDao.setEndLocation(routeUuid, locationEntity));
    }

    public InternalResponse<RouteDto> getRoute(String token, UUID routeUuid) {
        return routeDao.get(routeUuid)
                .flatMap(dto -> userFacade.authorize(token, dto.getOwnerUuid(), dto));
    }

    public InternalResponse<RouteDto> addAvailability(String token, UUID routeUuid, AvailabilityEntity availabilityEntity) {
        return authorizeAndGetRouteEntity(token, routeUuid)
                .override(routeDao.addAvailability(routeUuid, availabilityEntity));
    }

    public RouteEntity getByUuid(UUID uuid) {
        return routeDao.getEntity(uuid)
                .getOrElseThrow(() -> new TracciatoSchedulerException("Route with UUID: " + uuid.toString() + " not found"));
    }

}
