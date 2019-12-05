package pl.tscript3r.tracciato.infrastructure.spring.web;

import io.vavr.control.Either;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.infrastructure.response.ResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.response.ResponseStatus;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponseDto;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.valueOf;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class SpringResponseResolver implements ResponseResolver<ResponseEntity> {

    private static final String RESOURCE_NOT_FOUND = "Resource not found";

    @Override
    public ResponseEntity resolve(@NotNull FailureResponse failureResponse) {
        return createFailureResponse(failureResponse);
    }

    @Override
    public ResponseEntity resolve(Object payload, @NotNull Integer httpStatus) {
        return new ResponseEntity<>(ResponseDto.of(ResponseStatus.get(httpStatus), payload), valueOf(httpStatus));
    }

    @Override
    public ResponseEntity resolve(@NotNull Either<? extends FailureResponse, ?> response) {
        return response.map(ResponseDto::success)
                .map(ResponseEntity::ok)
                .getOrElseGet(this::createFailureResponse);
    }

    private ResponseEntity createFailureResponse(FailureResponse failureResponse) {
        final var failureResponseDto = FailureResponseDto.get(failureResponse.getReason());
        failureResponse.getAdditionalFields()
                .forEach(failureResponseDto::add);
        return resolve(failureResponseDto, failureResponse.getHttpStatus());
    }

    @Override
    public ResponseEntity resolve(@NotNull Either<? extends FailureResponse, ?> response, @NotNull Integer httpStatus) {
        return response.map(ResponseDto::success)
                .map(responseDto ->
                        ResponseEntity.status(httpStatus)
                                .body(responseDto))
                .getOrElseGet(this::createFailureResponse);
    }

}
