package pl.tscript3r.tracciato.infrastructure.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeBeforeAfterValidator implements ConstraintValidator<TimeBeforeAfter, Object>, FieldValueExtension {

    private String beforeField;
    private String afterField;
    private String switchField;
    private boolean enableSwitch;
    private boolean enabledSwitchValue;
    private String message;

    @Override
    public void initialize(TimeBeforeAfter constraintAnnotation) {
        beforeField = constraintAnnotation.beforeField();
        afterField = constraintAnnotation.afterField();
        switchField = constraintAnnotation.switchField();
        enableSwitch = constraintAnnotation.switchEnableValue();
        enabledSwitchValue = constraintAnnotation.enabledSwitchValue();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return isTimeChronology(value, context) || checkSwitchBoolean(value);
    }

    private boolean checkSwitchBoolean(Object value) {
        if (enableSwitch) {
            Object switchFieldValue;
            switchFieldValue = getFieldValue(value, switchField);
            if (switchFieldValue instanceof Boolean)
                return (Boolean) switchFieldValue == enabledSwitchValue;
            else
                throw new IllegalArgumentException("Switch field needs to be instance of Boolean");
        }
        return false;
    }

    private boolean isTimeChronology(Object value, ConstraintValidatorContext context) {
        Object beforeFieldValue;
        Object afterFieldValue;
        beforeFieldValue = getFieldValue(value, beforeField);
        afterFieldValue = getFieldValue(value, afterField);
        if (beforeFieldValue == null || afterFieldValue == null)
            return false;

        if (beforeFieldValue instanceof LocalTime && afterFieldValue instanceof LocalTime) {
            LocalTime before = (LocalTime) beforeFieldValue;
            LocalTime after = (LocalTime) afterFieldValue;
            if (!before.isBefore(after)) {
                addCustomContextConstraintViolation(context);
                return false;
            }
            return true;
        }
        if (beforeFieldValue instanceof LocalDateTime && afterFieldValue instanceof LocalDateTime) {
            LocalDateTime before = (LocalDateTime) beforeFieldValue;
            LocalDateTime after = (LocalDateTime) afterFieldValue;
            if (!before.isBefore(after)) {
                addCustomContextConstraintViolation(context);
                return false;
            }
            return true;
        }
        throw new IllegalArgumentException("Before and after fields needs to be instance of LocalTime or LocalDateTime");
    }

    private void addCustomContextConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        var customContext = context.buildConstraintViolationWithTemplate(message);
        customContext.addPropertyNode(beforeField)
                .addPropertyNode(afterField)
                .addConstraintViolation();
    }

}
