package pl.tscript3r.tracciato.duration.provider.google;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pl.tscript3r.tracciato.duration.provider.LocationDto2StringLocation;
import pl.tscript3r.tracciato.location.api.LocationDto;
import pl.tscript3r.tracciato.utils.ReplaceCamelCaseAndUnderscores;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("LocationDto2StringLocation")
@DisplayNameGeneration(ReplaceCamelCaseAndUnderscores.class)
class LocationDto2StringLocationTest {

    public static final String CITY = "Berlin";
    public static final String COUNTRY = "Germany";
    public static final String STREET = "Warschauersstrasse";
    public static final String NUMBER = "35";
    public static final String ZIP = "10115";

    LocationDto locationDto;

    @BeforeEach
    void setUp() {
        locationDto = new LocationDto();
        locationDto.setCity(CITY);
        locationDto.setCountry(COUNTRY);
        locationDto.setStreet(STREET);
        locationDto.setNumber(NUMBER);
        locationDto.setZip(ZIP);
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_AllLocationDataIsGiven() {
        // Expected: STREET NUMBER, ZIP CITY, COUNTRY
        var expectedOutput = STREET + " " + NUMBER + ", " + ZIP + " " + CITY + ", " + COUNTRY;
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_StreetIsNotGiven() {
        // Expected: ZIP CITY, COUNTRY (so NUMBER is set but will be ignored by building output string)
        var expectedOutput = ZIP + " " + CITY + ", " + COUNTRY;
        locationDto.setStreet(null);
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_StreetNumberIsNotGiven() {
        locationDto.setNumber(null);
        // Expected: STREET, ZIP CITY, COUNTRY
        var expectedOutput = STREET + ", " + ZIP + " " + CITY + ", " + COUNTRY;
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_ZipCodeIsNotGiven() {
        locationDto.setZip(null);
        // Expected: STREET NUMBER, CITY, COUNTRY
        var expectedOutput = STREET + " " + NUMBER + ", " + CITY + ", " + COUNTRY;
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_CountryIsNotGiven() {
        locationDto.setCountry(null);
        // Expected: STREET NUMBER, ZIP CITY, COUNTRY
        var expectedOutput = STREET + " " + NUMBER + ", " + ZIP + " " + CITY;
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_OnlyCityGiven() {
        locationDto = new LocationDto();
        locationDto.setCity(CITY);
        assertEquals(CITY, LocationDto2StringLocation.map(locationDto));
    }

    @Test
    void map_Should_ReturnWellFormattedAddressString_When_OnlyCityAndZipGiven() {
        locationDto = new LocationDto();
        locationDto.setCity(CITY);
        locationDto.setZip(ZIP);
        var expectedOutput = ZIP + " " + CITY;
        assertEquals(expectedOutput, LocationDto2StringLocation.map(locationDto));
    }

}