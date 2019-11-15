package pl.tscript3r.tracciato.infrastructure.spring.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.response.ResponseStatus;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.ERROR;
import static pl.tscript3r.tracciato.infrastructure.response.ResponseStatus.FAIL;

// TODO Add: Caused following exception: org.springframework.web.HttpMediaTypeNotSupportedException: Content type
//  'text/plain;charset=UTF-8' not supported

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@AllArgsConstructor
public class ControllerExceptionHandler {

    private final ResponseResolver<ResponseEntity> responseResolver;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity handleMessageNotReadableException() {
        return responseResolver.get(HttpStatus.BAD_REQUEST.value(), ResponseStatus.FAIL,
                FaultResponseDto.get("Body no readable / empty"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity handleBindingFail(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> results = new HashMap<>();
        bindingResult.getAllErrors().forEach(objectError ->
                results.put(((FieldError) objectError).getField(), objectError.getDefaultMessage())
        );
        return responseResolver.get(BAD_REQUEST.value(), FAIL, FaultResponseDto.get("Validation").add("fields", results));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return responseResolver.get(METHOD_NOT_ALLOWED.value(), FAIL, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public final ResponseEntity handleNotFoundError(HttpServletRequest request) {
        return responseResolver.get(NOT_FOUND.value(), FAIL,
                FaultResponseDto.get("Resource not found").add("method", request.getMethod()).add("uri", request.getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleUnknownException(Exception exception, WebRequest webRequest) {
        log.error("Request: {} User: {} Params: {}",
                webRequest.toString(),
                webRequest.getParameterMap()
                        .entrySet()
                        .stream()
                        .map(stringEntry -> stringEntry.getKey() + "=" + Arrays.toString(stringEntry.getValue()))
                        .collect(Collectors.toList()),
                webRequest.getUserPrincipal());
        log.error("Caused following exception: {}", exception.toString(), exception);
        return responseResolver.get(INTERNAL_SERVER_ERROR.value(), ERROR, FaultResponseDto.get("Internal server error"));
    }

    @ExceptionHandler(NotImplementedException.class)
    public final ResponseEntity handleNotImplementedException() {
        return responseResolver.get(NOT_IMPLEMENTED.value(), ERROR, FaultResponseDto.get("Not implemented yet"));
    }

}
