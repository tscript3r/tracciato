package pl.tscript3r.tracciato.user;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.AUTH_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.USER_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@Component
public class UserFeatures implements ApplicationListener<WebServerInitializedEvent> {

    public static final String PAYLOAD = "payload";

    @Autowired
    UserFacade userFacade;

    @Autowired
    UserSpringRepository userSpringRepository;

    private int servicePort;

    public JSONObject registerUser(String json, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(USER_MAPPING, json, expectedHttpStatus));
        var payloadJsonObject = jsonObject.getJSONObject(PAYLOAD);
        return payloadJsonObject;
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

    private String postRequest(String uri, String content, int expectedHttpStatus) {
        return given()
                .port(servicePort)
                .body(content)
                .contentType(ContentType.JSON)
                .when()
                .post(uri)
                .then()
                .statusCode(expectedHttpStatus)
                .contentType(ContentType.JSON)
                .extract()
                .asString();
    }

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent webServerInitializedEvent) {
        this.servicePort = webServerInitializedEvent.getWebServer().getPort();
    }

}
