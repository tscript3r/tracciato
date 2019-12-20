package pl.tscript3r.tracciato.user;

import java.util.UUID;

class UserResourceAuthorization {

    Boolean authorize(UUID requestUserUuid, UUID resourceOwnerUuid) {
        // by adding roles there will be more
        return requestUserUuid.equals(resourceOwnerUuid);
    }

}
