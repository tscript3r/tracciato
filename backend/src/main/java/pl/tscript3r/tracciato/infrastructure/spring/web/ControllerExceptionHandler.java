package pl.tscript3r.tracciato.infrastructure.spring.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import pl.tscript3r.tracciato.infrastructure.response.ResponseResolver;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

// TODO Add: Caused following exception: org.springframework.web.HttpMediaTypeNotSupportedException: Content type
//  'text/plain;charset=UTF-8' not supported

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@AllArgsConstructor
public final class ControllerExceptionHandler {

    private final ResponseResolver<ResponseEntity<?>> responseResolver;

    @ResponseStatus(BAD_REQUEST) // <-- needed for OpenAPI docs
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<?> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException && e.getCause().getCause() instanceof DateTimeException) {
            DateTimeException dateTimeException = (DateTimeException) (e.getCause()).getCause();
            return responseResolver.resolve(FailureResponseDto.get(dateTimeException.getMessage()), BAD_REQUEST.value());
        }
        return responseResolver.resolve(FailureResponseDto.get("Body not readable / empty"), BAD_REQUEST.value());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleBindingFail(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> results = new HashMap<>();
        bindingResult.getAllErrors().forEach(objectError ->
                results.put(((FieldError) objectError).getField(), objectError.getDefaultMessage())
        );
        return responseResolver.resolve(FailureResponseDto.get("Validation").add("fields", results),
                BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return responseResolver.resolve(FailureResponseDto.get("Invalid method argument")
                .add("variable", e.getParameter().getParameterName()), BAD_REQUEST.value());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return responseResolver.resolve(e.getMessage(), METHOD_NOT_ALLOWED.value());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public final ResponseEntity<?> handleNotFoundError(HttpServletRequest request) {
        return responseResolver.resolve(FailureResponseDto.get("Resource not found")
                        .add("method", request.getMethod())
                        .add("uri", request.getRequestURI()),
                NOT_FOUND.value());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleUnknownException(Exception exception, WebRequest webRequest) {
        log.error("Request: {} User: {} Params: {}",
                webRequest.toString(),
                webRequest.getParameterMap()
                        .entrySet()
                        .stream()
                        .map(stringEntry -> stringEntry.getKey() + "=" + Arrays.toString(stringEntry.getValue()))
                        .collect(Collectors.toList()),
                webRequest.getUserPrincipal());
        log.error("Caused following exception: {}", exception.toString(), exception);
        return responseResolver.resolve(FailureResponseDto.get("Internal server error"), INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(NotImplementedException.class)
    public final ResponseEntity<?> handleNotImplementedException() {
        return responseResolver.resolve(FailureResponseDto.get("Not implemented yet"), NOT_IMPLEMENTED.value());
    }

}
