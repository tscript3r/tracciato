package pl.tscript3r.tracciato.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RouteSpringRepository extends JpaRepository<RouteEntity, Long> {

    RouteEntity findByUuid(UUID uuid);

}
