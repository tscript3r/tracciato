package pl.tscript3r.tracciato.route.schedule.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;

import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class RouteSchedulerSpringConfiguration {

    private final DurationProvider durationProvider;

    @Bean
    public RouteScheduler getRouteSchedulerFacade() {
        var routePermutationsFactory = new RoutePermutationsFactory(durationProvider);
        return new RouteScheduler(routePermutationsFactory, Executors.newFixedThreadPool(5));
    }

}
