package pl.tscript3r.tracciato.user;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.AbstractFeatures;
import pl.tscript3r.tracciato.user.api.UserDto;
import pl.tscript3r.tracciato.utils.ConcurrentStressTest;
import pl.tscript3r.tracciato.utils.StressTestResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.AUTH_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@Slf4j
@Component
public class UserFeatures extends AbstractFeatures implements ConcurrentStressTest {

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserSpringRepository userSpringRepository;

    int threadsCount = 10;

    public JSONObject registerUser(String json, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(null, USER_MAPPING, json, expectedHttpStatus));
        return jsonObject.getJSONObject(PAYLOAD);
    }

    public String loginUser(String username, String password, int expectedHttpStatus) {
        return given()
                .port(servicePort)
                .when()
                .post(AUTH_MAPPING + "?username=" + username + "&password=" + password)
                .then()
                .statusCode(expectedHttpStatus)
                .extract()
                .header(TOKEN_HEADER);
    }

    public Boolean validateToken(String token) {
        return userFacade.validateAndGetUuidFromToken(token).isRight();
    }

    public Boolean isUserUuidExisting(String uuid) {
        // TODO for now there is no other way to check if the user has been added
        return userSpringRepository.findByUuid(UUID.fromString(uuid)) != null;
    }

    public Boolean isUsernameExisting(String username) {
        return userSpringRepository.existsByUsernameIgnoreCase(username);
    }

    public List<UserJson> generateRandomUsersJson(int count) {
        List<UserJson> results = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < count; i++) {
            var userDto = new UserDto();
            userDto.setUsername(RandomStringUtils.randomAlphanumeric(10));
            userDto.setPassword(RandomStringUtils.randomAlphanumeric(10));
            userDto.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@mail.com");
            results.add(new UserJson(userDto));
        }
        return results;
    }

    public StressTestResult<JSONObject> registerUsers(Collection<UserJson> usersJsons, int executionTimeMilli) throws ExecutionException {
        Queue<UserJson> userQueue = new ConcurrentLinkedQueue<>(usersJsons);
        return concurrentStressTest(threadsCount, userQueue.size(), executionTimeMilli, () ->
                registerUser(Objects.requireNonNull(userQueue.poll()).json(), 201));
    }

    public StressTestResult<Map.Entry<UserJson, String>> loginUsers(Collection<UserJson> usersJsons, int executionTimeMilli) throws ExecutionException {
        Queue<UserJson> userQueue = new ConcurrentLinkedQueue<>(usersJsons);
        return concurrentStressTest(threadsCount, userQueue.size(), executionTimeMilli, () -> {
            var userJson = Objects.requireNonNull(userQueue.poll());
            var token = loginUser(userJson.getUsername(), userJson.object.getPassword(), 200);
            return Maps.immutableEntry(userJson, token);
        });
    }

    public Map<UserJson, String> createRandomUsersAndGetTokens(int count, int executionTimeMilli) throws ExecutionException {
        var randomGeneratedUsers = generateRandomUsersJson(count);
        var registrationResults = registerUsers(randomGeneratedUsers, executionTimeMilli);
        assertEquals(0, registrationResults.getUncompletedCount(), "Registration probably didnt had enough time to finish");
        var loginResults = loginUsers(randomGeneratedUsers, executionTimeMilli);
        assertEquals(0, loginResults.getUncompletedCount(), "Login probably didnt had enough time to finish");
        Map<UserJson, String> results = new ConcurrentHashMap<>();
        var unwrappedResults = unwrapFutures(loginResults.getFuturesList());
        unwrappedResults.forEach(o -> results.put(o.getKey(), o.getValue()));
        return results;
    }

}
