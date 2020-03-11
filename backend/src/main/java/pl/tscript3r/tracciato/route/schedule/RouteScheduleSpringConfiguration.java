package pl.tscript3r.tracciato.route.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.schedule.scheduler.RouteScheduler;

@Configuration
@RequiredArgsConstructor
class RouteScheduleSpringConfiguration {

    private final RouteFacade routeFacade;
    private final RouteScheduler routeScheduler;

    @Bean
    public RouteScheduleFacade getRouteScheduleFacade() {
        return new RouteScheduleFacade(routeFacade, routeScheduler);
    }

}
