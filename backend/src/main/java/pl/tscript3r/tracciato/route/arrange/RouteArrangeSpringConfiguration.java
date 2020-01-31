package pl.tscript3r.tracciato.route.arrange;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.route.RouteFacade;

@Configuration
@RequiredArgsConstructor
public class RouteArrangeSpringConfiguration {

    private final RouteFacade routeFacade;

    @Bean
    public RouteArrangeFacade getRouteArrangeFacade() {
        return new RouteArrangeFacade(routeFacade);
    }

}
