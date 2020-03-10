package pl.tscript3r.tracciato.route.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.route.schedule.scheduler.RouteSchedulerFacade;

@Configuration
@RequiredArgsConstructor
class RouteScheduleSpringConfiguration {

    private final RouteFacade routeFacade;
    private final RouteSchedulerFacade routeSchedulerFacade;

    @Bean
    public RouteScheduleFacade getRouteArrangeFacade() {
        return new RouteScheduleFacade(routeFacade, routeSchedulerFacade);
    }

}
