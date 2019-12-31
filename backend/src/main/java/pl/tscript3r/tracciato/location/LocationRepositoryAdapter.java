package pl.tscript3r.tracciato.location;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;

import java.util.Collection;
import java.util.UUID;

public interface LocationRepositoryAdapter extends RepositoryAdapter<Long, LocationEntity> {

    Collection<LocationEntity> findAllFromUser(UUID ownerUuid);

    Option<LocationEntity> findByUuid(UUID uuid);

}
