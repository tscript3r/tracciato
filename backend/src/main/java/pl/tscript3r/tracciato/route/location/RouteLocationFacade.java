package pl.tscript3r.tracciato.route.location;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;

@AllArgsConstructor
class RouteLocationFacade {

    private final RouteFacade routeFacade;
    private final DefaultValidator<RouteLocationDto> routeLocationValidator;

    Either<FailureResponse, RouteLocationDto> add(String token, UUID routeUuid, RouteLocationDto routeLocationDto) {
        return routeLocationValidator.validate(routeLocationDto)
                .map(RouteLocationMapper::map)
                .flatMap(routeLocationEntity -> routeFacade.addLocation(token, routeUuid, routeLocationEntity))
                .map(RouteLocationMapper::map);
    }

}
