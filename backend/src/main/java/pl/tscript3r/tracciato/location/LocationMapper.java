package pl.tscript3r.tracciato.location;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.location.api.LocationDto;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class LocationMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static LocationEntity map(LocationDto locationDto) {
        return modelMapper.map(locationDto, LocationEntity.class);
    }

    static LocationDto map(LocationEntity locationEntity) {
        return modelMapper.map(locationEntity, LocationDto.class);
    }

    static Set<LocationDto> map(Collection<LocationEntity> locationEntities) {
        return locationEntities.stream()
                .map(LocationMapper::map)
                .collect(Collectors.toSet());
    }

}
