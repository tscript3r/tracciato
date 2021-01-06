package pl.tscript3r.tracciato.infrastructure.db;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class SpringRepositoryAdapter<E extends AbstractEntity> implements RepositoryAdapter<E> {

    private final SpringRepository<E> springRepository;

    @Override
    public Option<E> findById(Long id) {
        return Option.ofOptional(springRepository.findById(id));
    }

    @Override
    public Option<E> findByUuid(UUID uuid) {
        return Option.of(springRepository.findByUuid(uuid));
    }

    @Override
    public void delete(Long id) {
        springRepository.deleteById(id);
    }

    @Override
    public E save(E entity) {
        return springRepository.save(entity);
    }

}
