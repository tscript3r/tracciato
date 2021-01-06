package pl.tscript3r.tracciato.location;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepository;

import java.util.Set;
import java.util.UUID;

public interface LocationSpringRepository extends SpringRepository<LocationEntity> {

    Set<LocationEntity> findAllByOwnerUuid(UUID uuid);

}
