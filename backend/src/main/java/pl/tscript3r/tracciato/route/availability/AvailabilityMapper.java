package pl.tscript3r.tracciato.route.availability;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;

final class AvailabilityMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static AvailabilityEntity map(AvailabilityDto availabilityDto) {
        return modelMapper.map(availabilityDto, AvailabilityEntity.class);
    }

    static AvailabilityDto map(AvailabilityEntity availabilityEntity) {
        return modelMapper.map(availabilityEntity, AvailabilityDto.class);
    }

}
