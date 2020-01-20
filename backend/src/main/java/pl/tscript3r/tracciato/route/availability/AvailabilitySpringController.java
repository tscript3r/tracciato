package pl.tscript3r.tracciato.route.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_AVAILABILITY_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_UUID_VARIABLE;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(ROUTE_AVAILABILITY_MAPPING)
@RequiredArgsConstructor
class AvailabilitySpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final AvailabilityFacade availabilityFacade;

    @PostMapping
    public ResponseEntity<?> addAvailability(@RequestHeader(TOKEN_HEADER) String token,
                                             @PathVariable(ROUTE_UUID_VARIABLE) UUID routeUuid,
                                             @RequestBody AvailabilityDto availabilityDto) {
        return responseResolver.resolve(availabilityFacade.addAvailability(token, routeUuid, availabilityDto),
                HttpStatus.CREATED.value());
    }

}
