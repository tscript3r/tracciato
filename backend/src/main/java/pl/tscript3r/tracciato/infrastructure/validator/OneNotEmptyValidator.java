package pl.tscript3r.tracciato.infrastructure.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OneNotEmptyValidator implements ConstraintValidator<OneNotEmpty, Object>, FieldValueExtension {

    private String[] fields;
    private String message;

    @Override
    public void initialize(OneNotEmpty constraintAnnotation) {
        fields = constraintAnnotation.fields();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return onInvalidCustomMessage(Arrays.stream(fields)
                .filter(s -> getFieldValue(value, s) != null)
                .count() == 1, context);
    }

    private boolean onInvalidCustomMessage(boolean validationResult, ConstraintValidatorContext context) {
        if (!validationResult) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fields[0])
                    .addConstraintViolation();
        }
        return validationResult;
    }

}
