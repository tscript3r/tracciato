package pl.tscript3r.tracciato.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface RouteSpringRepository extends JpaRepository<RouteEntity, Long> {

    RouteEntity findByUuid(UUID uuid);

}
