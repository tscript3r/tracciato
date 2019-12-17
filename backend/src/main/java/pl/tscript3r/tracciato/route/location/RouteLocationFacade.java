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
        var validationResults = routeLocationValidator.validate(routeLocationDto)
                .map(RouteLocationMapper::map);
        if (validationResults.isRight())
            return routeFacade.addLocation(token, routeUuid, validationResults.get())
                    .map(RouteLocationMapper::map);
        return Either.left(validationResults.getLeft());
    }

}
