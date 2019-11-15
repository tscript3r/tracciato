package pl.tscript3r.tracciato.infrastructure.spring.web;

import io.vavr.control.Either;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.infrastructure.response.ResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.response.ResponseStatus;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponseDto;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.FAIL;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.SUCCESS;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class ResponseResolverImpl implements ResponseResolver<ResponseEntity> {

    private static final String RESOURCE_NOT_FOUND = "Resource not found";

    @Override
    public ResponseEntity get(@NotNull Integer httpStatus, @NotNull ResponseStatus responseStatus, Object payload) {
        return new ResponseEntity<>(new ResponseDto(responseStatus, payload), HttpStatus.valueOf(httpStatus));
    }

    @Override
    public ResponseEntity get(Object payload) {
        if (payload == null)
            return get(NOT_FOUND.value(), FAIL, FaultResponseDto.get(RESOURCE_NOT_FOUND));
        else
            return get(OK.value(), SUCCESS, payload);
    }

    @Override
    public ResponseEntity get(@NotNull Either<FaultResponse, Object> payload) {
        return null;
    }

    @Override
    public ResponseEntity get(@NotNull Either<FaultResponse, Object> payload, @NotNull Integer httpStatus) {
        return null;
    }

}
