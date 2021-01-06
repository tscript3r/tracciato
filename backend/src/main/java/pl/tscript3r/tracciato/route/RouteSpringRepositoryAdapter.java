package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.infrastructure.db.SpringRepository;
import pl.tscript3r.tracciato.infrastructure.db.SpringRepositoryAdapter;

class RouteSpringRepositoryAdapter extends SpringRepositoryAdapter<RouteEntity> implements RouteRepositoryAdapter {

    public RouteSpringRepositoryAdapter(SpringRepository<RouteEntity> routeSpringRepository) {
        super(routeSpringRepository);
    }

}
