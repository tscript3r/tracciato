package pl.tscript3r.tracciato.route.arrange;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.arrange.validator.BeforeArrangeValidator;

import java.util.UUID;

@RequiredArgsConstructor
public class RouteArrangeFacade {

    private final RouteFacade routeFacade;

    public InternalResponse<RouteDto> validate(String token, UUID routeUuid) {
        return routeFacade.getRoute(token, routeUuid)
                .flatMap(BeforeArrangeValidator::validate);
    }

}
