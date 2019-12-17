package pl.tscript3r.tracciato.route;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.AbstractInMemoryRepositoryAdapter;

import java.util.UUID;

public class RouteInMemoryRepositoryAdapter extends AbstractInMemoryRepositoryAdapter<RouteEntity> implements
        RouteRepositoryAdapter {

    @Override
    public Option<RouteEntity> findByUuid(UUID routeUuid) {
        return find(routeEntity -> routeEntity.getUuid().equals(routeUuid));
    }

}
