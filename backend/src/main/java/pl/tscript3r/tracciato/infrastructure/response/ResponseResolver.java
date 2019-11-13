package pl.tscript3r.tracciato.infrastructure.response;

import java.util.Map;

public interface ResponseResolver<T> {

    T getAccessDeniedResponse(String httpMethod, String path);

    T getNotFoundFailResponse(String httpMethod, String path);

    T getFailResponse(Integer httpStatus, String message, Map<String, String> fields);

    T getFailResponse(Integer httpStatus, String message);

    T getErrorResponse(Integer httpStatus, String message);

}
