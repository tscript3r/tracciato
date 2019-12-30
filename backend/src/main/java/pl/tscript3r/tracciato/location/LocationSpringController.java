package pl.tscript3r.tracciato.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.LOCATION_MAPPING;

@RestController
@RequestMapping(LOCATION_MAPPING)
@RequiredArgsConstructor
public class LocationSpringController {

    // get, update, delete users saved locations

}
