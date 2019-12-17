package pl.tscript3r.tracciato.steps;

import io.restassured.http.ContentType;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.tscript3r.tracciato.infrastructure.EndpointsMappings;

import javax.validation.constraints.NotNull;

import static io.restassured.RestAssured.given;

@Component
public class UserFeatures implements ApplicationListener<WebServerInitializedEvent> {

    private int servicePort;

    public String registerUser(String json) {
        return given()
                    .port(servicePort)
                    .body(json)
                    .contentType(ContentType.JSON)
               .when()
                    .post(EndpointsMappings.USER_MAPPING)
                .then()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .extract()
                    .asString();
    }

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent webServerInitializedEvent) {
        this.servicePort = webServerInitializedEvent.getWebServer().getPort();
    }

}
