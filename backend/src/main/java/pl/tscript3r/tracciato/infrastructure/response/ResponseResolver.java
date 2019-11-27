package pl.tscript3r.tracciato.infrastructure.response;

import io.vavr.control.Either;
import org.springframework.lang.Nullable;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import javax.validation.constraints.NotNull;

public interface ResponseResolver<T> {

    T resolve(@Nullable Object payload, @NotNull Integer httpStatus);

    T resolve(@NotNull Either<? extends FailureResponse, ?> response);

    T resolve(@NotNull Either<? extends FailureResponse, ?> payload, @NotNull Integer httpStatus);

}
