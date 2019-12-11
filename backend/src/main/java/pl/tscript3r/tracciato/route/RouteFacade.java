package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.user.UserFacade;

@AllArgsConstructor
public class RouteFacade {

    private final UserFacade userFacade;
    private final RouteFactory routeFactory;

    public Either<FailureResponse, NewRouteDto> create(String token, NewRouteDto newRouteDto) {
        // TODO refactor
        var tokenValidation = userFacade.validateAndGetUuidFromToken(token)
                .map(newRouteDto::setOwner);
        if (tokenValidation.isRight())
            return routeFactory.create(newRouteDto);
        return tokenValidation;
    }

}
