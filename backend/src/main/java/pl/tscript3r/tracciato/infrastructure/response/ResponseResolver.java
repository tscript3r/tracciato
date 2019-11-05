package pl.tscript3r.tracciato.infrastructure.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tscript3r.tracciato.infrastructure.response.error.AccessDeniedFaultResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.error.BindingFaultResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponseDto;

import java.util.Map;

import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.ERROR;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.FAIL;

public final class ResponseResolver {

    private ResponseResolver() {
    }

    public static ResponseDto getAccessDeniedResponse(String httpMethod, String path) {
        return new ResponseDto(FAIL, new AccessDeniedFaultResponseDto("Forbidden", httpMethod, path));
    }

    public static ResponseEntity getFailResponse(HttpStatus status, String message, Map<String, String> fields) {
        return new ResponseEntity<>(new ResponseDto(FAIL,
                new BindingFaultResponseDto(message, fields)), status);
    }

    public static ResponseEntity getFailResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(getResponse(FAIL, message), status);
    }

    public static ResponseEntity getErrorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(getResponse(ERROR, message), status);
    }

    private static ResponseDto getResponse(ResponseStatus responseStatus, String message) {
        return new ResponseDto(responseStatus, new FaultResponseDto(message));
    }

}
