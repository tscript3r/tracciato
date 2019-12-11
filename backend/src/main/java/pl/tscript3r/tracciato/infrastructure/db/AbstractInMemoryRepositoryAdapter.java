package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.AbstractEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class AbstractInMemoryRepositoryAdapter<T extends AbstractEntity> implements RepositoryAdapter<Long, T> {

    protected final Map<Long, T> db = new HashMap<>();

    @Override
    public Option<T> findById(Long id) {
        return Option.of(db.get(id));
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
