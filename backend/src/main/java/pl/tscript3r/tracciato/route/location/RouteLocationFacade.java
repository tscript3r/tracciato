package pl.tscript3r.tracciato.route.location;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;

@AllArgsConstructor
public class RouteLocationFacade {

    private final RouteFacade routeFacade;
    private final LocationFacade locationFacade;
    private final DefaultValidator<RouteLocationDto> routeLocationValidator;

    InternalResponse<RouteDto> add(String token, UUID routeUuid, RouteLocationDto routeLocationDto) {
        return validateAndMap(token, routeUuid, routeLocationDto);
    }

    private InternalResponse<RouteDto> validateAndMap(String token, UUID routeUuid, RouteLocationDto routeLocationDto) {
        return routeLocationValidator.validate(routeLocationDto)
                .map(RouteLocationMapper::map)
                .flatMap(routeLocationEntity -> handleLocation(token, routeLocationEntity, routeLocationDto))
                .flatMap(routeLocationEntity -> routeFacade.addLocation(token, routeUuid, routeLocationEntity));
    }

    private InternalResponse<RouteLocationEntity> handleLocation(String token, RouteLocationEntity routeLocationEntity,
                                                                 RouteLocationDto routeLocationDto) {
        if (routeLocationDto.getLocation() != null)
            return saveNewLocation(token, routeLocationEntity, routeLocationDto);
        if (routeLocationDto.getExistingLocationUuid() != null)
            return assignExistingLocationIfNoNewGiven(routeLocationEntity, routeLocationDto);
        routeLocationEntity.setUuid(UUID.randomUUID());
        throw new IllegalArgumentException("Route location needs to be set as existing location uuid or new location");
    }

    private InternalResponse<RouteLocationEntity> saveNewLocation(String token, RouteLocationEntity routeLocationEntity,
                                                                  RouteLocationDto routeLocationDto) {
        return locationFacade.addLocation(token, routeLocationDto.getLocation())
                .flatMap(locationDto -> locationFacade.getLocationEntityByUuid(locationDto.getUuid()))
                .peek(routeLocationEntity::setLocation)
                .map(locationEntity -> routeLocationEntity);
    }

    private InternalResponse<RouteLocationEntity> assignExistingLocationIfNoNewGiven(RouteLocationEntity routeLocationEntity,
                                                                                     RouteLocationDto routeLocationDto) {
        return locationFacade.getLocationEntityByUuid(routeLocationDto.getExistingLocationUuid())
                .map(locationEntity -> {
                    routeLocationEntity.setLocation(locationEntity);
                    return routeLocationEntity;
                });
    }

}
