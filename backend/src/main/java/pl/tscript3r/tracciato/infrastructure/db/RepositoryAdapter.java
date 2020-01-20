package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;

import java.util.UUID;

public interface RepositoryAdapter<ID, T> {

    Option<T> findById(ID id);

    Option<T> findByUuid(UUID uuid);

    T save(T entity);

    void delete(ID id);

}
