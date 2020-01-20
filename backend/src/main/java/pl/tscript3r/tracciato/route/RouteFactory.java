package pl.tscript3r.tracciato.route;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;

import java.util.UUID;

@RequiredArgsConstructor
class RouteFactory {

    private final DefaultValidator<NewRouteDto> validator;
    private final ModelMapper modelMapper;
    private final RouteDao routeDao;

    InternalResponse<RouteDto> create(NewRouteDto newRouteDto) {
        return validator.validate(newRouteDto)
                .map(this::createRouteEntity)
                .flatMap(routeDao::save);
    }

    private RouteDto createRouteEntity(NewRouteDto newRouteDto) {
        var routeEntity = modelMapper.map(newRouteDto, RouteDto.class);
        routeEntity.setUuid(UUID.randomUUID());
        return routeEntity;
    }

}
