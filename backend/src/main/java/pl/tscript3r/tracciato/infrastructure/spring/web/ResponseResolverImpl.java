package pl.tscript3r.tracciato.infrastructure.spring.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.infrastructure.response.ResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.response.ResponseStatus;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.error.MethodPathFaultResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.error.ValidationFaultResponseDto;

import java.util.Map;

import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.ERROR;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.FAIL;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class ResponseResolverImpl implements ResponseResolver<ResponseEntity> {

    private ResponseResolverImpl() {
    }

    @Override
    public ResponseEntity getAccessDeniedResponse(String httpMethod, String path) {
        return new ResponseEntity<>(new ResponseDto(FAIL,
                getMethodPathFaultResponseDto("Forbidden", httpMethod, path)), HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity getNotFoundFailResponse(String httpMethod, String path) {
        return new ResponseEntity<>(new ResponseDto(FAIL,
                getMethodPathFaultResponseDto("Not found", httpMethod, path)), HttpStatus.NOT_FOUND);
    }

    private MethodPathFaultResponseDto getMethodPathFaultResponseDto(String reason, String httpMethod, String path) {
        return new MethodPathFaultResponseDto(reason, httpMethod, path);
    }

    @Override
    public ResponseEntity getFailResponse(Integer httpStatus, String message, Map<String, String> fields) {
        return new ResponseEntity<>(new ResponseDto(FAIL,
                new ValidationFaultResponseDto(message, fields)), HttpStatus.valueOf(httpStatus));
    }

    @Override
    public ResponseEntity getFailResponse(Integer httpStatus, String message) {
        return new ResponseEntity<>(getResponseDto(FAIL, message), HttpStatus.valueOf(httpStatus));
    }

    @Override
    public ResponseEntity getErrorResponse(Integer httpStatus, String message) {
        return new ResponseEntity<>(getResponseDto(ERROR, message), HttpStatus.valueOf(httpStatus));
    }

    private ResponseDto getResponseDto(ResponseStatus responseStatus, String message) {
        return new ResponseDto(responseStatus, new FaultResponseDto(message));
    }

}
