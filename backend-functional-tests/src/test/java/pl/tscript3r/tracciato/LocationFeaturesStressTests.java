package pl.tscript3r.tracciato;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.location.LocationFeatures;
import pl.tscript3r.tracciato.location.LocationJson;
import pl.tscript3r.tracciato.user.UserJson;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Location features stress tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableAutoConfiguration
public class LocationFeaturesStressTests extends AbstractFunctionalTests implements ConcurrentStressTest {

    @Autowired
    LocationFeatures locationFeatures;

    Map<UserJson, String> usersList;
    List<LocationJson> locationsList;

    @BeforeAll
    public void before() throws ExecutionException {
        usersList = userFeatures.createRandomUsersAndGetTokens(1000, 100_000);
        locationsList = locationFeatures.generateRandomLocationJson(10000);
    }

    @Test
    @Order(0)
    @DisplayName("Adding 10000 locations for 1000 users")
    void addLocations() throws ExecutionException {
        var results = locationFeatures.addMultipleLocations(new ArrayList<>(usersList.values()), locationsList, 100_000);
        assertEquals(0, results.getUncompletedCount());
        log.info("addLocations: {}", results.toString());
    }

    @Test
    @Order(1)
    @DisplayName("Receiving locations from 1000 users")
    void getLocations() throws ExecutionException {
        var results = locationFeatures.getMultipleLocations(new ArrayList<>(usersList.values()), 100_000);
        assertEquals(0, results.getUncompletedCount());
        log.info("getLocations: {}", results.toString());
    }

}
