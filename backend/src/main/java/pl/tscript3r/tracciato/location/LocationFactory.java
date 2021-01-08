package pl.tscript3r.tracciato.location;

import lombok.AllArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.UUID;

@AllArgsConstructor
class LocationFactory {

    private final LocationDao locationDao;
    private final DefaultValidator<LocationDto> validator;

    InternalResponse<LocationEntity> createEntity(UUID ownerUuid, LocationDto locationDto) {
        return validator.validate(locationDto)
                .peek(dto -> {
                    dto.setOwnerUuid(ownerUuid);
                    dto.setUuid(UUID.randomUUID());
                })
                .flatMap(locationDao::saveWithoutResultMapping);
    }

}
