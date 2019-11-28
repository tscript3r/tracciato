package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;

public interface RepositoryAdapter<ID, T> {

    Option<T> findById(ID id);

    T save(T entity);

    void delete(ID id);

}
