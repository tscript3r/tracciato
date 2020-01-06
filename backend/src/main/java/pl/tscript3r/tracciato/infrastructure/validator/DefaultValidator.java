package pl.tscript3r.tracciato.infrastructure.validator;

import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultValidator<T> {

    private final Validator validator;

    public InternalResponse<T> validate(T object) {
        final Set<ConstraintViolation<T>> validationResults =
                validator.validate(object, Default.class);
        final var additionalValidations = additionalConstraints(object);
        if (validationResults.isEmpty() && additionalValidations.isEmpty())
            return InternalResponse.payload(object);
        else
            return InternalResponse.failure(createFailureResponse(map(validationResults, additionalValidations)));
    }

    protected Map<String, String> additionalConstraints(T object) {
        return new HashMap<>();
    }

    private FailureResponse createFailureResponse(Map<String, String> bindingFails) {
        return BindingFailureResponse.get(bindingFails);
    }

    private Map<String, String> map(Set<ConstraintViolation<T>> validationResults,
                                    Map<String, String> additionalValidations) {
        final Map<String, String> bindingFails = new HashMap<>();
        validationResults.forEach(objectError ->
                bindingFails.put(objectError.getPropertyPath().toString(), objectError.getMessage())
        );
        additionalValidations.forEach(bindingFails::put);
        return bindingFails;
    }

}
