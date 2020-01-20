package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class InMemoryRepositoryAdapter<T extends AbstractEntity> implements RepositoryAdapter<Long, T> {

    protected final ConcurrentMap<Long, T> db = new ConcurrentHashMap<>();

    @Override
    public Option<T> findById(Long id) {
        return Option.of(db.get(id));
    }

    @Override
    public Option<T> findByUuid(UUID uuid) {
        return find(o -> o.getUuid().equals(uuid));
    }

    @Override
    public T save(T entity) {
        Long id = (entity.getId() == null) ? db.size() + 1 : entity.getId();
        entity.setId(id);
        db.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        db.remove(id);
    }

    protected Option<T> find(Predicate<T> entityPredicate) {
        return Option.ofOptional(
                db.values()
                        .stream()
                        .filter(entityPredicate)
                        .findFirst()
        );
    }

}
