package pl.tscript3r.tracciato.route;

import io.vavr.control.Option;
import pl.tscript3r.tracciato.infrastructure.db.RepositoryAdapter;

import java.util.UUID;

public interface RouteRepositoryAdapter extends RepositoryAdapter<Long, RouteEntity> {

    Option<RouteEntity> findByUuid(UUID routeUuid);

}
