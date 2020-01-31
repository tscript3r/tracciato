package pl.tscript3r.tracciato.route.arrange;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_ARRANGEMENT_MAPPING)
@RequiredArgsConstructor
class RouteArrangeSpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final RouteArrangeFacade routeArrangeFacade;

    @GetMapping(ROUTE_ARRANGEMENT_VALIDATION_MAPPING)
    public HttpEntity<?> validate(@RequestHeader(TOKEN_HEADER) String token,
                                  @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid) {
        return responseResolver.resolve(routeArrangeFacade.validate(token, routeUuid));
    }

}
