package pl.tscript3r.tracciato.route.location;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;
import java.util.function.Function;

@AllArgsConstructor
public class RouteLocationFacade {

    private final RouteFacade routeFacade;
    private final DefaultValidator<RouteLocationDto> routeLocationValidator;

    Either<FailureResponse, RouteLocationDto> add(String token, UUID routeUuid, RouteLocationDto routeLocationDto) {
        return validateMapAndFlatMap(routeLocationDto,
                routeLocationEntity -> routeFacade.addLocation(token, routeUuid, routeLocationEntity));
    }

    private Either<FailureResponse, RouteLocationDto> validateMapAndFlatMap(RouteLocationDto routeLocationDto,
                                                                            Function<? super RouteLocationEntity,
                                                                                    ? extends Either<FailureResponse,
                                                                                            ? extends RouteLocationEntity>> mapper) {
        return routeLocationValidator.validate(routeLocationDto)
                .map(RouteLocationMapper::map)
                .flatMap(mapper)
                .map(RouteLocationMapper::map);
    }

    Either<FailureResponse, RouteLocationDto> setStartLocation(String token, UUID routeUuid,
                                                               RouteLocationDto routeLocationDto) {
        return validateMapAndFlatMap(routeLocationDto,
                routeLocationEntity -> routeFacade.setStartLocation(token, routeUuid, routeLocationEntity));
    }

    Either<FailureResponse, RouteLocationDto> setEndLocation(String token, UUID routeUuid,
                                                             RouteLocationDto routeLocationDto) {
        return validateMapAndFlatMap(routeLocationDto,
                routeLocationEntity -> routeFacade.setEndLocation(token, routeUuid, routeLocationEntity));
    }

}
