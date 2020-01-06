package pl.tscript3r.tracciato;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import javax.validation.constraints.NotNull;

import static io.restassured.RestAssured.given;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

public abstract class AbstractFeatures implements ApplicationListener<WebServerInitializedEvent> {

    public static final String PAYLOAD = "payload";

    protected int servicePort;

    protected String postRequest(String token, String uri, String content, int expectedHttpStatus) {
        var requestSpecification = given().port(servicePort);
        if (token != null)
            requestSpecification = requestSpecification.header(new Header(TOKEN_HEADER, token));
        if (content != null)
            requestSpecification = requestSpecification.body(content)
                    .contentType(ContentType.JSON);
        return requestSpecification
                .when()
                .post(uri)
                .then()
                .statusCode(expectedHttpStatus)
                .contentType(ContentType.JSON)
                .extract()
                .asString();
    }

    protected String putRequest(String token, String uri, String content, int expectedHttpStatus) {
        var requestSpecification = given().port(servicePort);
        if (token != null)
            requestSpecification = requestSpecification.header(new Header(TOKEN_HEADER, token));
        if (content != null)
            requestSpecification = requestSpecification.body(content)
                    .contentType(ContentType.JSON);
        return requestSpecification
                .when()
                .put(uri)
                .then()
                .statusCode(expectedHttpStatus)
                .contentType(ContentType.JSON)
                .extract()
                .asString();
    }

    protected String getRequest(String token, String uri, String content, int expectedHttpStatus) {
        var requestSpecification = given().port(servicePort).contentType(ContentType.JSON);
        if (token != null)
            requestSpecification = requestSpecification.header(new Header(TOKEN_HEADER, token));
        if (content != null)
            requestSpecification = requestSpecification.body(content);
        return requestSpecification
                .when()
                .get(uri)
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
