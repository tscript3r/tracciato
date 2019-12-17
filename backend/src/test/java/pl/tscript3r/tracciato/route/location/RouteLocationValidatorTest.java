package pl.tscript3r.tracciato.route.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.RouteFacadeTest;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.Validation;

@DisplayName("Route location validator")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
class RouteLocationValidatorTest {

    @BeforeEach
    void setUp() {

    }

}
