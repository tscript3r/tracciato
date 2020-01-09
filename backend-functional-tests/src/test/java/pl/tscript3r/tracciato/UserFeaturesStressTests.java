package pl.tscript3r.tracciato;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.user.UserJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("User features stress test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EnableAutoConfiguration
public class UserFeaturesStressTests extends AbstractFunctionalTests implements ConcurrentStressTest {

    List<UserJson> usersList = new ArrayList<>();
    Map<UserJson, String> usersTokens = new ConcurrentHashMap<>();

    @BeforeAll
    public void before() {
        usersList = userFeatures.generateRandomUsersJson(1000);
    }

    @Test
    @Order(0)
    @DisplayName("Registering 1000 users")
    void registerUsers() throws ExecutionException {
        var results = userFeatures.registerUsers(usersList, 100_000);
        assertEquals(0, results.getUncompletedCount());
        log.info("Register: {}", results.toString());
    }

    @Test
    @Order(1)
    @DisplayName("Login 1000 users")
    void loginUsers() throws ExecutionException {
        var results = userFeatures.loginUsers(usersList, 100_000);
        assertEquals(0, results.getUncompletedCount());
        var unwrappedResults = unwrapFutures(results.futuresList);
        unwrappedResults.forEach(o -> usersTokens.put(o.getKey(), o.getValue()));
        log.info("Login: {}", results.toString());
    }

}
