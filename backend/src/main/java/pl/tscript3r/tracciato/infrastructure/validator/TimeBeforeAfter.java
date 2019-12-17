package pl.tscript3r.tracciato.infrastructure.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeBeforeAfterValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeBeforeAfter {
    String message() default "{error.time}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String beforeField();

    String afterField();

    boolean switchEnableValue() default false;

    String switchField() default "";

    boolean enabledSwitchValue() default true;

}
