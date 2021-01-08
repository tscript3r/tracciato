package pl.tscript3r.tracciato.stop;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.availability.api.AvailabilityDto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class StopMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static StopEntity map(StopDto stopDto) {
        var routeLocationEntity = modelMapper.map(stopDto, StopEntity.class);
        routeLocationEntity.setAvailability(mapDayAvailabilityList(stopDto.getAvailability()));
        routeLocationEntity.setOnsideDuration(stringToDuration(stopDto.getOnsideDuration()));
        return routeLocationEntity;
    }

    private static List<AvailabilityEntity> mapDayAvailabilityList(Collection<AvailabilityDto> routeLocationDtos) {
        List<AvailabilityEntity> resultList = new ArrayList<>();
        routeLocationDtos.forEach(availabilityDto -> resultList.add(modelMapper.map(availabilityDto, AvailabilityEntity.class)));
        return resultList;
    }

    private static Duration stringToDuration(String duration) {
        return Duration.parse(duration);
    }

    public static StopDto map(StopEntity stopEntity) {
        return modelMapper.map(stopEntity, StopDto.class);
    }

}
