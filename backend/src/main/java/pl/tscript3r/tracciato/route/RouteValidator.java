package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.infrastructure.validator.AbstractValidator;
import pl.tscript3r.tracciato.route.api.NewRouteDto;

import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;

class RouteValidator extends AbstractValidator<NewRouteDto> {

    RouteValidator(Validator validator) {
        super(validator);
    }

    @Override
    protected Map<String, String> additionalConstraints(NewRouteDto newRouteDto) {
        final Map<String, String> bindingFails = new HashMap<>();
        if (newRouteDto.getStartDate() == null || newRouteDto.getMaxEndDate() == null ||
                !newRouteDto.getStartDate().isBefore(newRouteDto.getMaxEndDate()))
            bindingFails.put("route_duration", "Start date needs to be before max end date");
        return bindingFails;
    }

}
