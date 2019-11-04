package pl.tscript3r.tracciato.infrastructure.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.tscript3r.tracciato.infrastructure.response.error.BindingErrorResponseDto;
import pl.tscript3r.tracciato.infrastructure.response.error.ErrorResponseDto;

import java.util.Map;

public final class ResponseResolver {

    public static ResponseEntity getErrorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new ResponseDto(ResponseStatus.FAIL, new ErrorResponseDto(message)), status);
    }

    public static ResponseEntity getErrorResponse(HttpStatus status, String message, Map<String, String> failsList) {
        return new ResponseEntity<>(new ResponseDto(ResponseStatus.FAIL,
                new BindingErrorResponseDto(message, failsList)), status);
    }

}
