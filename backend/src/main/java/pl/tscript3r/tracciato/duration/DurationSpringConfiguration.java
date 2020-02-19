package pl.tscript3r.tracciato.duration;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;
import pl.tscript3r.tracciato.duration.provider.google.GoogleMapsDurationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
class DurationSpringConfiguration {

    // TODO: externalize key & threads count
    private final String googleMapsApiKey = "";

    @Bean
    public DurationFacade getDurationFacade() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        DurationProvider durationProvider = new GoogleMapsDurationProvider(executorService, geoApiContext);
        return new DurationFacade(durationProvider);
    }

}
