package pl.tscript3r.tracciato.location;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.AbstractInMemoryRepositoryAdapter;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocationInMemoryRepositoryAdapter extends AbstractInMemoryRepositoryAdapter<LocationEntity>
        implements LocationRepositoryAdapter {

    @Override
    public Collection<LocationEntity> findAllFromUser(UUID ownerUuid) {
        return db.values()
                .stream()
                .filter(locationEntity -> locationEntity.getOwnerUuid().equals(ownerUuid))
                .collect(Collectors.toSet());
    }

    @Override
    public Option<LocationEntity> findByUuid(UUID uuid) {
        return find(locationEntity -> locationEntity.getUuid().equals(uuid));
    }

}
