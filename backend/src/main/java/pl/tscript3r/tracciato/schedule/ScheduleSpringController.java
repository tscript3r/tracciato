package pl.tscript3r.tracciato.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.*;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_SCHEDULE_MAPPING)
@RequiredArgsConstructor
class ScheduleSpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final ScheduleFacade scheduleFacade;

    @PostMapping(ROUTE_SCHEDULE_VALIDATION_MAPPING)
    public HttpEntity<?> validate(@RequestHeader(TOKEN_HEADER) String token,
                                  @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid) {
        return responseResolver.resolve(scheduleFacade.validate(token, routeUuid));
    }

    @PostMapping
    public HttpEntity<?> schedule(@RequestHeader(TOKEN_HEADER) String token,
                                  @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid,
                                  @RequestParam(required = false, defaultValue = "true", name = "wait") Boolean await) {
        return responseResolver.resolve(scheduleFacade.schedule(token, routeUuid, await));
    }

}
