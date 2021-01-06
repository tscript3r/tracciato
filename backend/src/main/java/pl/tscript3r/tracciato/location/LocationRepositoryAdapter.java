package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;

import java.util.Set;
import java.util.UUID;

interface LocationRepositoryAdapter extends RepositoryAdapter<LocationEntity> {

    Set<LocationEntity> findAllFromUser(UUID ownerUuid);

}
