package pl.tscript3r.tracciato.route.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.route.RouteFacade;

@Configuration
@RequiredArgsConstructor
public class RouteScheduleSpringConfiguration {

    private final RouteFacade routeFacade;

    @Bean
    public RouteScheduleFacade getRouteArrangeFacade() {
        return new RouteScheduleFacade(routeFacade);
    }

}
