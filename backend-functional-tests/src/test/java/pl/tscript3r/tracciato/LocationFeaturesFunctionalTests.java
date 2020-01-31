package pl.tscript3r.tracciato;

import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import pl.tscript3r.tracciato.location.LocationFeatures;
import pl.tscript3r.tracciato.location.LocationJson;
import pl.tscript3r.tracciato.location.LocationSpringRepository;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.user.UserJson;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_PASSWORD;
import static pl.tscript3r.tracciato.user.UserJson.EXISTING_USERNAME;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Location features")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@EnableAutoConfiguration
public class LocationFeaturesFunctionalTests extends AbstractFunctionalTests {

    @Autowired
    UserFacade userFacade;

    @Autowired
    LocationFeatures locationFeatures;

    @Autowired
    LocationSpringRepository locationSpringRepository;

    String token;

    @BeforeAll
    public void before() throws JSONException {
        if (!userFeatures.isUsernameExisting(EXISTING_USERNAME))
            userFeatures.registerUser(UserJson.existing().json(), 201);
        token = userFeatures.loginUser(EXISTING_USERNAME, EXISTING_PASSWORD, 200);
    }

    @Test
    void addLocation_ReturnForbiddenStatus_When_RequestDoesNotContainTokenHeader() throws JSONException {
        locationFeatures.addLocation(null, LocationJson.valid().json(), 403);
    }

    @Test
    void addLocation_Should_SuccessfullySaveNewLocation_When_ValidJsonGiven() throws JSONException {
        locationFeatures.addLocation(token, LocationJson.valid().json(), 201);
    }

    @Test
    void addLocation_Should_RejectNewLocation_When_NotValidJsonGiven() throws JSONException {
        locationFeatures.addLocation(token, LocationJson.invalid().json(), 400);
    }

    @Test
    void getAll_Should_ReturnForbiddenStatus_When_RequestDoesNotContainTokenHeader() throws JSONException {
        locationFeatures.getLocations(null, 403);
    }

    @Test
    void getAll_Should_ReturnCurrentLocationsList_When_RequestContainsValidToken() throws JSONException {
        // given
        var uuid = userFacade.validateAndGetUuidFromToken(token).get();
        var currentSize = locationSpringRepository.findAllByOwnerUuid(uuid).size();
        if (currentSize == 0) {
            // just to be sure that getLocations does not always return empty list
            locationFeatures.addLocation(token, LocationJson.valid().json(), 201);
            currentSize++;
        }

        // when
        var results = locationFeatures.getLocations(token, 200);

        // then
        assertEquals(currentSize, results.length());
    }

}
