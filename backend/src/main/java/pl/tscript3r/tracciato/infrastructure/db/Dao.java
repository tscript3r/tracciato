package pl.tscript3r.tracciato.infrastructure.db;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class Dao<E extends AbstractEntity, D> {

    protected final ModelMapper modelMapper;
    protected final RepositoryAdapter<E> repositoryAdapter;
    protected final FailureResponse notFoundFailureResponse;

    private final Class<E> entityType;
    private final Class<D> dtoType;

    @SuppressWarnings("unchecked")
    public Dao(ModelMapper modelMapper, RepositoryAdapter<E> repositoryAdapter, FailureResponse notFoundFailureResponse) {
        Objects.requireNonNull(modelMapper);
        Objects.requireNonNull(repositoryAdapter);
        Objects.requireNonNull(notFoundFailureResponse);
        this.modelMapper = modelMapper;
        this.repositoryAdapter = repositoryAdapter;
        this.notFoundFailureResponse = notFoundFailureResponse;

        entityType = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        dtoType = (Class<D>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public InternalResponse<D> get(UUID uuid) {
        Objects.requireNonNull(uuid);
        return InternalResponse.ofOption(repositoryAdapter.findByUuid(uuid)
                .map(this::map), notFoundFailureResponse);
    }

    public InternalResponse<D> update(UUID uuid, Consumer<? super E> action) {
        Objects.requireNonNull(uuid);
        Objects.requireNonNull(action);
        return InternalResponse.ofOption(repositoryAdapter.findByUuid(uuid)
                .peek(action)
                .peek(repositoryAdapter::save)
                .map(this::map), notFoundFailureResponse);
    }

    public InternalResponse<D> save(D o) {
        Objects.requireNonNull(o);
        return InternalResponse.payload(o)
                .map(this::map)
                .peek(repositoryAdapter::save)
                .map(this::map);
    }

    public E map(D o) {
        Objects.requireNonNull(o);
        return modelMapper.map(o, entityType);
    }

    public D map(E o) {
        Objects.requireNonNull(o);
        return modelMapper.map(o, dtoType);
    }

}
