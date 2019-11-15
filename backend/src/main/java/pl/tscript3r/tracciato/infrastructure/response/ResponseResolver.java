package pl.tscript3r.tracciato.infrastructure.response;

import io.vavr.control.Either;
import org.springframework.lang.Nullable;
import pl.tscript3r.tracciato.infrastructure.response.error.FaultResponse;

import javax.validation.constraints.NotNull;

public interface ResponseResolver<T> {

    /**
     * Creates default response. There are two default behaviors:
     * <p>
     * In case when given argument contains any object:
     * - http status: 200
     * - response status: success
     * - payload will be mapped to JSON
     * <p>
     * In case when given argument is null returns:
     * - http status: 404
     * - response status: fail
     * - as payload will be added default message: "Resource not found"
     *
     * @param payload content of response
     * @return <b>T</b> desired response object
     */
    T get(@Nullable Object payload);

    T get(@NotNull Either<FaultResponse, Object> payload);

    T get(@NotNull Either<FaultResponse, Object> payload, @NotNull Integer httpStatus);

    T get(@NotNull Integer httpStatus, @NotNull ResponseStatus responseStatus, @Nullable Object payload);

}
