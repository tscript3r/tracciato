package pl.tscript3r.tracciato.schedule.optimization;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.scheduled.ScheduledFacade;

import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class OptimizerSpringConfiguration {

    private final DurationProvider durationProvider;
    private final ScheduledFacade scheduledFacade;

    @Bean
    public Optimizer getOptimizerFacade() {
        var permutationsFactory = new PermutationsFactory(durationProvider);
        return new Optimizer(permutationsFactory, Executors.newFixedThreadPool(5), scheduledFacade);
    }

}
