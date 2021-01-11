package pl.tscript3r.tracciato;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.route.RouteFeatures;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pl.tscript3r.tracciato.AbstractFeatures.PAYLOAD;
import static pl.tscript3r.tracciato.infrastructure.EndpointsMappings.ROUTE_MAPPING;
import static pl.tscript3r.tracciato.infrastructure.spring.security.SecurityConstants.TOKEN_HEADER;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Invalid requests")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class InvalidRequestsTests extends AbstractFunctionalTests {

    @Autowired
    RouteFeatures routeFeatures;

    @BeforeAll
    public void before() throws JSONException {
        registerUserAndLogin();
    }

    @Test
    @DisplayName("Body not readable")
    void bodyNotReadable() throws JSONException {
        var json = new JSONObject(given().port(routeFeatures.servicePort)
                .contentType(ContentType.JSON)
                .header(new Header(TOKEN_HEADER, token))
                .body("::::")
                .when()
                .post(ROUTE_MAPPING)
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .extract()
                .asString());
        assertNotNull(json.get(PAYLOAD));
    }

    @Test
    @DisplayName("Path variable invalid")
    void pathVariableInvalid() throws JSONException {
        var json = new JSONObject(given().port(routeFeatures.servicePort)
                .contentType(ContentType.JSON)
                .header(new Header(TOKEN_HEADER, token))
                .when()
                .get(ROUTE_MAPPING + "/@#$s")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .extract()
                .asString());
        assertNotNull(json.get(PAYLOAD));
    }

    @Test
    @DisplayName("Request method not supported")
    void requestMethodNotSupported() throws JSONException {
        var json = new JSONObject(given().port(routeFeatures.servicePort)
                .contentType(ContentType.JSON)
                .header(new Header(TOKEN_HEADER, token))
                .when()
                .patch(ROUTE_MAPPING)
                .then()
                .statusCode(405)
                .contentType(ContentType.JSON)
                .extract()
                .asString());
        assertNotNull(json.get(PAYLOAD));
    }

    @Test
    @DisplayName("Not implemented")
    void notImplemented() throws JSONException {
        var json = new JSONObject(given().port(routeFeatures.servicePort)
                .contentType(ContentType.JSON)
                .header(new Header(TOKEN_HEADER, token))
                .when()
                .get("/api/test/not_implemented")
                .then()
                .statusCode(501)
                .contentType(ContentType.JSON)
                .extract()
                .asString());
        assertNotNull(json.get(PAYLOAD));
    }

    @Test
    @DisplayName("Any not handled application exception")
    void anyException() throws JSONException {
        var json = new JSONObject(given().port(routeFeatures.servicePort)
                .contentType(ContentType.JSON)
                .header(new Header(TOKEN_HEADER, token))
                .when()
                .get("/api/test/any_exception")
                .then()
                .statusCode(500)
                .contentType(ContentType.JSON)
                .extract()
                .asString());
        assertNotNull(json.get(PAYLOAD));
    }

}
