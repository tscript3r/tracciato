package pl.tscript3r.tracciato.availability;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@RequiredArgsConstructor
class AvailabilitySpringConfiguration {

    private final RouteFacade routeFacade;

    @Bean
    public AvailabilityFacade getAvailabilityFacade() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        return new AvailabilityFacade(new DefaultValidator<>(validator), routeFacade);
    }

}
