package pl.tscript3r.tracciato.route.location;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.route.location.api.DayAvailabilityDto;
import pl.tscript3r.tracciato.route.location.api.RouteLocationDto;
import pl.tscript3r.tracciato.route.location.day.DayAvailabilityEntity;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pl.tscript3r.tracciato.infrastructure.DateTimeFormats.TIME_FORMAT;

public final class RouteLocationMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static RouteLocationEntity map(RouteLocationDto routeLocationDto) {
        var routeLocationEntity = modelMapper.map(routeLocationDto, RouteLocationEntity.class);
        routeLocationEntity.setAvailability(mapDayAvailabilityList(routeLocationDto.getAvailability()));
        routeLocationEntity.setOnsideDuration(stringToDuration(routeLocationDto.getOnsideDuration()));
        return routeLocationEntity;
    }

    private static List<DayAvailabilityEntity> mapDayAvailabilityList(Collection<DayAvailabilityDto> routeLocationDtos) {
        List<DayAvailabilityEntity> resultList = new ArrayList<>();
        routeLocationDtos.forEach(dayAvailabilityDto -> resultList.add(modelMapper.map(dayAvailabilityDto, DayAvailabilityEntity.class)));
        return resultList;
    }

    private static Duration stringToDuration(String duration) {
        LocalTime lt = LocalTime.parse(duration, DateTimeFormatter.ofPattern(TIME_FORMAT));
        return Duration.between(LocalTime.MIN, lt);
    }

    public static RouteLocationDto map(RouteLocationEntity routeLocationEntity) {
        return modelMapper.map(routeLocationEntity, RouteLocationDto.class);
    }

}
