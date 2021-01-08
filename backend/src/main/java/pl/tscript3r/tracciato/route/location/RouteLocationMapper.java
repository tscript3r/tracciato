package pl.tscript3r.tracciato.route.location;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.route.availability.AvailabilityEntity;
import pl.tscript3r.tracciato.route.availability.api.AvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class RouteLocationMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static RouteLocationEntity map(RouteLocationDto routeLocationDto) {
        var routeLocationEntity = modelMapper.map(routeLocationDto, RouteLocationEntity.class);
        routeLocationEntity.setAvailability(mapDayAvailabilityList(routeLocationDto.getAvailability()));
        routeLocationEntity.setOnsideDuration(stringToDuration(routeLocationDto.getOnsideDuration()));
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

    public static RouteLocationDto map(RouteLocationEntity routeLocationEntity) {
        return modelMapper.map(routeLocationEntity, RouteLocationDto.class);
    }

}
