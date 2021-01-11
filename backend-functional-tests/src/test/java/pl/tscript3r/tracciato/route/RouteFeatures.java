package pl.tscript3r.tracciato.route;

import com.google.common.collect.Maps;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.location.LocationConst;
import pl.tscript3r.tracciato.location.LocationJson;
import pl.tscript3r.tracciato.route.api.NewRouteDto;
import pl.tscript3r.tracciato.stop.StopFeatures;
import pl.tscript3r.tracciato.stop.StopJson;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;
import pl.tscript3r.tracciato.utils.StressTestResult;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_MAPPING;

@Component
public class RouteFeatures extends AbstractFeatures implements ConcurrentStressTest {

    @Autowired
    UserFacade userFacade;

    @Autowired
    RouteFacade routeFacade;

    @Autowired
    StopFeatures stopFeatures;

    @Autowired
    RouteSpringRepository routeSpringRepository;

    public JSONObject postRoute(String token, String routeJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, ROUTE_MAPPING, routeJson, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public Boolean isRouteUuidExisting(UUID uuid) {
        return routeSpringRepository.findByUuid(uuid) != null;
    }

    public JSONObject getRoute(String token, UUID routeUuid, int expectedHttpStatus) throws JSONException {
        return new JSONObject(getRequest(token, getRouteMapping(routeUuid), null, expectedHttpStatus));
    }

    public String getRouteMapping(UUID routeUuid) {
        if (routeUuid != null)
            return ROUTE_MAPPING + "/" + routeUuid.toString();
        return ROUTE_MAPPING;
    }

    public List<RouteJson> generateRandomRoutesJson(int count) {
        List<RouteJson> results = Collections.synchronizedList(new ArrayList<>());
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            LocalDateTime routeStartDate = LocalDateTime.now().plusDays(2 + random.nextInt(40));
            LocalDateTime routeEndDate = routeStartDate.plusDays(2 + random.nextInt(20));
            TrafficPrediction trafficPrediction = getRandomTrafficPrediction();
            NewRouteDto newRouteDto = new NewRouteDto();
            newRouteDto.setStartDate(routeStartDate);
            newRouteDto.setMaxEndDate(routeEndDate);
            newRouteDto.setTraffic(trafficPrediction);
            newRouteDto.setName("Iteration: " + i);
            results.add(new RouteJson(newRouteDto));
        }
        return results;
    }

    public TrafficPrediction getRandomTrafficPrediction() {
        TrafficPrediction result;
        switch (new Random().nextInt(4)) { // bleh
            case 0:
                result = TrafficPrediction.NONE;
                break;
            case 2:
                result = TrafficPrediction.OPTIMISTIC;
                break;
            case 3:
                result = TrafficPrediction.PESSIMISTIC;
                break;
            default:
                result = TrafficPrediction.BEST_GUESS;
                break;
        }
        return result;
    }

    public StressTestResult<Map.Entry<String, JSONObject>> addMultipleRoutes(List<String> tokens, Collection<RouteJson> routeJsons,
                                                                             int executionTimeMilli) throws ExecutionException {
        Queue<RouteJson> routeQueue = new ConcurrentLinkedQueue<>(routeJsons);
        Random random = new Random();
        return concurrentStressTest(10, routeQueue.size(), executionTimeMilli, () -> {
            var routeJson = Objects.requireNonNull(routeQueue.poll());
            var randomToken = tokens.get(random.nextInt(tokens.size()));
            return Maps.immutableEntry(randomToken, postRoute(randomToken, routeJson.json(), 201));
        });
    }

    public StressTestResult<JSONObject> receiveMultipleRoutes(Map<String, JSONObject> routesMap, int executionTimeMilli) throws ExecutionException {
        Queue<Map.Entry<String, JSONObject>> routeQueue = new ConcurrentLinkedQueue<>();
        routesMap.forEach((token, jsonObject) -> routeQueue.add(Maps.immutableEntry(token, jsonObject)));
        return concurrentStressTest(10, routeQueue.size(), executionTimeMilli, () -> {
            var routeEntry = Objects.requireNonNull(routeQueue.poll());
            var routeUuid = routeEntry.getValue().getString("uuid");
            return getRoute(routeEntry.getKey(), UUID.fromString(routeUuid), 200);
        });
    }

    public UUID createCompleteValidRoute(String token) throws JSONException {
        // route creation
        var routeUuid = UUID.fromString(
                postRoute(token, RouteJson.newValid().json(), 201).getString("uuid")
        );

        // start location
        var startLocation = LocationJson.of(LocationConst.getBerlinLocationDto());
        stopFeatures.setStartLocation(token, routeUuid, startLocation.json(), 201);

        // end location
        var endLocation = LocationJson.of(LocationConst.getHamburgLocationDto());
        stopFeatures.setRouteEndLocation(token, routeUuid, endLocation.json(), 201);

        // stops (required >=3)
        stopFeatures.addStop(token,
                routeUuid,
                StopJson.newValid()
                        .setLocationDto(LocationConst.getEssenLocationDto())
                        .json(),
                201
        );
        stopFeatures.addStop(token,
                routeUuid,
                StopJson.newValid()
                        .setLocationDto(LocationConst.getStuttgartLocationDto())
                        .json(),
                201
        );
        stopFeatures.addStop(token,
                routeUuid,
                StopJson.newValid()
                        .setLocationDto(LocationConst.getLuneburgLocationDto())
                        .json(),
                201
        );
        return routeUuid;
    }


}
