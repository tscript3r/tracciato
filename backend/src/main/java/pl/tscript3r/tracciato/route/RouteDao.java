package pl.tscript3r.tracciato.route;

import pl.tscript3r.tracciato.route.location.RouteLocationEntity;

import java.util.UUID;

public class RouteDao {

    private final RouteEntity routeEntity;

    public static RouteDao get(RouteEntity routeEntity) {
        return new RouteDao(routeEntity);
    }

    private RouteDao(RouteEntity routeEntity) {
        this.routeEntity = routeEntity;
    }

    public UUID getOwnerUuid() {
        return routeEntity.getOwnerUuid();
    }

    public void addRouteLocation(RouteLocationEntity routeLocationEntity) {
        var routeLocations = routeEntity.getLocations();
        routeLocations.add(routeLocationEntity);
    }

    public RouteEntity get() {
        return routeEntity;
    }

}
