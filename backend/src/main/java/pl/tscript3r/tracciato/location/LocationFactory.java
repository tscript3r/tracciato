package pl.tscript3r.tracciato.location;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.UUID;

@AllArgsConstructor
class LocationFactory {

    private final LocationRepositoryAdapter locationRepositoryAdapter;
    private final DefaultValidator<LocationDto> validator;

    Either<FailureResponse, LocationDto> create(UUID ownerUuid, LocationDto locationDto) {
        return validator.validate(locationDto)
                .map(location -> createLocationEntity(ownerUuid, locationDto))
                .peek(locationRepositoryAdapter::save)
                .map(LocationMapper::map);
    }

    private LocationEntity createLocationEntity(UUID ownerUuid, LocationDto locationDto) {
        var locationEntity = LocationMapper.map(locationDto);
        locationEntity.setOwnerUuid(ownerUuid);
        locationEntity.setUuid(UUID.randomUUID());
        return locationEntity;
    }

}
