package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;

import java.util.UUID;

public interface RepositoryAdapter<E extends AbstractEntity> {

    Option<E> findById(Long id);

    Option<E> findByUuid(UUID uuid);

    E save(E entity);

    void delete(Long id);

}
