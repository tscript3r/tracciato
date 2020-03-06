package pl.tscript3r.tracciato.duration.provider;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FakeDurationProvider implements DurationProvider {

    public final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public final Map<String, Callable<DurationDto>> customCallableMap = new LinkedHashMap<>();
    public int travelDurationCallsCount = 0;

    @Override
    public Future<DurationDto> getQuickestTravelDuration(String from, String destination) {
        travelDurationCallsCount++;
        var key = concatenate(from, destination);
        if (customCallableMap.containsKey(key))
            return executorService.submit(customCallableMap.get(key));
        return executorService.submit(this::getRandomDuration);
    }

    public DurationDto getRandomDuration() {
        Random random = new Random();
        return new DurationDto(Duration.ofHours(random.nextInt()), random.nextLong());
    }

    public String concatenate(String from, String destination) {
        return from + " -> " + destination;
    }

    public void setCallable(String from, String destination, Callable<DurationDto> callable) {
        customCallableMap.put(concatenate(from, destination), callable);
    }

}