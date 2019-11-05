package pl.tscript3r.tracciato.infrastructure.spring.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity handleMessageNotReadableException() {
        return ResponseResolver.getFailResponse(HttpStatus.BAD_REQUEST, "Body empty / not readable");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity handleBindingFail(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> results = new HashMap<>();
        bindingResult.getAllErrors().forEach(objectError ->
                results.put(((FieldError) objectError).getField(), objectError.getDefaultMessage())
        );
        return ResponseResolver.getFailResponse(HttpStatus.BAD_REQUEST, "Validation", results);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseResolver.getFailResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleUnknownException(Exception exception, WebRequest webRequest) {
        log.error("Request: {} User: {} Params: {}",
                webRequest.toString(),
                webRequest.getParameterMap().entrySet().
                        stream()
                        .map(stringEntry -> stringEntry.getKey() + "=" + Arrays.toString(stringEntry.getValue()))
                        .collect(Collectors.toList()),
                webRequest.getUserPrincipal());
        log.error("Caused following exception: {}", exception.toString(), exception);
        return ResponseResolver.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(NotImplementedException.class)
    public final ResponseEntity handleNotImplementedException() {
        return ResponseResolver.getErrorResponse(HttpStatus.NOT_IMPLEMENTED,"Not implemented yet");
    }

}
