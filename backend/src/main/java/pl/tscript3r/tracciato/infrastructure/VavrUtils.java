package pl.tscript3r.tracciato.infrastructure;

import io.vavr.control.Either;
import io.vavr.control.Option;

public final class VavrUtils {

    public static <U, T> Either<? extends U, ? extends T> unwrapOptionToEither(U left, Option<T> rightOption) {
        if (rightOption.isDefined())
            return Either.right(rightOption.get());
        else
            return Either.left(left);
    }

}
