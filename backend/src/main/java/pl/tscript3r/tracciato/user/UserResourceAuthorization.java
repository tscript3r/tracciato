package pl.tscript3r.tracciato.user;

import io.vavr.control.Either;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.util.UUID;

import static pl.tscript3r.tracciato.infrastructure.response.error.GlobalFailureResponse.UNAUTHORIZED_ERROR;

class UserResourceAuthorization {

    <T> Either<FailureResponse, T> authorize(UUID requestUserUuid, UUID resourceOwnerUuid, T resource) {
        // by adding roles there will be more
        if (requestUserUuid.equals(resourceOwnerUuid))
            return Either.right(resource);
        return Either.left(UNAUTHORIZED_ERROR);
    }

}
