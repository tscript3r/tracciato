package pl.tscript3r.tracciato.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.ReplaceCamelCaseAndUnderscores;

import javax.validation.Validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.tracciato.route.RouteConst.MAX_END_DATE;
import static pl.tscript3r.tracciato.route.RouteConst.START_DATE;

@DisplayName("Route validator")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class RouteValidatorTest {

    RouteValidator routeValidator;

    @BeforeEach
    void setUp() {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        routeValidator = new RouteValidator(validator);
    }

    @Test
    void validate_Should_ReturnRight_When_ValidNewRouteDtoPassed() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();

        // when
        var validationResults = routeValidator.validate(newRouteDto);

        // then
        assertTrue(validationResults.isRight());
    }

    @Test
    void validate_Should_ReturnValidationFailure_When_StartDateIsAfterEndDate() {
        // given
        var newRouteDto = RouteConst.getValidNewRouteDto();
        newRouteDto.setMaxEndDate(START_DATE);
        newRouteDto.setStartDate(MAX_END_DATE);

        // when
        var validationResults = routeValidator.validate(newRouteDto);

        // then
        assertTrue(validationResults.isLeft());
        assertFalse(validationResults.getLeft().getAdditionalFields().isEmpty());
    }

}