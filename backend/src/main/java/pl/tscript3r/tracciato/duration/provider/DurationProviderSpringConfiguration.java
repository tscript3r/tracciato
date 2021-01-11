package pl.tscript3r.tracciato.duration.provider;

import com.google.maps.GeoApiContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import pl.tscript3r.tracciato.duration.provider.google.GoogleMapsDurationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static pl.tscript3r.tracciato.infrastructure.EnvironmentConst.GOOGLE_API_KEY;

@Slf4j
@Configuration
@RequiredArgsConstructor
class DurationProviderSpringConfiguration {

    private final Environment environment;

    @Bean
    @Profile("!fakeDurationProvider")
    DurationProvider getDurationProvider() {
        log.info("Creating Google Maps based travel duration provider");
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(environment.getProperty(GOOGLE_API_KEY))
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        return new GoogleMapsDurationProvider(executorService, geoApiContext);
    }

    @Bean
    @Profile("fakeDurationProvider")
    DurationProvider getFakeDurationProvider() {
        log.warn("Creating fake travel duration provider");
        return new FakeDurationProvider();
    }


}
