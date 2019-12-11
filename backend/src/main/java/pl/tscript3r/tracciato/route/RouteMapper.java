package pl.tscript3r.tracciato.route;

import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.route.api.NewRouteDto;

final class RouteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static RouteEntity map(NewRouteDto newRouteDto) {
        return modelMapper.map(newRouteDto, RouteEntity.class);
    }

    public static NewRouteDto map(RouteEntity routeEntity) {
        return modelMapper.map(routeEntity, NewRouteDto.class);
    }

}
