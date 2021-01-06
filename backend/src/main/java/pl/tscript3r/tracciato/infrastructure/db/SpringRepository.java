package pl.tscript3r.tracciato.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface SpringRepository<E extends AbstractEntity> extends JpaRepository<E, Long> {

    E findByUuid(UUID uuid);

}
