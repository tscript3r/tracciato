package pl.tscript3r.tracciato.infrastructure.response;

import io.vavr.control.Either;
import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InternalResponse<T> implements Either<FailureResponse, T> {

    private final Either<FailureResponse, T> either;

    private InternalResponse(Either<FailureResponse, T> either) {
        this.either = either;
    }

    public static <T> InternalResponse<T> payload(T value) {
        return new InternalResponse<>(Either.right(value));
    }

    public static <T> InternalResponse<T> failure(FailureResponse failureResponse) {
        return new InternalResponse<>(Either.left(failureResponse));
    }

    public static <T> InternalResponse<T> ofOption(Option<T> option, FailureResponse failureResponse) {
        return new InternalResponse<>(option.toEither(failureResponse));
    }

    public <U> InternalResponse<U> map(Function<? super T, ? extends U> mapper) {
        return new InternalResponse<>(either.map(mapper));
    }

    public InternalResponse<T> peek(Consumer<? super T> action) {
        either.peek(action);
        return this;
    }

    public <U> InternalResponse<U> flatMap(Function<? super T, ? extends Either<FailureResponse, ? extends U>> mapper) {
        return new InternalResponse<>(either.flatMap(mapper));
    }

    public InternalResponse<T> filterOrElse(Predicate<? super T> predicate, Function<? super T, ? extends FailureResponse> zero) {
        return new InternalResponse<>(either.filterOrElse(predicate, zero));
    }

    public InternalResponse<T> filterInternal(Predicate<? super T> predicate, FailureResponse failureResponse) {
        return isLeft() || predicate.test(get()) ? this : InternalResponse.failure(failureResponse);
    }

    @Override
    public T get() {
        return either.get();
    }

    @Override
    public String stringPrefix() {
        return either.stringPrefix();
    }

    @Override
    public FailureResponse getLeft() {
        return either.getLeft();
    }

    @Override
    public boolean isLeft() {
        return either.isLeft();
    }

    @Override
    public boolean isRight() {
        return either.isRight();
    }

}
