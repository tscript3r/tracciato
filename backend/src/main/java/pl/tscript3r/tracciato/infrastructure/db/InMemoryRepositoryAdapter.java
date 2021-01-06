package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class InMemoryRepositoryAdapter<E extends AbstractEntity> implements RepositoryAdapter<E> {

    protected final ConcurrentMap<Long, E> db = new ConcurrentHashMap<>();

    @Override
    public Option<E> findById(Long id) {
        return Option.of(db.get(id));
    }

    @Override
    public Option<E> findByUuid(UUID uuid) {
        return find(o -> o.getUuid().equals(uuid));
    }

    @Override
    public E save(E entity) {
        Long id = (entity.getId() == null) ? db.size() + 1 : entity.getId();
        entity.setId(id);
        db.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        db.remove(id);
    }

    protected Option<E> find(Predicate<E> entityPredicate) {
        return Option.ofOptional(
                db.values()
                        .stream()
                        .filter(entityPredicate)
                        .findFirst()
        );
    }

}
