package pl.tscript3r.tracciato.route;

import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RouteSpringRepositoryAdapter implements RouteRepositoryAdapter {

    private final RouteSpringRepository routeSpringRepository;

    @Override
    public Option<RouteEntity> findById(Long id) {
        return Option.ofOptional(routeSpringRepository.findById(id));
    }

    @Override
    public RouteEntity save(RouteEntity entity) {
        return routeSpringRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        routeSpringRepository.deleteById(id);
    }

    @Override
    public Option<RouteEntity> findByUuid(UUID routeUuid) {
        return Option.of(routeSpringRepository.findByUuid(routeUuid));
    }

}
