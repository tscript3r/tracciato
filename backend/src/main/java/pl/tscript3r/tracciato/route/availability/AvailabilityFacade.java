package pl.tscript3r.tracciato.route.availability;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;

import java.util.UUID;

@RequiredArgsConstructor
public class AvailabilityFacade {

    private final DefaultValidator<AvailabilityDto> validator;
    private final RouteFacade routeFacade;

    public InternalResponse<RouteDto> addAvailability(String token, UUID routeUuid, AvailabilityDto availabilityDto) {
        return validator.validate(availabilityDto)
                .map(AvailabilityMapper::map)
                .flatMap(availabilityEntity -> routeFacade.addAvailability(token, routeUuid, availabilityEntity));
    }

}
