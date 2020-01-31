package pl.tscript3r.tracciato;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.user.UserJson;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Route features stress tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableAutoConfiguration
public class RouteFeaturesStressTests extends AbstractFunctionalTests implements ConcurrentStressTest {

    Map<UserJson, String> usersMap;
    Map<String, JSONObject> routesMap = new ConcurrentHashMap<>();

    @BeforeAll
    public void before() throws ExecutionException {
        usersMap = userFeatures.createRandomUsersAndGetTokens(500, 100_000);
    }

    @Test
    @Order(0)
    @DisplayName("Adding 1000 routes to random users")
    void addRoutes() throws ExecutionException {
        var randomRouteJsons = routeFeatures.generateRandomRoutesJson(1000);
        var results = routeFeatures.addMultipleRoutes(new ArrayList<>(usersMap.values()),
                randomRouteJsons, 100_000);
        unwrapFutures(results.getFuturesList())
                .forEach(o -> routesMap.put(o.getKey(), o.getValue()));
        assertEquals(0, results.getUncompletedCount());
        log.info("addRoutes: {}", results.toString());
    }

    @Test
    @Order(1)
    @DisplayName("Requesting for added routes")
    void receiveRoutes() throws ExecutionException {
        var results = routeFeatures.receiveMultipleRoutes(routesMap, 100_000);
        assertEquals(0, results.getUncompletedCount());
        log.info("receiveRoutes: {}", results.toString());
    }

}
