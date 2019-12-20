package pl.tscript3r.tracciato.route;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.user.UserFacade;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@Component
public class RouteFeatures implements ApplicationListener<WebServerInitializedEvent> {

    public static final String PAYLOAD = "payload";

    @Autowired
    UserFacade userFacade;

    @Autowired
    RouteFacade routeFacade;

    @Autowired
    RouteSpringRepository routeSpringRepository;

    private int servicePort;

    private String postRequest(String token, String uri, String content, int expectedHttpStatus) {
        return given()
                .port(servicePort)
                .header(new Header(TOKEN_HEADER, token))
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

    public JSONObject addRoute(String token, String routeJson, int expectedHttpStatus) throws JSONException {
        JSONObject jsonObject = new JSONObject(postRequest(token, ROUTE_MAPPING, routeJson, expectedHttpStatus));
        var payloadJsonObject = jsonObject.getJSONObject(PAYLOAD);
        return payloadJsonObject;
    }

    public Boolean isRouteUuidExisting(UUID uuid) {
        return routeSpringRepository.findByUuid(uuid) != null;
    }

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent webServerInitializedEvent) {
        this.servicePort = webServerInitializedEvent.getWebServer().getPort();
    }

}
