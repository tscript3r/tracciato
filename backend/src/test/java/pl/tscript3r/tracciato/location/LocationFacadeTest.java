package pl.tscript3r.tracciato.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.tscript3r.tracciato.infrastructure.response.InternalResponse;
import pl.tscript3r.tracciato.user.UserFacade;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.tracciato.location.LocationConst.getValidLocationDto;

@DisplayName("Location facade")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LocationFacadeTest {

    @Mock
    UserFacade userFacade;

    LocationFacade locationFacade;

    LocationRepositoryAdapter locationInMemoryRepositoryAdapter;

    final UUID userUuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        locationInMemoryRepositoryAdapter = new LocationInMemoryRepositoryAdapter();
        locationFacade = LocationSpringConfiguration.getInMemoryLocationFacade(userFacade,
                locationInMemoryRepositoryAdapter);
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.payload(userUuid));
    }

    @Test
    void add_Should_SuccessfullySaveLocation_When_ValidLocationDtoGiven() {
        // given
        var locationDto = getValidLocationDto();

        // when
        var results = locationFacade.addLocation("mocked", locationDto);

        // then
        assertTrue(results.isRight());
        var locationUuid = results.get().getUuid();
        assertTrue(locationInMemoryRepositoryAdapter.findByUuid(locationUuid).isDefined());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_InvalidLocationDtoGiven() {
        // given
        var locationDto = getValidLocationDto();
        locationDto.setCity(null);

        // when
        var results = locationFacade.addLocation("mocked", locationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void add_Should_ReturnFailureResponse_When_InvalidTokenGiven() {
        // given
        var locationDto = getValidLocationDto();
        when(userFacade.validateAndGetUuidFromToken(any())).thenReturn(InternalResponse.failure(null));

        // when
        var results = locationFacade.addLocation("mocked", locationDto);

        // then
        assertTrue(results.isLeft());
    }

    @Test
    void getAllLocationsFromUser_Should_ReturnEmptyCollection_When_UserHasNoLocationsAdded() {
        // when
        var result = locationFacade.getAllLocationsFromUser("mocked");

        // then
        assertEquals(0, result.get().size());
    }

    @Test
    void getAllLocationsFromUser_Should_ReturnAddedLocations_When_UserHasLocationsAdded() {
        // given
        var locationDto = getValidLocationDto();
        locationFacade.addLocation("mocked", locationDto);

        // when
        var result = locationFacade.getAllLocationsFromUser("mocked");

        // then
        assertEquals(1, result.get().size());
    }

    @Test
    void getEntityByUuid_Should_ReturnLocationEntity_When_LocationUuidExists() {
        // given
        var locationDto = getValidLocationDto();
        var addedLocationUuid = locationFacade.addLocation("mocked", locationDto).get().getUuid();

        // when
        var results = locationFacade.getLocationEntityByUuid(addedLocationUuid);

        // then
        assertTrue(results.isRight());
    }

    @Test
    void getEntityByUuid_Should_ReturnFailureResponse_When_LocationUuidNotExists() {
        // given
        var locationDto = getValidLocationDto();
        locationFacade.addLocation("mocked", locationDto).get().getUuid();

        // when
        var results = locationFacade.getLocationEntityByUuid(UUID.randomUUID());

        // then
        assertTrue(results.isLeft());
    }

}