package pl.tscript3r.tracciato.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.location.api.LocationDto;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.LOCATION_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@RestController
@RequestMapping(LOCATION_MAPPING)
@RequiredArgsConstructor
class LocationSpringController {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;
    private final LocationFacade locationFacade;

    @PostMapping
    public HttpEntity<?> add(@RequestHeader(TOKEN_HEADER) String token, @RequestBody LocationDto locationDto) {
        return responseResolver.resolve(locationFacade.addLocationAndMap(token, locationDto), HttpStatus.CREATED.value());
    }

    @GetMapping
    public HttpEntity<?> getAll(@RequestHeader(TOKEN_HEADER) String token) {
        return responseResolver.resolve(locationFacade.getAllLocationsFromUser(token));
    }

}
