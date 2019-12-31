package pl.tscript3r.tracciato.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface LocationSpringRepository extends JpaRepository<LocationEntity, Long> {

    Set<LocationEntity> findAllByOwnerUuid(UUID uuid);

    LocationEntity findByUuid(UUID uuid);

}
