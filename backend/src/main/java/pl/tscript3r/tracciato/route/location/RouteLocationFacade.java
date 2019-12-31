package pl.tscript3r.tracciato.route.location;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;
import java.util.function.Function;

@AllArgsConstructor
public class RouteLocationFacade {

    private final RouteFacade routeFacade;
    private final LocationFacade locationFacade;
    private final DefaultValidator<RouteLocationDto> routeLocationValidator;

    Either<FailureResponse, RouteLocationDto> add(String token, UUID routeUuid, RouteLocationDto routeLocationDto) {
        return validateAndMap(token, routeLocationDto,
                routeLocationEntity -> routeFacade.addLocation(token, routeUuid, routeLocationEntity));
    }

    private Either<FailureResponse, RouteLocationDto> validateAndMap(String token,
                                                                     RouteLocationDto routeLocationDto,
                                                                     Function<RouteLocationEntity, Either<FailureResponse,
                                                                             RouteLocationEntity>> mapper) {
        return routeLocationValidator.validate(routeLocationDto)
                .map(RouteLocationMapper::map)
                .flatMap(routeLocationEntity -> handleLocation(token, routeLocationEntity, routeLocationDto))
                .flatMap(mapper)
                .map(RouteLocationMapper::map);
    }

    private Either<FailureResponse, RouteLocationEntity> handleLocation(String token,
                                                                        RouteLocationEntity routeLocationEntity,
                                                                        RouteLocationDto routeLocationDto) {
        if (routeLocationDto.getLocation() != null)
            return saveNewLocation(token, routeLocationEntity, routeLocationDto);
        if (routeLocationDto.getExistingLocationUuid() != null)
            return assignExistingLocationIfNoNewGiven(routeLocationEntity, routeLocationDto);

        throw new IllegalArgumentException("Route location needs to be set as existing location uuid or new location");
    }

    private Either<FailureResponse, RouteLocationEntity> saveNewLocation(String token,
                                                                         RouteLocationEntity routeLocationEntity,
                                                                         RouteLocationDto routeLocationDto) {
        return locationFacade.addLocation(token, routeLocationDto.getLocation())
                .flatMap(locationDto -> locationFacade.getEntityByUuid(locationDto.getUuid()))
                .peek(routeLocationEntity::setLocation)
                .map(locationEntity -> routeLocationEntity);
    }

    private Either<FailureResponse, RouteLocationEntity> assignExistingLocationIfNoNewGiven(RouteLocationEntity routeLocationEntity,
                                                                                            RouteLocationDto routeLocationDto) {
        return locationFacade.getEntityByUuid(routeLocationDto.getExistingLocationUuid())
                .map(locationEntity -> {
                    routeLocationEntity.setLocation(locationEntity);
                    return routeLocationEntity;
                });
    }

    Either<FailureResponse, RouteLocationDto> setStartLocation(String token, UUID routeUuid,
                                                               RouteLocationDto routeLocationDto) {
        return validateAndMap(token, routeLocationDto,
                routeLocationEntity -> routeFacade.setStartLocation(token, routeUuid, routeLocationEntity));
    }

    Either<FailureResponse, RouteLocationDto> setEndLocation(String token, UUID routeUuid,
                                                             RouteLocationDto routeLocationDto) {
        return validateAndMap(token, routeLocationDto,
                routeLocationEntity -> routeFacade.setEndLocation(token, routeUuid, routeLocationEntity));
    }

}
