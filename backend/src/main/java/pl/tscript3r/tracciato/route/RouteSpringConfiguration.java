package pl.tscript3r.tracciato.route;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.infrastructure.validator.DefaultValidator;
import pl.tscript3r.tracciato.location.LocationFacade;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.Validation;

@Configuration
@RequiredArgsConstructor
class RouteSpringConfiguration {

    private final RouteSpringRepository routeSpringRepository;
    private final UserFacade userFacade;
    private final LocationFacade locationFacade;

    static RouteFacade getInMemoryRouteFacade(UserFacade userFacade,
                                              RouteRepositoryAdapter routeRepositoryAdapter,
                                              LocationFacade locationFacade) {
        return get(routeRepositoryAdapter, userFacade, locationFacade);
    }

    private static RouteFacade get(RouteRepositoryAdapter routeRepositoryAdapter, UserFacade userFacade,
                                   LocationFacade locationFacade) {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var routeValidator = new DefaultValidator<NewRouteDto>(validator);
        var routeFactory = new RouteFactory(routeValidator, routeRepositoryAdapter);
        return new RouteFacade(userFacade, routeFactory, routeRepositoryAdapter, locationFacade);
    }

    @Bean
    public RouteFacade getRouteFacade() {
        var routeSpringRepositoryAdapter = new RouteSpringRepositoryAdapter(routeSpringRepository);
        return get(routeSpringRepositoryAdapter, userFacade, locationFacade);
    }

}
