package pl.tscript3r.tracciato.route.location;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@RequiredArgsConstructor
class RouteLocationSpringConfiguration {

    private final RouteFacade routeFacade;
    private final LocationFacade locationFacade;

    @Bean
    public RouteLocationFacade getRouteLocationFacade() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        DefaultValidator<RouteLocationDto> routeLocationValidator = new DefaultValidator<>(validator);
        return new RouteLocationFacade(routeFacade, locationFacade, routeLocationValidator);
    }

}
