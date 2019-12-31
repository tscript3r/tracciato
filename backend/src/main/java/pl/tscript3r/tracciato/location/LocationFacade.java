package pl.tscript3r.tracciato.location;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class LocationFacade {

    private final UserFacade userFacade;
    private final LocationFactory locationFactory;
    private final LocationRepositoryAdapter locationRepositoryAdapter;

    public Either<FailureResponse, LocationDto> addLocation(String token, LocationDto locationDto) {
        return userFacade.validateAndGetUuidFromToken(token)
                .flatMap(uuid -> locationFactory.create(uuid, locationDto));
    }

    public Either<FailureResponse, Set<LocationDto>> getAllLocationsFromUser(String token) {
        return userFacade.validateAndGetUuidFromToken(token)
                .map(this::receiveUsersLocations);
    }

    private Set<LocationDto> receiveUsersLocations(UUID userUuid) {
        var locationEntities = locationRepositoryAdapter.findAllFromUser(userUuid);
        return LocationMapper.map(locationEntities);
    }

    public Either<FailureResponse, LocationEntity> getEntityByUuid(UUID existingLocationUuid) {
        return locationRepositoryAdapter.findByUuid(existingLocationUuid)
                .toEither(LocationFailureResponse.uuidNotFound(existingLocationUuid));
    }

}
