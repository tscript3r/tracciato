package pl.tscript3r.tracciato.duration.provider;

import com.google.maps.GeoApiContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import pl.tscript3r.tracciato.duration.provider.google.GoogleMapsDurationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static pl.tscript3r.tracciato.infrastructure.EnvironmentConst.GOOGLE_API_KEY;

@Configuration
@RequiredArgsConstructor
public class DurationProviderSpringConfiguration {

    private final Environment environment;

    @Bean
    DurationProvider getDurationProvider() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(environment.getProperty(GOOGLE_API_KEY))
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        return new GoogleMapsDurationProvider(executorService, geoApiContext);
    }

}
