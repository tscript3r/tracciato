package pl.tscript3r.tracciato.stop;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;

import java.util.UUID;

@AllArgsConstructor
public class StopFacade {

    private final RouteFacade routeFacade;
    private final LocationFacade locationFacade;
    private final DefaultValidator<StopDto> routeLocationValidator;

    InternalResponse<RouteDto> add(String token, UUID routeUuid, StopDto stopDto) {
        return validateAndMap(token, routeUuid, stopDto);
    }

    private InternalResponse<RouteDto> validateAndMap(String token, UUID routeUuid, StopDto stopDto) {
        return routeLocationValidator.validate(stopDto)
                .map(StopMapper::map)
                .peek(stopEntity -> stopEntity.setUuid(UUID.randomUUID()))
                .flatMap(routeLocationEntity -> handleLocation(token, routeLocationEntity, stopDto))
                .flatMap(routeLocationEntity -> routeFacade.addStop(token, routeUuid, routeLocationEntity));
    }

    private InternalResponse<StopEntity> handleLocation(String token, StopEntity stopEntity,
                                                        StopDto stopDto) {
        if (stopDto.getLocation() != null)
            return saveNewStop(token, stopEntity, stopDto);
        if (stopDto.getExistingLocationUuid() != null)
            return assignExistingLocationIfNoNewGiven(stopEntity, stopDto);
        stopEntity.setUuid(UUID.randomUUID());
        return InternalResponse.failure(StopFailureResponse.existingOrNewLocationRequired());
    }

    private InternalResponse<StopEntity> saveNewStop(String token, StopEntity stopEntity,
                                                     StopDto stopDto) {
        return locationFacade.addLocation(token, stopDto.getLocation())
                .flatMap(locationDto -> locationFacade.getLocationEntityByUuid(locationDto.getUuid()))
                .peek(stopEntity::setLocation)
                .map(locationEntity -> stopEntity);
    }

    private InternalResponse<StopEntity> assignExistingLocationIfNoNewGiven(StopEntity stopEntity,
                                                                            StopDto stopDto) {
        return locationFacade.getLocationEntityByUuid(stopDto.getExistingLocationUuid())
                .map(locationEntity -> {
                    stopEntity.setLocation(locationEntity);
                    return stopEntity;
                });
    }

}
