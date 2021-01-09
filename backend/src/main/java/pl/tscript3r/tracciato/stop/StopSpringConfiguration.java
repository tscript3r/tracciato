package pl.tscript3r.tracciato.stop;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.RouteFacade;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@RequiredArgsConstructor
class StopSpringConfiguration {

    private final RouteFacade routeFacade;
    private final LocationFacade locationFacade;

    @Bean
    public StopFacade getStopFacade() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        DefaultValidator<StopDto> routeLocationValidator = new DefaultValidator<>(validator);
        return new StopFacade(routeFacade, locationFacade, routeLocationValidator);
    }

}
