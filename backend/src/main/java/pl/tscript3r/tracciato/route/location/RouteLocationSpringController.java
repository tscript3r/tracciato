package pl.tscript3r.tracciato.route.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_LOCATION_MAPPING)
@RequiredArgsConstructor
public class RouteLocationSpringController {

    private final ResponseResolver<ResponseEntity> responseResolver;
    private final RouteLocationFacade routeLocationFacade;

    @PostMapping
    public HttpEntity add(@RequestHeader(TOKEN_HEADER) String token, @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid,
                          @RequestBody RouteLocationDto routeLocationDto) {
        return responseResolver.resolve(routeLocationFacade.add(token, routeUuid, routeLocationDto),
                HttpStatus.CREATED.value());
    }

    @PostMapping(ROUTE_START_LOCATION_MAPPING)
    public HttpEntity setStartLocation(@RequestHeader(TOKEN_HEADER) String token,
                                       @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid,
                                       @RequestBody RouteLocationDto routeLocationDto) {
        return responseResolver.resolve(routeLocationFacade.setStartLocation(token, routeUuid, routeLocationDto),
                HttpStatus.CREATED.value());
    }

    @PostMapping(ROUTE_END_LOCATION_MAPPING)
    public HttpEntity setEndLocation(@RequestHeader(TOKEN_HEADER) String token,
                                     @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid,
                                     @RequestBody RouteLocationDto routeLocationDto) {
        return responseResolver.resolve(routeLocationFacade.setEndLocation(token, routeUuid, routeLocationDto),
                HttpStatus.CREATED.value());
    }

}
