package pl.tscript3r.tracciato.location;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.user.UserFacade;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class LocationFacade {

    private final UserFacade userFacade;
    private final LocationFactory locationFactory;
    private final LocationRepositoryAdapter locationRepositoryAdapter;

    public InternalResponse<LocationEntity> addLocation(String token, LocationDto locationDto) {
        return userFacade.validateAndGetUuidFromToken(token)
                .flatMap(uuid -> locationFactory.createEntity(uuid, locationDto));
    }

    public InternalResponse<LocationDto> addLocationAndMap(String token, LocationDto locationDto) {
        return addLocation(token, locationDto)
                .map(LocationMapper::map);
    }

    public InternalResponse<Set<LocationDto>> getAllLocationsFromUser(String token) {
        return userFacade.validateAndGetUuidFromToken(token)
                .map(this::receiveUsersLocations);
    }

    private Set<LocationDto> receiveUsersLocations(UUID userUuid) {
        var locationEntities = locationRepositoryAdapter.findAllFromUser(userUuid);
        return LocationMapper.map(locationEntities);
    }

    public InternalResponse<LocationEntity> getLocationEntityByUuid(UUID existingLocationUuid) {
        return InternalResponse.fromOption(locationRepositoryAdapter.findByUuid(existingLocationUuid),
                LocationFailureResponse.uuidNotFound(existingLocationUuid));
    }

}
