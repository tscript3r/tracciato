package pl.tscript3r.tracciato.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.route.RouteFacade;
import pl.tscript3r.tracciato.schedule.optimization.Optimizer;

@Configuration
@RequiredArgsConstructor
class ScheduleSpringConfiguration {

    private final RouteFacade routeFacade;
    private final Optimizer optimizer;

    @Bean
    public ScheduleFacade getRouteScheduleFacade() {
        return new ScheduleFacade(routeFacade, optimizer);
    }

}
