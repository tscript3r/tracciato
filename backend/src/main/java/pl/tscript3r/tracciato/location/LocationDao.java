package pl.tscript3r.tracciato.location;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.infrastructure.db.Dao;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class LocationDao extends Dao<LocationEntity, LocationDto> {

    private final LocationRepositoryAdapter locationRepositoryAdapter;

    public LocationDao(ModelMapper modelMapper, LocationRepositoryAdapter repositoryAdapter, FailureResponse notFoundFailureResponse) {
        super(modelMapper, repositoryAdapter, notFoundFailureResponse);
        this.locationRepositoryAdapter = repositoryAdapter;
    }

    public InternalResponse<LocationEntity> getEntity(UUID uuid) {
        return InternalResponse.ofOption(repositoryAdapter.findByUuid(uuid), notFoundFailureResponse);
    }

    public Set<LocationDto> getAllFromUser(UUID userUuid) {
        return locationRepositoryAdapter.findAllFromUser(userUuid)
                .stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }

    public InternalResponse<LocationEntity> saveWithoutBackMapping(LocationDto dto) {
        return InternalResponse.payload(dto)
                .map(this::map)
                .peek(locationRepositoryAdapter::save);
    }

}
