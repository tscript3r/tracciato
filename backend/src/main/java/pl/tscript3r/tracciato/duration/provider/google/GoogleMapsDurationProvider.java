package pl.tscript3r.tracciato.duration.provider.google;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pl.tscript3r.tracciato.duration.provider.DurationDto;
import pl.tscript3r.tracciato.duration.provider.DurationProvider;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
public class GoogleMapsDurationProvider implements DurationProvider, DisposableBean {

    private final ExecutorService executorService;
    private final GeoApiContext geoApiContext;

    public GoogleMapsDurationProvider(ExecutorService executorService, GeoApiContext geoApiContext) {
        this.executorService = executorService;
        this.geoApiContext = geoApiContext;
    }

    @Override
    public Future<DurationDto> getQuickestTravelDuration(String from, String to) {
        Callable<DurationDto> callable = () -> {
            var request = getDirectionsApiRequest(from, to);
            var directionsResult = request.await();
            return DirectionsResult2DurationDto.map(directionsResult);
        };
        return executorService.submit(callable);
    }

    private DirectionsApiRequest getDirectionsApiRequest(String from, String to) {
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(geoApiContext);
        apiRequest.origin(from);
        apiRequest.destination(to);
        apiRequest.mode(TravelMode.DRIVING);
        return apiRequest;
    }

    @Override
    public void destroy() {
        executorService.shutdown();
    }

}
