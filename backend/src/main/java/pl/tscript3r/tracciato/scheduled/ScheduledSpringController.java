package pl.tscript3r.tracciato.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_SCHEDULED_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_SCHEDULED_MAPPING)
@RequiredArgsConstructor
public class ScheduledSpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final ScheduledFacade scheduledFacade;

    @GetMapping("{uuid}")
    public HttpEntity<?> get(@RequestHeader(TOKEN_HEADER) String token,
                             @PathVariable("uuid") UUID scheduleUuid) {
        return responseResolver.resolve(scheduledFacade.getScheduledResults(token, scheduleUuid));
    }

}
