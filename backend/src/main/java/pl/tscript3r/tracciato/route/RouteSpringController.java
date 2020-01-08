package pl.tscript3r.tracciato.route;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.route.api.NewRouteDto;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_UUID_VARIABLE;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_MAPPING)
@RequiredArgsConstructor
public class RouteSpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final RouteFacade routeFacade;

    @PostMapping
    public HttpEntity<?> createRoute(@RequestHeader(TOKEN_HEADER) String token, @RequestBody NewRouteDto newRouteDto) {
        return responseResolver.resolve(routeFacade.create(token, newRouteDto), HttpStatus.CREATED.value());
    }

    @GetMapping("{" + ROUTE_UUID_VARIABLE + "}")
    public HttpEntity<?> getRoute(@RequestHeader(TOKEN_HEADER) String token, @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid) {
        return responseResolver.resolve(routeFacade.getRoute(token, routeUuid));
    }

}
