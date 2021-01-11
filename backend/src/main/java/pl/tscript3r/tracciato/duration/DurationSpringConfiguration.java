package pl.tscript3r.tracciato.duration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;

@Configuration
@Slf4j
@RequiredArgsConstructor
class DurationSpringConfiguration {

    private final DurationProvider durationProvider;

    @Bean
    public DurationFacade getDurationFacade() {
        return new DurationFacade(durationProvider);
    }

}
