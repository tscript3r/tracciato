package pl.tscript3r.tracciato.duration.provider;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FakeDurationProvider implements DurationProvider {

    public final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public final Map<String, Callable<DurationDto>> customCallableMap = new LinkedHashMap<>();
    public final long meters = 100L;
    public int travelDurationCallsCount = 0;
    public Duration duration = Duration.ofHours(5);

    @Override
    public Future<DurationDto> getQuickestTravelDuration(String from, String destination) {
        travelDurationCallsCount++;
        var key = concatenate(from, destination);
        if (customCallableMap.containsKey(key))
            return executorService.submit(customCallableMap.get(key));
        return executorService.submit(this::getNextDuration);
    }

    public DurationDto getNextDuration() {
        duration = duration.plusHours(1);
        return new DurationDto(duration, meters);
    }

    public String concatenate(String from, String destination) {
        return from + " -> " + destination;
    }

    public void setCallable(String from, String destination, Callable<DurationDto> callable) {
        customCallableMap.put(concatenate(from, destination), callable);
    }

}