package pl.tscript3r.tracciato.location;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;
import pl.tscript3r.tracciato.utils.StressTestResult;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.LOCATION_MAPPING;

@Slf4j
@Component
public class LocationFeatures extends AbstractFeatures implements ConcurrentStressTest {

    public JSONObject addLocation(String token, String locationJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, LOCATION_MAPPING, locationJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public JSONArray getLocations(String token, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(getRequest(token, LOCATION_MAPPING, null, expectedHttpStatus));
        try {
            return jsonObject.getJSONArray(PAYLOAD);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public StressTestResult<JSONObject> addMultipleLocations(List<String> tokens, Collection<LocationJson> locationJsons,
                                                             int executionTimeMilli) throws ExecutionException {
        Queue<LocationJson> locationsQueue = new ConcurrentLinkedQueue<>(locationJsons);
        Random random = new Random();
        return concurrentStressTest(10, locationsQueue.size(), executionTimeMilli, () -> {
            var locationJson = Objects.requireNonNull(locationsQueue.poll());
            var randomToken = tokens.get(random.nextInt(tokens.size()));
            return addLocation(randomToken, locationJson.json(), 201);
        });
    }

    public StressTestResult<JSONArray> getMultipleLocations(List<String> tokens, int executionTimeMilli) throws ExecutionException {
        Queue<String> tokenQueue = new ConcurrentLinkedQueue<>(tokens);
        return concurrentStressTest(10, tokenQueue.size(), executionTimeMilli, () ->
                getLocations(tokenQueue.poll(), 200)
        );
    }

    public List<LocationJson> generateRandomLocationJson(int count) {
        List<LocationJson> results = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < count; i++) {
            var locationDto = new LocationDto();
            locationDto.setCity(RandomStringUtils.randomAlphanumeric(10));
            results.add(new LocationJson(locationDto));
        }
        return results;
    }

}
