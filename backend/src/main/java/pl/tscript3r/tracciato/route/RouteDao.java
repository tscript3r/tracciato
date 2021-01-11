package pl.tscript3r.tracciato.route;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.infrastructure.db.Dao;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.location.LocationEntity;
import pl.tscript3r.tracciato.route.api.RouteDto;
import pl.tscript3r.tracciato.stop.StopEntity;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

final class RouteDao extends Dao<RouteEntity, RouteDto> {

    private final RouteRepositoryAdapter routeRepositoryAdapter;

    public RouteDao(ModelMapper modelMapper, RouteRepositoryAdapter repositoryAdapter,
                    FailureResponse notFoundFailureResponse) {
        super(modelMapper, repositoryAdapter, notFoundFailureResponse);
        this.routeRepositoryAdapter = repositoryAdapter;
    }

    public InternalResponse<RouteDto> addStop(UUID routeUuid, StopEntity stopEntity) {
        return updateAndSetScheduledAsFalse(routeUuid, entity -> entity.getStops().add(stopEntity));
    }

    private InternalResponse<RouteDto> updateAndSetScheduledAsFalse(UUID uuid, Consumer<? super RouteEntity> action) {
        return update(uuid, routeEntity -> {
            routeEntity.setScheduled(false);
            routeEntity.setLastUpdate(LocalDateTime.now());
            action.accept(routeEntity);
        });
    }

    public InternalResponse<RouteEntity> getEntity(UUID routeUuid) {
        return InternalResponse.ofOption(routeRepositoryAdapter.findByUuid(routeUuid), notFoundFailureResponse);
    }

    public InternalResponse<RouteDto> setStartLocation(UUID routeUuid, LocationEntity locationEntity) {
        return updateAndSetScheduledAsFalse(routeUuid, entity -> entity.setStartLocation(locationEntity));
    }

    public InternalResponse<RouteDto> setEndLocation(UUID routeUuid, LocationEntity locationEntity) {
        return updateAndSetScheduledAsFalse(routeUuid, entity -> entity.setEndLocation(locationEntity));
    }

    public InternalResponse<RouteDto> addAvailability(UUID routeUuid, AvailabilityEntity availabilityEntity) {
        return updateAndSetScheduledAsFalse(routeUuid, entity -> entity.getAvailabilities().add(availabilityEntity));
    }

    public InternalResponse<RouteDto> setAsScheduled(UUID routeUuid) {
        return update(routeUuid, routeEntity -> {
            routeEntity.setScheduled(true);
            routeEntity.setLastUpdate(LocalDateTime.now());
            routeEntity.setLastSchedule(LocalDateTime.now());
        });
    }

}
