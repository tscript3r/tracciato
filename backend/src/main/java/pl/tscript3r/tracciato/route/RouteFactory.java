package pl.tscript3r.tracciato.route;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import pl.tscript3r.tracciato.infrastructure.response.error.FailureResponse;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.route.api.RouteDto;

import java.util.UUID;

@RequiredArgsConstructor
class RouteFactory {

    private final DefaultValidator<NewRouteDto> validator;
    private final RouteRepositoryAdapter routeRepositoryAdapter;

    synchronized Either<FailureResponse, RouteDto> create(NewRouteDto newRouteDto) {
        return validator.validate(newRouteDto)
                .map(this::createRouteEntity)
                .map(routeRepositoryAdapter::save)
                .map(RouteMapper::map);
    }

    private RouteEntity createRouteEntity(NewRouteDto newRouteDto) {
        var routeEntity = RouteMapper.map(newRouteDto);
        routeEntity.setUuid(UUID.randomUUID());
        return routeEntity;
    }

}
